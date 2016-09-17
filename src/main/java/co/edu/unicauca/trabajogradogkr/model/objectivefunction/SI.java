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
import co.edu.unicauca.trabajogradogkr.model.Cluster;
import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.model.Record;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class SI implements ObjectiveFunction {

    @Override
    public boolean minimizes() {
        return false;
    }

    @Override
    public double calculate(Agent agent, Dataset dataset, Distance distance) {
        double ret = 0;
        //Por sospecha
        agent.calcClusters(dataset);
        Cluster[] clusters = agent.getClusters();
        Cluster[] otherClusters;
        int N = dataset.getN();
        int K = clusters.length;
        int[] rgs = agent.getP().getRgs();
        int cont;
        double[] ai = new double[N];
        double[] bi = new double[N];
        double[] si = new double[N];
        double[] sk = new double[K];

        for (int i = 0; i < N; i++) {
            Record record = dataset.getRecord(i);
            otherClusters = new Cluster[K - 1];
            cont = 0;
            for (int j = 0; j < K; j++) {
                if (j != rgs[i]) {
                    otherClusters[cont] = clusters[j];
                    cont++;
                }
            }
            ai[i] = this.a(dataset.getRecord(i), clusters[rgs[i]], distance);
            bi[i] = this.b(dataset.getRecord(i), otherClusters, distance);
            si[i] = (bi[i] - ai[i]) / Math.max(ai[i], bi[i]);
        }

        for (int i = 0; i < N; i++) {
            sk[rgs[i]] += si[i];
        }

        for (int i = 0; i < K; i++) {
            sk[i] = sk[i] / clusters[i].getRecords().length;
            ret += sk[i];
        }

        ret /= K;
        return ret;
    }

    private double a(Record record, Cluster cluster, Distance distance) {
        double ret = 0;

        Record[] records = cluster.getRecords();
        int nk = records.length;

        if (nk - 1 == 0) {
            return Double.POSITIVE_INFINITY;
        }

        for (int i = 0; i < records.length; i++) {
            ret = distance.distance(record, records[i]);
        }

        ret /= (nk - 1);

        return ret;
    }

    private double b(Record record, Cluster[] clusters, Distance distance) {
        double ret = Double.POSITIVE_INFINITY;
        double da;
        int k = clusters.length;
        int nkp;
        Record[] records;

        for (int i = 0; i < k; i++) {
            records = clusters[i].getRecords();
            nkp = records.length;
            da = 0;
            for (Record currRecord : records) {
                da += distance.distance(record, currRecord);
            }
            da /= nkp;
            if (da < ret) {
                ret = da;
            }
        }

        return ret;
    }

    @Override
    public ObjectiveFunction newInstance() {
        return new SI();
    }

    @Override
    public String toString() {
        return "SI";
    }

}
