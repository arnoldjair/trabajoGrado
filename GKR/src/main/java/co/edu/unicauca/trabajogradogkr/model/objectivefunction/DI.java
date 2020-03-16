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
package co.edu.unicauca.trabajogradogkr.model.objectivefunction;

import co.edu.unicauca.trabajogradogkr.model.Agent;
import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.model.distance.Distance;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class DI implements ObjectiveFunction {

    @Override
    public boolean minimizes() {
        return false;
    }

    @Override
    public double calculate(Agent agent, Dataset dataset, Distance distance) {
        agent.calcClusters(dataset);
        double currDist;
        double minDist = Double.POSITIVE_INFINITY;
        double maxDist = 0;
        int[] rgs = agent.getP().getRgs();

        for (int i = 0; i < dataset.getN(); i++) {
            for (int j = 0; j < dataset.getN(); j++) {
                currDist = distance.distance(dataset.getRecord(i), dataset.getRecord(j));
                if (rgs[i] != rgs[j]) {
                    minDist = Math.min(currDist, minDist);
                } else {
                    maxDist = Math.max(currDist, maxDist);
                }
            }
        }

        double ret = minDist / maxDist;
        return ret;
    }

    @Override
    public ObjectiveFunction newInstance() {
        return new DI();
    }

    @Override
    public String toString() {
        return "DI";
    }

}
