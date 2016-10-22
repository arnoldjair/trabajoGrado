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
public class Result implements Comparable<Result> {

    //private Agent[] agents;
    private ContingencyMatrix[] contingencyMatrices;
    private int[] icc;
    private int[] iic;
    private double[] er;
    private int numExperiments;
    private double averageIcc;
    private double averageIic;
    private double averageEr;
    private double standardDeviation;
    private String dataset;
    private String objectiveFunction;
    private String distance;
    private String algorithm;

    /*public Agent[] getAgents() {
        return agents;
    }

    public void setAgents(Agent[] agents) {
        this.agents = agents;
     */
    public ContingencyMatrix[] getContingencyMatrices() {
        return contingencyMatrices;
    }

    public void setContingencyMatrices(ContingencyMatrix[] contingencyMatrices) {
        this.contingencyMatrices = contingencyMatrices;
    }

    public int[] getIcc() {
        return icc;
    }

    public void setIcc(int[] icc) {
        this.icc = icc;
    }

    public int[] getIic() {
        return iic;
    }

    public void setIic(int[] iic) {
        this.iic = iic;
    }

    public double[] getEr() {
        return er;
    }

    public void setEr(double[] er) {
        this.er = er;
    }

    public int getNumExperiments() {
        return numExperiments;
    }

    public void setNumExperiments(int numExperiments) {
        this.numExperiments = numExperiments;
    }

    public double getAverageIcc() {
        return averageIcc;
    }

    public void setAverageIcc(double averageIcc) {
        this.averageIcc = averageIcc;
    }

    public double getAverageIic() {
        return averageIic;
    }

    public void setAverageIic(double averageIic) {
        this.averageIic = averageIic;
    }

    public double getAverageEr() {
        return averageEr;
    }

    public void setAverageEr(double averageEr) {
        this.averageEr = averageEr;
    }

    public void calcAverages() {
        averageIcc = 0;
        averageIic = 0;
        averageEr = 0;
        standardDeviation = 0;
        for (int i = 0; i < numExperiments; i++) {
            averageIcc += (double) icc[i];
            averageIic += iic[i];
            averageEr += (double) er[i];
        }

        averageIcc /= numExperiments;
        averageIic /= numExperiments;
        averageEr /= numExperiments;

        for (int i = 0; i < numExperiments; i++) {
            standardDeviation += Math.pow((double) er[i] - averageEr, 2);
        }

        standardDeviation /= numExperiments;
        standardDeviation = Math.sqrt(standardDeviation);
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getObjectiveFunction() {
        return objectiveFunction;
    }

    public void setObjectiveFunction(String objectiveFunction) {
        this.objectiveFunction = objectiveFunction;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public int compareTo(Result o) {
        if (this.averageEr < o.getAverageEr()) {
            return -1;
        }

        if (this.averageEr == o.getAverageEr()) {
            return 0;
        }

        return 1;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(averageIcc).append("\t");
        ret.append(averageIic).append("\t");
        ret.append(averageEr).append("\t");
        ret.append(standardDeviation).append("\t");
        ret.append(dataset).append("\t");
        ret.append(objectiveFunction).append("\t");
        ret.append(distance).append("\t");
        ret.append(algorithm).append("\n");

        return ret.toString();
    }
}
