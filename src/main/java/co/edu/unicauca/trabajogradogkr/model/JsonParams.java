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

import java.util.List;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class JsonParams {

    private double minPar;
    private double maxPar;
    private double hmcr;
    private double po;
    private double er;
    private int hms;
    private int nExp;
    private int nIt;
    private int maxK;
    private int maxKMeans;
    private int threads;
    private int tuneUpIt;
    private long seed;
    private String datasetsPath;
    private List<String> datasets;
    private List<String> objectiveFunctions;
    private List<String> distances;
    private List<String> algorithms;
    private String tuneUp;

    public double getMinPar() {
        return minPar;
    }

    public void setMinPar(double minPar) {
        this.minPar = minPar;
    }

    public double getMaxPar() {
        return maxPar;
    }

    public void setMaxPar(double maxPar) {
        this.maxPar = maxPar;
    }

    public double getHmcr() {
        return hmcr;
    }

    public void setHmcr(double hmcr) {
        this.hmcr = hmcr;
    }

    public double getPo() {
        return po;
    }

    public void setPo(double po) {
        this.po = po;
    }

    public double getEr() {
        return er;
    }

    public void setEr(double er) {
        this.er = er;
    }

    public int getHms() {
        return hms;
    }

    public void setHms(int hms) {
        this.hms = hms;
    }

    public int getnExp() {
        return nExp;
    }

    public void setnExp(int nExp) {
        this.nExp = nExp;
    }

    public int getnIt() {
        return nIt;
    }

    public void setnIt(int nIt) {
        this.nIt = nIt;
    }

    public int getMaxK() {
        return maxK;
    }

    public void setMaxK(int maxK) {
        this.maxK = maxK;
    }

    public int getMaxKMeans() {
        return maxKMeans;
    }

    public void setMaxKMeans(int maxKMeans) {
        this.maxKMeans = maxKMeans;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getTuneUpIt() {
        return tuneUpIt;
    }

    public void setTuneUpIt(int tuneUpIt) {
        this.tuneUpIt = tuneUpIt;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public String getDatasetsPath() {
        return datasetsPath;
    }

    public void setDatasetsPath(String datasetsPath) {
        this.datasetsPath = datasetsPath;
    }

    public List<String> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<String> datasets) {
        this.datasets = datasets;
    }

    public List<String> getObjectiveFunctions() {
        return objectiveFunctions;
    }

    public void setObjectiveFunctions(List<String> objectiveFunctions) {
        this.objectiveFunctions = objectiveFunctions;
    }

    public List<String> getDistances() {
        return distances;
    }

    public void setDistances(List<String> distances) {
        this.distances = distances;
    }

    public List<String> getAlgorithms() {
        return algorithms;
    }

    public void setAlgorithms(List<String> algorithms) {
        this.algorithms = algorithms;
    }

    public boolean verify() throws Exception {

        boolean ret = true;

        ret = ret && minPar >= 0;
        ret = ret && maxPar > 0;
        ret = ret && hmcr > 0;
        ret = ret && po >= 0;
        ret = ret && hms > 0;
        ret = ret && nExp > 0;
        ret = ret && nIt > 0;
        ret = ret && maxK > 0;
        ret = ret && maxKMeans > 0;
        ret = ret && threads > 0;
        ret = ret && datasets.size() > 0;
        ret = ret && objectiveFunctions.size() > 0;
        ret = ret && distances.size() > 0;
        ret = ret && algorithms.size() > 0;

        if (datasetsPath == null || datasetsPath.isEmpty()) {
            throw new Exception("Falta la ruta a los datasets");
        }

        return ret;
    }

    public String getTuneUp() {
        return tuneUp;
    }

    public void setTuneUp(String tuneUp) {
        this.tuneUp = tuneUp;
    }

}
