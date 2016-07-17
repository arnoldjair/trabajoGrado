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
public class ECVM {

    private double entropy;
    private double purity;
    private double fMeasure;
    private int icc;
    private ContingencyMatrix m;
    private double ER;

    public ECVM(ContingencyMatrix m) {
        this.m = m;
        cEntropy();
        cPurity();
        cFMeasure();
        cICC();
        cER();
    }

    public double getEntropy() {
        return entropy;
    }

    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }

    public double getPurity() {
        return purity;
    }

    public void setPurity(double purity) {
        this.purity = purity;
    }

    public double getfMeasure() {
        return fMeasure;
    }

    public void setfMeasure(double fMeasure) {
        this.fMeasure = fMeasure;
    }

    public int getIcc() {
        return icc;
    }

    public void setIcc(int icc) {
        this.icc = icc;
    }

    public ContingencyMatrix getM() {
        return m;
    }

    public void setM(ContingencyMatrix m) {
        this.m = m;
    }

    public double getER() {
        return ER;
    }

    public void setER(double ER) {
        this.ER = ER;
    }

    private void cEntropy() {
        double t1 = 0;
        double t2;
        double pij;
        double pi;
        int n = m.getN();
        int filas = m.getNumClusters();
        int columnas = m.getNumClasses();

        for (int i = 0; i < filas; i++) {
            pi = m.getPi(i);
            t2 = 0;
            for (int j = 0; j < columnas; j++) {
                pij = (double) m.getMatrix()[i][j] / n;
                if (pij == 0) {
                    continue;
                }
                t2 += (pij / pi) * Math.log(pij / pi);
            }
            t1 += pi * t2;
        }

        this.entropy = -t1;
    }

    private void cPurity() {
        purity = 0;
        int filas = m.getNumClusters();
        double pi;
        double maxPijOPi;

        for (int i = 0; i < filas; i++) {
            pi = m.getPi(i);
            maxPijOPi = m.getMaxPijOpi()[i];
            purity += (pi * maxPijOPi);
        }
    }

    private void cFMeasure() {
        this.fMeasure = 0;
        double pij;
        double pi;
        double pj;
        double t1;
        double t2;
        int filas = m.getNumClusters();
        int columnas = m.getNumClasses();
        int n = m.getN();

        for (int j = 0; j < columnas; j++) {
            pj = m.getPj()[j];
            t1 = 0;
            for (int i = 0; i < filas; i++) {
                pij = (double) m.getMatrix()[i][j] / n;
                pi = m.getPi()[i];
                t2 = 2 * ((pij / pi) * (pij / pj)) / ((pij / pi) + (pij / pj));
                if (t2 > t1) {
                    t1 = t2;
                }
            }
            fMeasure += pj * t1;
        }

    }

    private void cICC() {
        icc = 0;
        for (int i = 0; i < m.getNumClusters(); i++) {
            if (m.getBest()[i] == -1) {
                continue;
            }
            this.icc += m.getMatrix()[i][m.getBest()[i]];
        }
    }

    private void cER() {
        int iic = m.getN() - icc;
        ER = ((double) iic / m.getN()) * 100;
    }

}
