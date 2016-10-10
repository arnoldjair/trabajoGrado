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

import co.edu.unicauca.trabajogradogkr.model.distance.Distance;
import co.edu.unicauca.trabajogradogkr.model.Agent;
import co.edu.unicauca.trabajogradogkr.model.Cluster;
import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.model.Record;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class BIC implements ObjectiveFunction {

    @Override
    public boolean minimizes() {
        return true;
    }

    @Override
    public double calculate(Agent agent, Dataset dataset, Distance distance) {

        /**
         * Deviation, Prior, LogLikelihood
         */
        agent.calcClusters(dataset);
        Cluster[] clusters = agent.getClusters();
        for (Cluster cluster : clusters) {
            cluster.calcDeviation();
            cluster.calcPrior(dataset.getN());
        }

        double temp1 = 0;
        double ll = 0;
        double ln2 = Math.log(2);
        int n = dataset.getN();
        int k = agent.getClusters().length;

        for (int i = 0; i < n; i++) {
            temp1 = 0;
            for (int j = 0; j < k; j++) {
                Record curr = dataset.getRecord(i);
                double normal = curr.normal(clusters[j].getCentroid(), clusters[j].getStandarDeviation());
                temp1 += normal * clusters[j].getPrior();
            }
            double l = Math.log(temp1);
            ll += l / ln2;
        }

        int N = dataset.getN();
        int K = clusters.length;
        double NPK = Math.pow(N, K);
        double LL2 = Math.pow(ll, 2);
        double t2 = Math.log(NPK / LL2);
        double ret = (1.0 / N) * t2;
        return ret;
    }

    @Override
    public ObjectiveFunction newInstance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return "BIC";
    }

}
