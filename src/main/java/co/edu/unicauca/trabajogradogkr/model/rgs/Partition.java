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
package co.edu.unicauca.trabajogradogkr.model.rgs;

import co.edu.unicauca.trabajogradogkr.model.Agent;
import co.edu.unicauca.trabajogradogkr.model.Cluster;
import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.model.Record;
import co.edu.unicauca.trabajogradogkr.model.distance.Distance;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class Partition {

    private int[] rgs;
    private int n;
    private int k;

    public Partition() {
    }

    public Partition(int[] rgs, int n, int k) {
        this.rgs = rgs;
        this.n = n;
        this.k = k;
    }

    public Partition(int n) {
        rgs = new int[n];
        this.n = n;
        for (int i = 0; i < n; i++) {
            rgs[i] = 0;
        }
    }

    public int[] getRgs() {
        return rgs.clone();
    }

    public void setRgs(int[] rgs) {
        this.rgs = rgs.clone();
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public void clear() {
        for (int i = 0; i < this.n; i++) {
            rgs[i] = -1;
        }
    }

    public void complete() {
        for (int i = 0; i < this.n; i++) {
            if (rgs[i] == -1) {
                rgs[i] = this.k;
            }
        }
    }

    public void complete(int n) {
        for (int i = 0; i < this.n; i++) {
            if (this.rgs[i] == -1) {
                this.rgs[i] = n;
            }
        }
    }

    /**
     * Crea una partición con n elementos y a lo mucho k grupos.
     *
     * @param n
     * @param k
     * @return
     */
    public synchronized static Partition randPartition(int n, int k) {
        Random random = new SecureRandom();
        return randPartition(n, k, random);
    }

    /**
     * Partición aleatoria.
     *
     * @param n
     * @param k
     * @param random
     * @return
     */
    public synchronized static Partition randPartition(int n, int k, Random random) {
        int[] rgs = new int[n];
        k = k < 2 ? 2 : k;
        int max = k;

        int ak = 0;
        Partition ret = null;
        while (ak != k) {
            for (int i = 0; i < n; i++) {
                rgs[i] = random.nextInt(max);
            }
            ret = reprocessRGS(rgs);
            ak = ret.getK();
        }
        return ret;
    }

    /**
     * Partición aleatoria utilizando el método de inicialización de kmeanspp
     *
     * @param k
     * @param dataset
     * @param distance
     * @param random
     * @return
     */
    public synchronized static Partition RandPartitionKmeanspp(int k, Dataset dataset,
            Distance distance, Random random) {
        int[] rgs = new int[dataset.getN()];
        int max = k;

        List<Record> centroids = new ArrayList<>();

        //Las distancia entre cada punto y su centroide mas cercano.
        double[] D2 = new double[dataset.getN()];
        double[] probs = new double[dataset.getN()];
        double acumD2;

        //Se selecciona el primer centroide de manera aleatoria.
        int pos = random.nextInt(dataset.getN());
        centroids.add(dataset.getRecord(pos));

        //Seleccionar los siguientes centroides.
        for (int i = 1; i < k; i++) {
            acumD2 = 0;
            for (int j = 0; j < dataset.getN(); j++) {
                D2[j] = closestClusterDistance(dataset.getRecord(j), centroids, distance, dataset);
                acumD2 += D2[j];
            }

            for (int j = 0; j < dataset.getN(); j++) {
                probs[j] = D2[j] / acumD2;
            }

            double tmp = random.nextDouble();
            double acumProbs = 0;

            for (int c = 0; c < dataset.getN(); c++) {
                acumProbs += probs[c];
                if (acumProbs >= tmp) {
                    centroids.add(dataset.getRecord(c));
                    break;
                }
            }
        }

        Agent agent = new Agent();
        Cluster[] clusters = new Cluster[centroids.size()];

        pos = 0;

        for (Record centroid : centroids) {
            Cluster tmpCluster = new Cluster();
            tmpCluster.setCentroid(centroid);
            clusters[pos] = tmpCluster;
            pos++;
        }

        agent.setClusters(clusters);
        agent.reallocateRecords(dataset, distance);

        return agent.getP();
    }

    public synchronized static double closestClusterDistance(Record record, List<Record> centroids, Distance distance, Dataset dataset) {
        double minDist = Double.POSITIVE_INFINITY;
        for (Record centroid : centroids) {
            double currDist = distance.distance(record, centroid);
            if (currDist < minDist) {
                minDist = currDist;
            }
        }
        return minDist;
    }

    public synchronized static Partition reprocessRGS(int[] rgs) {
        int k = rgs[0];

        for (int i = 1; i < rgs.length; i++) {
            if (k < rgs[i]) {
                k = rgs[i];
            }
        }
        k++;

        int[][] swap = new int[k][2];

        for (int i = 0; i < k; i++) {
            swap[i][0] = i;
            swap[i][1] = -1;
        }

        int tmp = rgs[0];
        swap[tmp][1] = 0;
        rgs[0] = 0;

        int max = 0;
        k = 0;
        for (int i = 1; i < rgs.length; i++) {
            if (swap[rgs[i]][1] != -1) {
                rgs[i] = swap[rgs[i]][1];
            } else {
                max++;
                swap[rgs[i]][1] = max;
                rgs[i] = max;
            }
            if (k < rgs[i]) {
                k = rgs[i];
            }
        }

        k++;

        Partition ret = new Partition();
        ret.setN(rgs.length);
        ret.setRgs(rgs);
        ret.setK(k);

        return ret;
    }

    public synchronized Partition mix(Partition p, Random random, int c) {
        int[] rgs1 = this.rgs.clone();
        int[] rgs2 = p.getRgs().clone();

        for (int i = 0; i < this.n; i++) {
            if (rgs1[i] == c) {
                if (rgs2[i] == -1) {
                    rgs2[i] = c;
                } else if (random.nextInt(2) == 0) {
                    rgs2[i] = c;
                }
            }
        }
        Partition ret = new Partition(rgs2, n, p.getK());
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("n: ").append(this.n).append(", ");
        sb.append("k: ").append(this.k).append(" ");

        for (int i = 0; i < n; i++) {
            sb.append("[");
            sb.append(Long.toString(this.rgs[i]));
            sb.append("]");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Partition other = (Partition) obj;
        return Arrays.equals(this.rgs, other.rgs);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Arrays.hashCode(this.rgs);
        hash = 97 * hash + this.n;
        hash = 97 * hash + this.k;
        return hash;
    }

}
