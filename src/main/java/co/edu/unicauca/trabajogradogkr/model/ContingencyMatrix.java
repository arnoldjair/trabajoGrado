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

import co.edu.unicauca.trabajogradogkr.exception.DatasetException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class ContingencyMatrix {

    private int[][] matrix;
    private int numClusters;
    private int numClasses;
    private int n;
    private int current[];
    private int best[];
    private int[] ni;
    private double[] pi;
    private int[] nj;
    private double[] pj;
    private double[] maxPijOpi;
    private String[] classes;
    private Map<String, Integer> mapClasses;

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int getNumClusters() {
        return numClusters;
    }

    public void setNumClusters(int numClusters) {
        this.numClusters = numClusters;
    }

    public int getNumClasses() {
        return numClasses;
    }

    public void setNumClasses(int numClasses) {
        this.numClasses = numClasses;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int[] getCurrent() {
        return current;
    }

    public void setCurrent(int[] current) {
        this.current = current;
    }

    public int[] getBest() {
        return best;
    }

    public void setBest(int[] best) {
        this.best = best;
    }

    public int[] getNi() {
        return ni;
    }

    public void setNi(int[] ni) {
        this.ni = ni;
    }

    public double[] getPi() {
        return pi;
    }

    public double getPi(int i) {
        return (double) this.ni[i] / this.n;
    }

    public void setPi(double[] pi) {
        this.pi = pi;
    }

    public int[] getNj() {
        return nj;
    }

    public void setNj(int[] nj) {
        this.nj = nj;
    }

    public double[] getPj() {
        return pj;
    }

    public void setPj(double[] pj) {
        this.pj = pj;
    }

    public double[] getMaxPijOpi() {
        return maxPijOpi;
    }

    public void setMaxPijOpi(double[] maxPijOpi) {
        this.maxPijOpi = maxPijOpi;
    }

    public String[] getClasses() {
        return classes;
    }

    public void setClasses(String[] classes) {
        this.classes = classes;
    }

    public Map<String, Integer> getMapClasses() {
        return mapClasses;
    }

    public void setMapClasses(Map<String, Integer> mapClasses) {
        this.mapClasses = mapClasses;
    }

    public int getNi(int level) {
        return ni[level];
    }

    private void initVars(Agent agent, Dataset dataset) throws DatasetException {
        if (!dataset.isHasClass()) {
            throw new DatasetException("El dataset no tiene definida la clase");
        }
        int rows = agent.getP().getK();
        int columns = dataset.getClasses().length;
        numClusters = rows;
        numClasses = columns;
        n = dataset.getN();
        best = new int[numClusters + 1];
        current = new int[numClusters];
        best[numClusters] = Integer.MAX_VALUE;
        ni = new int[rows];
        pi = new double[rows];
        nj = new int[columns];
        pj = new double[columns];
        maxPijOpi = new double[rows];
        matrix = new int[rows][columns];
        if (dataset.isHasClass()) {
            classes = dataset.getClasses();
            mapClasses = new LinkedHashMap<>();
        }
    }

    public ContingencyMatrix(Agent P, Dataset d) {
        try {
            initVars(P, d);

            int classIndex = d.getClassIndex();
            String cClass;
            Record record;
            int[] rgs = P.getP().getRgs();
            Map<String, Integer> classMap = new LinkedHashMap<>();

            classes = d.getClasses();
            for (int i = 0; i < classes.length; i++) {
                classMap.put(classes[i], i);
            }
            for (int i = 0; i < rgs.length; i++) {
                record = d.getRecord(i);
                cClass = (String) record.getData()[classIndex];
                matrix[rgs[i]][classMap.get(cClass)]++;
                ni[rgs[i]]++;
                nj[classMap.get(cClass)]++;
            }

            for (int i = 0; i < numClusters; i++) {
                pi[i] = (double) ni[i] / n;
            }

            for (int j = 0; j < numClasses; j++) {
                pj[j] = (double) nj[j] / n;
            }

            for (int i = 0; i < numClusters; i++) {
                maxPijOpi[i] = 0;
                for (int j = 0; j < numClasses; j++) {
                    if (maxPijOpi[i] < ((double) matrix[i][j] / ni[i])) {
                        maxPijOpi[i] = ((double) matrix[i][j] / ni[i]);
                    }
                }

            }
            mapClasses(0, 0);
            map();

        } catch (DatasetException ex) {
            Logger.getLogger(ContingencyMatrix.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void mapClasses(int level, int error) {
        boolean ok;

        if (level == numClusters) {
            if (error < best[numClusters]) {
                best[numClusters] = error;
                for (int i = 0; i < numClusters; i++) {
                    best[i] = current[i];
                }
            }
        } else if (getNi(level) == 0) {
            //TODO: Esta condición no debería darse, puesto que en k-means no quedan clusters vacíos.
            current[level] = -1;
            mapClasses(level + 1, error);
        } else {

            current[level] = -1;
            mapClasses(level + 1, error + getNi(level));

            for (int i = 0; i < numClasses; i++) {
                if (matrix[level][i] > 0) {
                    ok = true;
                    for (int j = 0; j < level; j++) {
                        if (current[j] == i) {
                            ok = false;
                            break;
                        }
                    }
                    if (ok) {
                        current[level] = i;
                        mapClasses(level + 1, (error + (getNi(level) - matrix[level][i])));
                    }
                }
            }
        }
    }

    private void map() {
        for (int i = 0; i < this.numClusters; i++) {
            if (best[i] == -1) {
                continue;
            }
            this.mapClasses.put(this.classes[best[i]], i);
        }
    }
}
