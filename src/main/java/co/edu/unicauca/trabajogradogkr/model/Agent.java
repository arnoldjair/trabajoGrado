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
import co.edu.unicauca.trabajogradogkr.model.RGS.Partition;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class Agent {

    private Partition p;
    private double fitness;
    private Cluster[] clusters;
    private int minimize;

    public Partition getP() {
        return p;
    }

    public void setP(Partition p) {
        this.p = p;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public Cluster[] getClusters() {
        return clusters;
    }

    public void setClusters(Cluster[] clusters) {
        this.clusters = clusters;
    }

    public int getMinimize() {
        return minimize;
    }

    public void setMinimize(int minimize) {
        this.minimize = minimize;
    }

    public synchronized void calcClusters(Dataset dataset) {
        int[] rgs = p.getRgs();
        int k = p.getK();
        clusters = new Cluster[k];

        List<Record>[] l = new ArrayList[k];
        //TODO: Revisar si es necesario hacer esto.
        for (int i = 0; i < k; i++) {
            l[i] = new ArrayList<>();
        }

        for (int i = 0; i < dataset.getN(); i++) {
            l[rgs[i]].add(dataset.getRecord(i));
        }

        for (int i = 0; i < k; i++) {
            Cluster tmp = new Cluster();
            tmp.setAttributes(dataset.getAttributes());
            Record[] record = new Record[l[i].size()];
            for (int j = 0; j < l[i].size(); j++) {
                record[j] = l[i].get(j);
            }
            tmp.setRecords(record);
            tmp.calcCentroid();
            tmp.setIndex(i);
            clusters[i] = tmp;
        }
    }

    public synchronized void reallocateRecords(Dataset dataset, Distance distance) {
        int n = dataset.getN();
        int k = clusters.length;
        int index;
        int[] rgs = new int[n];
        double dist;
        double curDistance;

        for (int i = 0; i < n; i++) {
            Record record = dataset.getRecord(i);
            dist = distance.distance(clusters[0].getCentroid(), record);
            index = 0;
            for (int j = 1; j < k; j++) {
                curDistance = distance.distance(clusters[j].getCentroid(), record);
                if (curDistance < dist) {
                    dist = curDistance;
                    index = j;
                }
            }
            rgs[i] = index;
        }
        p = Partition.reprocessRGS(rgs);
    }

}
