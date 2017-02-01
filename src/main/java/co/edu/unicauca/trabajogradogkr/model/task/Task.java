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
package co.edu.unicauca.trabajogradogkr.model.task;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class Task {

    private double minPar;
    private double maxPar;
    private double hmcr;
    private double po;
    private int hms;
    private int nExp;
    private int nIt;
    private int maxK;
    private int maxKMeans;
    private int threads;
    private long seed;
    private String dataset;
    private String objectiveFunction;
    private String distance;
    private String algorithm;
    private String kmeansAlgorithm;
    private String initialization;
    private boolean done;

    public Task(double minPar, double maxPar, double hmcr, double po, int hms,
            int nExp, int nIt, int maxK, int maxKMeans, int threads, long seed,
            String dataset, String objectiveFunction, String distance,
            String algorithm, boolean done, String kmeansAlgorithm,
            String initialization) {
        this.minPar = minPar;
        this.maxPar = maxPar;
        this.hmcr = hmcr;
        this.po = po;
        this.hms = hms;
        this.nExp = nExp;
        this.nIt = nIt;
        this.maxK = maxK;
        this.maxKMeans = maxKMeans;
        this.threads = threads;
        this.seed = seed;
        this.dataset = dataset;
        this.objectiveFunction = objectiveFunction;
        this.distance = distance;
        this.algorithm = algorithm;
        this.done = done;
        this.kmeansAlgorithm = kmeansAlgorithm;
        this.initialization = initialization;
    }

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

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
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

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getKmeansAlgorithm() {
        return kmeansAlgorithm;
    }

    public void setKmeansAlgorithm(String kmeansAlgorithm) {
        this.kmeansAlgorithm = kmeansAlgorithm;
    }

    public String getInitialization() {
        return initialization;
    }

    public void setInitialization(String initialization) {
        this.initialization = initialization;
    }

}
