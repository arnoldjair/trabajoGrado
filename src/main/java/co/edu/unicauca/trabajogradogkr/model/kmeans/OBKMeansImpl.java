/*
 * Copyright (C) 2016 Pivotal Software, Inc..
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
package co.edu.unicauca.trabajogradogkr.model.kmeans;

import co.edu.unicauca.trabajogradogkr.model.Agent;
import co.edu.unicauca.trabajogradogkr.model.AgentComparator;
import co.edu.unicauca.trabajogradogkr.model.Cluster;
import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.model.Record;
import co.edu.unicauca.trabajogradogkr.model.distance.Distance;
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.ObjectiveFunction;
import co.edu.unicauca.trabajogradogkr.model.rgs.Partition;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class OBKMeansImpl implements KMeans {

    @Override
    public Agent process(Agent agent, Dataset dataset, Distance distance,
            double percentageStop, int maxIt, ObjectiveFunction objectiveFunction) {
        try {
            int n = dataset.getN();
            int currIt = 0;
            double reallocated;
            double prevReallocated = n;
            int k;
            int index;
            int[] rgs;
            Cluster[] clusters;
            //TODO: Verificar primero si no se ha calculado
            agent.setFitness(objectiveFunction.calculate(agent, dataset, distance));
            Agent ret = agent.clone();
            ret.calcClusters(dataset);
            AgentComparator comparator = new AgentComparator(objectiveFunction.minimizes());
            Agent old;

            do {
                old = ret.clone();
                old.calcClusters(dataset);
                reallocated = 0;
                rgs = ret.getP().getRgs().clone();
                clusters = ret.getClusters();
                k = ret.getP().getK();
                double dist;
                for (int i = 0; i < n; i++) {
                    Record record = dataset.getRecord(i);
                    index = rgs[i];
                    dist = distance.distance(clusters[index].getCentroid(), record);
                    for (int j = 0; j < k; j++) {
                        double currDist = distance.distance(clusters[j].getCentroid(), record);
                        if (currDist < dist) {
                            index = j;
                            dist = currDist;
                        }
                    }
                    if (index != rgs[i]) {
                        reallocated++;
                        rgs[i] = index;
                        /**
                         * MacQueen
                         */
                        //Partition tmp = Partition.reprocessRGS(rgs);
                        //rgs = tmp.getRgs();
                    }
                }

                ret.setP(Partition.reprocessRGS(rgs));
                ret.calcClusters(dataset);
                ret.setFitness(objectiveFunction.calculate(ret, dataset, distance));
                currIt++;

                /**
                 * Se repite mientras no se mejore el fitness
                 */
            } while (comparator.compare(ret, old) < 0 && currIt < maxIt);

            return ret;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(OBKMeansImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
