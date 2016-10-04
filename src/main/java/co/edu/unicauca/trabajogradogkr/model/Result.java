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

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class Result implements Comparable<Result> {

    private String id;
    private Agent[] agents;
    private ContingencyMatrix[] contingencyMatrix;
    private int[] icc;
    private int[] iic;
    private double[] er;
    private double[] fm;
    private double[] fm1;
    private int numExperiments;
    private double averageIcc;
    private double averageIic;
    private double averageEr;
    private double standardDeviation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Agent[] getAgents() {
        return agents;
    }

    public void setAgents(Agent[] agents) {
        this.agents = agents;
    }

    public ContingencyMatrix[] getContingencyMatrix() {
        return contingencyMatrix;
    }

    public void setContingencyMatrix(ContingencyMatrix[] contingencyMatrix) {
        this.contingencyMatrix = contingencyMatrix;
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

    public double[] getFm() {
        return fm;
    }

    public void setFm(double[] fm) {
        this.fm = fm;
    }

    public double[] getFm1() {
        return fm1;
    }

    public void setFm1(double[] fm1) {
        this.fm1 = fm1;
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

    public String summary() {
        StringBuilder sb = new StringBuilder();
        sb.append(id)
                .append("\t").append(averageIcc)
                .append("\t").append(averageIic)
                .append("\t").append(averageEr)
                .append("\t").append(standardDeviation)
                .append("\n");
        return sb.toString();
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

    @Entity
    public static class ResultDTO {

        @Id
        private int id;
        
    }
}
