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
import co.edu.unicauca.trabajogradogkr.model.Agent;
import co.edu.unicauca.trabajogradogkr.model.Dataset;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class CHI implements ObjectiveFunction {

    @Override
    public boolean minimizes() {
        return false;
    }

    @Override
    public double calculate(Agent agent, Dataset dataset, Distance distance) {
        InternalClusteringCriteria i = new InternalClusteringCriteria();

        try {
            double B = i.bgss(agent, dataset, distance);
            double K = agent.getP().getK();
            double W = i.wgss(agent, dataset, distance);
            double N = dataset.getN();

            if (K == 1) {
                return 0;
            }

            double ret = (B * (N - K)) / (W * (K - 1));

            return ret;

        } catch (Exception e) {
            return 0;
        }

    }

    @Override
    public ObjectiveFunction newInstance() {
        return new CHI();
    }

    @Override
    public String toString() {
        return "CHI";
    }

}
