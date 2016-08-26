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

import co.edu.unicauca.trabajogradogkr.distance.Distance;
import co.edu.unicauca.trabajogradogkr.exception.DistanceException;
import co.edu.unicauca.trabajogradogkr.model.Agent;
import co.edu.unicauca.trabajogradogkr.model.AgentComparator;
import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.model.kmeans.KMeans;
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.ObjectiveFunction;
import co.edu.unicauca.trabajogradogkr.model.rgs.Partition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class GBHSUtils {

    public List<Agent> generateHarmonyMemory(int hms, int maxK, Dataset dataset,
            Random rand, ObjectiveFunction f, Comparator comparador, Random random,
            Distance distance) throws DistanceException {
        List<Agent> harmonyMemory = new ArrayList<>();
        for (int i = 0; i < hms; i++) {
            Partition tmp = Partition.randPartition(dataset.getN(), random.nextInt(maxK), random);
            Agent atmp = new Agent(tmp);
            atmp.calcClusters(dataset);
            double fitness = f.calculate(atmp, dataset, distance);
            atmp.setFitness(fitness);
            if (!repeatedSolution(atmp, comparador, harmonyMemory)) {
                harmonyMemory.add(atmp);
            }
        }
        Collections.sort(harmonyMemory, comparador);
        return harmonyMemory;
    }

    public void optimizeMemory(int maxIt, double pKMeans, double p,
            Random rand, Dataset d, ObjectiveFunction f,
            Comparator comparador, List<Agent> harmonyMemory, Distance distance)
            throws DistanceException {
        KMeans kmeans = new KMeans();

        for (int i = 0; i < harmonyMemory.size(); i++) {
            if (rand.nextDouble() <= p) {
                Agent n = kmeans.process(harmonyMemory.get(i), d, distance, pKMeans, maxIt);
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
        return deviation <= 0.05 * mean;
    }

    public void regenerateMemory(List<Agent> agentes, int maxK, int maxIt,
            double po, double pKMeans,
            Dataset dataset, ObjectiveFunction f, AgentComparator comparator,
            Random random, Distance distance) throws DistanceException {
        int hms = agentes.size();
        KMeans kmeans = new KMeans();

        for (int i = 2; i < hms; i++) {
            Partition tmp = Partition.randPartition(dataset.getN(), random.nextInt(maxK), random);
            Agent atmp = new Agent(tmp);
            if (random.nextDouble() <= po) {
                atmp = kmeans.process(atmp, dataset, distance, pKMeans, maxIt);
            }
            double fitness = f.calculate(atmp, dataset, distance);
            atmp.setFitness(fitness);
            atmp.calcClusters(dataset);
            agentes.set(i, atmp);
        }
        Collections.sort(agentes, comparator);
    }
}
