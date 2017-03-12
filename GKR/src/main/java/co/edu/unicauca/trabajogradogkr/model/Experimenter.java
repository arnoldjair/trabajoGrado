/*
 * Copyright (C) 2016 Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package co.edu.unicauca.trabajogradogkr.model;

import co.edu.unicauca.trabajogradogkr.model.distance.Distance;
import co.edu.unicauca.trabajogradogkr.model.distance.DistanceFactory;
import co.edu.unicauca.trabajogradogkr.model.gbhs.GBHS;
import co.edu.unicauca.trabajogradogkr.model.gbhs.GBHSFactory;
import co.edu.unicauca.trabajogradogkr.model.kmeans.KMeans;
import co.edu.unicauca.trabajogradogkr.model.kmeans.KMeansFactory;
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.ObjectiveFunction;
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.ObjectiveFunctionFactory;
import co.edu.unicauca.trabajogradogkr.model.task.Task;
import co.edu.unicauca.trabajogradogkr.service.DatasetServiceImpl;
import co.edu.unicauca.trabajogradogkr.service.DatasetService;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class Experimenter implements Callable<Result> {

    private int hms;
    private int maxImprovisations;
    private int maxK;
    private int maxKMeans;
    private int nExp;
    private double minPar;
    private double maxPar;
    private double hmcr;
    private double pOptimize;
    private Dataset dataset;
    private ObjectiveFunction f;
    private Random random;
    private GBHS algorithm;
    private Agent[] solutions;
    private ContingencyMatrix[] contingencyMatrices;
    private int[] icc;
    private int[] iic;
    private double[] er;
    private Distance distance;
    private final KMeans kmeans;
    private final DatasetService datasetService;
    private final String initialization;

    public Experimenter(Task task) throws FileNotFoundException {
        this.datasetService = new DatasetServiceImpl();
        this.hms = task.getHms();
        this.maxImprovisations = task.getnIt();
        this.maxK = task.getMaxK();
        this.maxKMeans = task.getMaxKMeans();
        this.nExp = task.getnExp();
        this.minPar = task.getMinPar();
        this.maxPar = task.getMaxPar();
        this.hmcr = task.getHmcr();
        this.pOptimize = task.getPo();
        this.dataset = this.datasetService.byName(task.getDataset(), task.isNormalize());
        Map<String, Object> filters = task.getFilters();
        if (filters != null) {
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                this.dataset = this.datasetService.filter(this.dataset, key, value);
            }
        }
        this.f = ObjectiveFunctionFactory.getObjectiveFuncion(task.getObjectiveFunction());
        if (task.getSeed() != 0) {
            this.random = new Random(task.getSeed());
        } else {
            this.random = new Random();
        }
        this.algorithm = GBHSFactory.getGBHS(task.getAlgorithm());
        this.solutions = new Agent[nExp];
        this.contingencyMatrices = new ContingencyMatrix[nExp];
        this.icc = new int[nExp];
        this.iic = new int[nExp];
        this.er = new double[nExp];
        this.distance = DistanceFactory.getDistance(task.getDistance());
        this.kmeans = KMeansFactory.getKMeans(task.getKmeansAlgorithm());
        this.initialization = task.getInitialization();

    }

    public synchronized Result experiment() {
        Result ret = new Result();
        int n = dataset.getN();

        for (int i = 0; i < nExp; i++) {
            GBHS currAlgorithm = algorithm.newInstance();
            Agent cSolucion = currAlgorithm.process(hms, maxImprovisations,
                    maxK, maxKMeans, 0.0, minPar, maxPar, hmcr, pOptimize,
                    dataset, f, false, new Random(i), distance, kmeans, initialization);
            solutions[i] = cSolucion;

            ContingencyMatrix contingencyMatrix = new ContingencyMatrix(cSolucion, dataset);
            contingencyMatrices[i] = contingencyMatrix;

            ECVM ecvm = new ECVM(contingencyMatrix);
            icc[i] = ecvm.getIcc();
            iic[i] = n - icc[i];
            er[i] = ((double) iic[i] / n) * 100;
        }

        //ret.setAgents(solutions);
        ret.setIcc(icc);
        ret.setIic(iic);
        ret.setEr(er);
        ret.setNumExperiments(nExp);
        ret.calcAverages();
        //ret.setContingencyMatrices(contingencyMatrices);
        ret.setDataset(dataset.toString());
        ret.setObjectiveFunction(f.toString());
        ret.setAlgorithm(algorithm.toString());
        ret.setDistance(distance.toString());
        return ret;
    }

    public int getHms() {
        return hms;
    }

    public void setHms(int hms) {
        this.hms = hms;
    }

    public int getMaxImprovisations() {
        return maxImprovisations;
    }

    public void setMaxImprovisations(int maxImprovisations) {
        this.maxImprovisations = maxImprovisations;
    }

    public int getMaxK() {
        return maxK;
    }

    public void setMaxK(int maxK) {
        this.maxK = maxK;
    }

    public int getMaxKMeans() {
        return maxKMeans;
    }

    public void setMaxKMeans(int maxKMeans) {
        this.maxKMeans = maxKMeans;
    }

    public int getnExp() {
        return nExp;
    }

    public void setnExp(int nExp) {
        this.nExp = nExp;
    }

    public double getMinPar() {
        return minPar;
    }

    public void setMinPar(double minPar) {
        this.minPar = minPar;
    }

    public double getMaxPar() {
        return maxPar;
    }

    public void setMaxPar(double maxPar) {
        this.maxPar = maxPar;
    }

    public double getHmcr() {
        return hmcr;
    }

    public void setHmcr(double hmcr) {
        this.hmcr = hmcr;
    }

    public double getpOptimize() {
        return pOptimize;
    }

    public void setpOptimize(double pOptimize) {
        this.pOptimize = pOptimize;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public ObjectiveFunction getF() {
        return f;
    }

    public void setF(ObjectiveFunction f) {
        this.f = f;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public GBHS getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(GBHS algorithm) {
        this.algorithm = algorithm;
    }

    public Agent[] getSolutions() {
        return solutions;
    }

    public void setSolutions(Agent[] solutions) {
        this.solutions = solutions;
    }

    public ContingencyMatrix[] getContingencyMatrices() {
        return contingencyMatrices;
    }

    public void setContingencyMatrices(ContingencyMatrix[] contingencyMatrices) {
        this.contingencyMatrices = contingencyMatrices;
    }

    public int[] getIcc() {
        return icc;
    }

    public void setIcc(int[] icc) {
        this.icc = icc;
    }

    public int[] getIic() {
        return iic;
    }

    public void setIic(int[] iic) {
        this.iic = iic;
    }

    public double[] getEr() {
        return er;
    }

    public void setEr(double[] er) {
        this.er = er;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    @Override
    public Result call() throws Exception {
        return this.experiment();
    }

}
