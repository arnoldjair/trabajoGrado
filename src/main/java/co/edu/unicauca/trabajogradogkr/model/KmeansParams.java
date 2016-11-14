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
package co.edu.unicauca.trabajogradogkr.model;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class KmeansParams {

    private int k;
    private double percentageStop;
    private int maxIt;
    private int nExp;
    private String dataset;
    private String distance;
    private String objectiveFunction;
    private String algorithm;

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public double getPercentageStop() {
        return percentageStop;
    }

    public void setPercentageStop(double percentageStop) {
        this.percentageStop = percentageStop;
    }

    public int getMaxIt() {
        return maxIt;
    }

    public void setMaxIt(int maxIt) {
        this.maxIt = maxIt;
    }

    public int getnExp() {
        return nExp;
    }

    public void setnExp(int nExp) {
        this.nExp = nExp;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getObjectiveFunction() {
        return objectiveFunction;
    }

    public void setObjectiveFunction(String objectiveFunction) {
        this.objectiveFunction = objectiveFunction;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dataset).append("\t")
                .append(distance).append("\t")
                .append(objectiveFunction).append("\t")
                .append(algorithm).append("\t")
                .append(maxIt).append("\t")
                .append(nExp).append("\t");

        return sb.toString();
    }

}
