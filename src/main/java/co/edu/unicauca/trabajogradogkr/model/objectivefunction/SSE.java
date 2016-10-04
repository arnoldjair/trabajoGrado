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
package co.edu.unicauca.trabajogradogkr.model.objectivefunction;

import co.edu.unicauca.trabajogradogkr.distance.Distance;
import co.edu.unicauca.trabajogradogkr.distance.ManhattanDistance;
import co.edu.unicauca.trabajogradogkr.model.Agent;
import co.edu.unicauca.trabajogradogkr.model.Cluster;
import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.model.Record;
import co.edu.unicauca.trabajogradogkr.model.rgs.Partition;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class SSE implements ObjectiveFunction {

    @Override
    public boolean minimizes() {
        return true;
    }

    @Override
    public double calculate(Agent agent, Dataset dataset, Distance distance) {
        Partition p = agent.getP();
        double ret = 0;
        //Por sospecha
        agent.calcClusters(dataset);
        Cluster[] clusters = agent.getClusters();
        double dist;
        Distance localDistance = new ManhattanDistance();

        for (Cluster cluster : clusters) {
            Record[] records = cluster.getRecords();
            for (Record record : records) {
                //dist = localDistance.distance(cluster.getCentroid(), record);
                dist = distance.distance(cluster.getCentroid(), record);
                ret += Math.pow(dist, 2);
            }
        }

        return ret;
    }

    @Override
    public ObjectiveFunction newInstance() {
        return new SSE();
    }

    @Override
    public String toString() {
        return "SSE";
    }
}
