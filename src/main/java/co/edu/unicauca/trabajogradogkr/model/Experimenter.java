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

import co.edu.unicauca.trabajogradogkr.distance.Distance;
import co.edu.unicauca.trabajogradogkr.model.gbhs.GBHS;
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.ObjectiveFunction;
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
    private boolean log;
    private Random random;
    private GBHS algorithm;
    private Agent[] solutions;
    private ContingencyMatrix[] contingencyMatrices;
    private int[] icc;
    private int[] iic;
    private double[] er;
    private String id;
    private Distance distance;

    /**
     *
     * @param hms
     * @param maxImprovisations
     * @param maxK
     * @param maxKMeans
     * @param nExp
     * @param minPar
     * @param maxPar
     * @param hmcr
     * @param pOptimize
     * @param dataset
     * @param f
     * @param log
     * @param seed
     * @param algorithm
     * @param id
     * @param distance
     */
    public Experimenter(int hms, int maxImprovisations, int maxK, int maxKMeans,
            int nExp, double minPar, double maxPar, double hmcr, double pOptimize,
            Dataset dataset, ObjectiveFunction f, boolean log, long seed,
            GBHS algorithm, String id, Distance distance) {
        this.hms = hms;
        this.maxImprovisations = maxImprovisations;
        this.maxK = maxK;
        this.maxKMeans = maxKMeans;
        this.nExp = nExp;
        this.minPar = minPar;
        this.maxPar = maxPar;
        this.hmcr = hmcr;
        this.pOptimize = pOptimize;
        this.dataset = dataset;
        this.f = f;
        this.log = log;
        if (seed != 0) {
            this.random = new Random(seed);
        } else {
            this.random = new Random();
        }
        this.algorithm = algorithm;
        this.solutions = new Agent[nExp];
        this.contingencyMatrices = new ContingencyMatrix[nExp];
        this.icc = new int[nExp];
        this.iic = new int[nExp];
        this.er = new double[nExp];
        this.id = id;
        this.distance = distance;
    }
    
    public synchronized Result experiment() {
        Result ret = new Result();
        int n = dataset.getN();
        
        for (int i = 0; i < nExp; i++) {
            GBHS currAlgorithm = algorithm.newInstance();
            Agent cSolucion = currAlgorithm.process(hms, maxImprovisations,
                    maxK, maxKMeans, minPar, maxPar, hmcr, pOptimize,
                    dataset, f, log, random, distance);
            solutions[i] = cSolucion;
            
            ContingencyMatrix contingencyMatrix = new ContingencyMatrix(cSolucion, dataset);
            contingencyMatrices[i] = contingencyMatrix;
            
            ECVM ecvm = new ECVM(contingencyMatrix);
            icc[i] = ecvm.getIcc();
            iic[i] = n - icc[i];
            er[i] = ((double) iic[i] / n) * 100;
        }
        
        ret.setAgents(solutions);
        ret.setIcc(icc);
        ret.setIic(iic);
        ret.setEr(er);
        ret.setId(id);
        ret.setNumExperiments(nExp);
        ret.calcAverages();
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
    
    public boolean isLog() {
        return log;
    }
    
    public void setLog(boolean log) {
        this.log = log;
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
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
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
