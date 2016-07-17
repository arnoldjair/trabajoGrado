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
package co.edu.unicauca.trabajogradogkr.model.RGS;

import java.security.SecureRandom;
import java.util.Arrays;
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

    public synchronized static Partition randPartition(int n, int k) {
        Random random = new SecureRandom();
        return randPartition(n, k, random);
    }

    public synchronized static Partition randPartition(int n, int k, Random random) {
        int[] rgs = new int[n];
        int max = k + 1;
        int ak = 0;
        Partition ret = null;
        while (ak != k) {
            max = 2;
            for (int i = 0; i < n; i++) {
                rgs[i] = random.nextInt(max);
            }
            ret = reprocessRGS(rgs);
            ak = ret.getK();
        }
        return ret;
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
        k = rgs[0];
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
