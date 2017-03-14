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
import co.edu.unicauca.trabajogradogkr.model.kmeans.BasicKMeansImpl;
import co.edu.unicauca.trabajogradogkr.model.kmeans.KMeans;
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.ObjectiveFunction;
import co.edu.unicauca.trabajogradogkr.model.rgs.Partition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class GBHSUtils {

    public List<Agent> generateHarmonyMemory(int hms, int maxK, Dataset dataset,
            ObjectiveFunction f, Comparator comparador, Random random,
            Distance distance, String initialization, boolean fixedK) throws DistanceException, Exception {
        List<Agent> harmonyMemory = new ArrayList<>();
        int k;
        if (fixedK) {
            if (dataset.getK() == 0) {
                throw new Exception("El dataset no tiene el valor de k");
            }
        }

        for (int i = 0; i < hms; i++) {
            if (fixedK) {
                if (dataset.getK() == 0) {
                    throw new Exception("El dataset no tiene el valor de k");
                }
                k = dataset.getK();
            } else {
                k = random.nextInt(maxK) + 2;
            }
            Partition tmp;
            switch (initialization) {
                case "random":
                    tmp = Partition.randPartition(dataset.getN(), k, random);
                    break;
                case "kmeanspp":
                    tmp = Partition.RandPartitionKmeanspp(k, dataset, distance, random);
                    break;
                default:
                    throw new Exception("El método de init buen hombre");
            }
            if (tmp.getK() < 2) {
                Logger.getLogger("Error").log(Level.SEVERE, "K < 2");
                i--;
            }
            if (fixedK) {
                if (tmp.getK() != dataset.getK()) {
                    continue;
                }
            }

            Agent atmp = new Agent(tmp);
            atmp.calcClusters(dataset);
            double fitness = f.calculate(atmp, dataset, distance);

            if (Double.isNaN(fitness)) {
                i--;
                continue;
            }
            if (Double.isInfinite(fitness)) {
                i--;
                continue;
            }
            atmp.setFitness(fitness);
            if (!repeatedSolution(atmp, comparador, harmonyMemory)) {
                harmonyMemory.add(atmp);
            }
        }
        if (harmonyMemory.isEmpty()) {
            System.out.println("Aqui");
        }
        Collections.sort(harmonyMemory, comparador);
        return harmonyMemory;
    }

    public void optimizeMemory(int maxIt, double pKMeans, double p,
            Random rand, Dataset d, ObjectiveFunction f,
            Comparator comparador, List<Agent> harmonyMemory, Distance distance,
            KMeans kmeans)
            throws DistanceException {

        for (int i = 0; i < harmonyMemory.size(); i++) {
            if (rand.nextDouble() <= p) {
                Agent n = kmeans.process(harmonyMemory.get(i), d, distance, pKMeans, maxIt, f);
                n.setFitness(f.calculate(n, d, distance));
                harmonyMemory.set(i, n);
            }
        }
        Collections.sort(harmonyMemory, comparador);
    }

    public int chooseK(int maxK, double hmcr,
            double par, Random random, List<Agent> harmonyMemory) {
        double num = random.nextDouble();
        int ret;
        int hms = harmonyMemory.size();

        if (num <= hmcr) {
            int pos = random.nextInt(hms);
            ret = harmonyMemory.get(pos).getP().getK();

            if (random.nextDouble() < par) {
                ret = harmonyMemory.get(0).getP().getK();
            }
        } else {
            ret = random.nextInt(maxK);
            ret = ret < 2 ? 2 : ret;
        }

        return ret;
    }

    /**
     * Verifica si existe una solución igual en la memoria
     *
     * @param newSolution
     * @param comparadorAgents
     * @param harmonyMemory
     * @return true si la solución ya está en la memoria.
     */
    public boolean repeatedSolution(Agent newSolution,
            Comparator comparadorAgents, List<Agent> harmonyMemory) {

        int hms = harmonyMemory.size();
        for (int i = 0; i < hms; i++) {
            if (comparadorAgents.compare(harmonyMemory.get(i), newSolution) == 0) {
                if (harmonyMemory.get(i).getP().equals(newSolution.getP())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean replaceSolution(List<Agent> ma, Agent ns, AgentComparator c) {
        int hms = ma.size();
        if (c.compare(ns, ma.get(hms - 1)) == -1) {
            ma.set(hms - 1, ns);
            return true;
        }
        return false;
    }

    public boolean uniformMemory(List<Agent> harmonyMemory) {
        int hms = harmonyMemory.size();
        double mean = 0;
        double deviation = 0;
        for (Agent agent : harmonyMemory) {
            mean += agent.getFitness();
        }
        mean /= hms;
        for (Agent agent : harmonyMemory) {
            deviation += Math.pow(agent.getFitness() - mean, 2);
        }
        deviation /= (hms - 1);
        deviation = Math.sqrt(deviation);
        return deviation <= 0.05 * mean;
    }

    public List<Agent> regenerateMemory(List<Agent> agentes, int maxK, int maxIt,
            double po, double pKMeans,
            Dataset dataset, ObjectiveFunction f, AgentComparator comparator,
            Random random, Distance distance, String initialization, boolean fixedK) throws DistanceException, Exception {
        int hms = agentes.size();
        BasicKMeansImpl kmeans = new BasicKMeansImpl();

        List<Agent> ret = generateHarmonyMemory(hms - 2, maxK, dataset, f,
                comparator, random, distance, initialization, fixedK);

        ret.add(agentes.get(0));
        ret.add(agentes.get(1));

        Collections.sort(ret, comparator);
        return ret;
    }

    /**
     * Verifica si una solución es rentable.
     *
     * @param agent
     * @return
     */
    public boolean testSolution(Agent agent) {

        if (agent.getFitness() == Double.NEGATIVE_INFINITY || agent.getFitness() == Double.POSITIVE_INFINITY) {
            return false;
        }

        if (agent.getP().getK() < 2) {
            return false;
        }

        Cluster[] clusters = agent.getClusters();
        for (Cluster cluster : clusters) {
            if (cluster.getRecords().length == 1) {
                return false;
            }
        }

        return true;
    }
}
