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
package co.edu.unicauca.trabajogradogkr.model.gbhs;

import co.edu.unicauca.trabajogradogkr.model.distance.Distance;
import co.edu.unicauca.trabajogradogkr.exception.DistanceException;
import co.edu.unicauca.trabajogradogkr.model.Agent;
import co.edu.unicauca.trabajogradogkr.model.AgentComparator;
import co.edu.unicauca.trabajogradogkr.model.Cluster;
import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.model.kmeans.KMeans;
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.ObjectiveFunction;
import co.edu.unicauca.trabajogradogkr.service.Config;
import co.edu.unicauca.trabajogradogkr.utils.Report;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class GBHSCentroids implements GBHS {

    @Override
    public Agent process(int hms, int maxImprovisations, int maxK, int maxKMeans, double pKmeans,
            double minPar, double maxPar, double hmcr, double pOptimize,
            Dataset dataset, ObjectiveFunction f, boolean log, Random random, Distance distance) {

        try {
            int repeated = 0;
            int curHms = 0;
            int bad = 0;
            File resultFolder = Config.getInstance().getResultFolder();
            File resultado = new File(resultFolder, "registros_"
                    + dataset.getName() + "_" + f.toString() + ".txt");
            String logPath = resultado.getAbsolutePath();
            Report report = new Report(logPath);
            AgentComparator agentComparator = new AgentComparator(f.minimizes());
            List<Agent> harmonyMemory;
            double par;
            GBHSUtils utils = new GBHSUtils();
            KMeans kmeans = new KMeans();

            harmonyMemory = utils.generateHarmonyMemory(hms, maxK, dataset, random, f,
                    agentComparator, random, distance);

            curHms = harmonyMemory.size();

            if (log) {
                report.writeHarmonyMemory(harmonyMemory, "Initial Harmony Memory");
            }

            utils.optimizeMemory(maxKMeans, pKmeans, pOptimize, random, dataset, f,
                    agentComparator, harmonyMemory, distance);

            if (log) {
                report.writeHarmonyMemory(harmonyMemory, "Optimized Harmony Memory");
            }

            for (int cIt = 0; cIt < maxImprovisations; cIt++) {
                par = minPar + ((maxPar - minPar) / maxImprovisations) * cIt;
                int k = utils.chooseK(maxK, hmcr, par, random, harmonyMemory);
                Agent newSolution = new Agent();
                newSolution.setClusters(new Cluster[k]);

                for (int j = 0; j < k; j++) {
                    double num = random.nextDouble();
                    Cluster c;
                    if (num <= hmcr) {
                        int pos = random.nextInt(curHms);
                        int nc = harmonyMemory.get(pos).getClusters().length;
                        nc = random.nextInt(nc);
                        c = harmonyMemory.get(pos).getClusters()[nc];

                        //TODO: Modificación al par
                        if (random.nextDouble() < par) {
                            nc = harmonyMemory.get(0).getClusters().length;
                            nc = random.nextInt(nc);
                            c = harmonyMemory.get(0).getClusters()[nc];
                        }
                    } else {
                        c = Cluster.randCluster(dataset, random);
                    }
                    newSolution.getClusters()[j] = c;
                }

                newSolution.reallocateRecords(dataset, distance);

                if (random.nextDouble() < pOptimize) {
                    newSolution = kmeans.process(newSolution, dataset, distance, pKmeans, maxKMeans);
                }

                newSolution.setFitness(f.calculate(newSolution, dataset, distance));

                newSolution.calcClusters(dataset);
                if (!utils.testSolution(newSolution)) {
                    cIt--;
                    continue;
                }
                
                if (utils.repeatedSolution(newSolution, agentComparator, harmonyMemory)) {
                    repeated++;
                } else {

                    if (curHms < hms) {
                        harmonyMemory.add(newSolution);
                        curHms++;
                    } else if (!utils.replaceSolution(harmonyMemory, newSolution, agentComparator)) {
                        bad++;
                    }

                    Collections.sort(harmonyMemory, agentComparator);

                    if (utils.uniformMemory(harmonyMemory)) {
                        utils.regenerateMemory(harmonyMemory, maxK,
                                maxImprovisations, pOptimize, 0.0, dataset, f,
                                agentComparator, random, distance);
                    }
                }

                if (log) {
                    report.writeHarmonyMemory(harmonyMemory, "Harmony Memory iteration " + cIt);
                }
            }

            if (log) {
                for (Agent agent : harmonyMemory) {
                    report.writeLine(agent.toString());
                }
            }
            System.out.println("Repetidos: " + repeated);
            System.out.println("malos: " + bad);
            report.close();
            return harmonyMemory.get(0);
        } catch (DistanceException ex) {
            Logger.getLogger(GBHSRecords.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public GBHS newInstance() {
        return new GBHSCentroids();
    }

    @Override
    public String toString() {
        return "Centroids";
    }

}
