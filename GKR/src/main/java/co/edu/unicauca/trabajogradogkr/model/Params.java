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

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class Params implements Comparable<Params> {

    public double minPar;
    public double maxPar;
    public double hmcr;
    public double po;
    public double ER;
    public int hms;
    public String algorithm;
    public String objectiveFunction;

    public Params() {
        minPar = 0;
        maxPar = 0;
        hmcr = 0;
        po = 0;
        hms = 1;
        ER = 100;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(algorithm).append("\t")
                .append(objectiveFunction).append("\t")
                .append(minPar).append("\t")
                .append(maxPar).append("\t")
                .append(hmcr).append("\t")
                .append(hms).append("\t")
                .append(po).append("\t")
                .append(ER).append("\n");
        return sb.toString();
    }

    @Override
    public int compareTo(Params o) {
        if (o.ER == this.ER) {
            return 0;
        }

        return o.ER > this.ER ? -1 : 1;
    }

}
