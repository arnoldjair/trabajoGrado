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

import co.edu.unicauca.trabajogradogkr.service.interfaces.DatasetService;
import java.util.Random;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class Cluster {

    private int index;
    private Record centroid;
    private Record standarDeviation;
    private Record[] records;
    private Attribute[] attributes;
    private double prior;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Record getCentroid() {
        return centroid;
    }

    public void setCentroid(Record centroid) {
        this.centroid = centroid;
    }

    public Record[] getRecords() {
        return records.clone();
    }

    public void setRecords(Record[] records) {
        this.records = records.clone();
    }

    public Attribute[] getAttributes() {
        return attributes;
    }

    public void setAttributes(Attribute[] attributes) {
        this.attributes = attributes;
    }

    public Record getStandarDeviation() {
        return standarDeviation;
    }

    public void setStandarDeviation(Record standarDeviation) {
        this.standarDeviation = standarDeviation;
    }

    public double getPrior() {
        return prior;
    }

    public void setPrior(double prior) {
        this.prior = prior;
    }

    public void calcCentroid() {
        centroid = new Record();
        Object[] centroidData = new Object[attributes.length];

        for (int i = 0; i < attributes.length; i++) {
            centroidData[i] = 0.0;
        }

        for (Record record : records) {
            Object[] tmp = record.getData();
            for (int i = 0; i < attributes.length; i++) {
                if (attributes[i].getType() == DatasetService.DOUBLE) {
                    centroidData[i] = (double) centroidData[i] + (double) tmp[i];
                }
            }
        }
        for (int i = 0; i < this.attributes.length; i++) {
            centroidData[i] = (Double) centroidData[i] / records.length;
        }

        centroid.setData(centroidData);
        centroid.setAttributes(attributes);
    }

    public void calcDeviation() {

        Object[] tmp = new Object[this.attributes.length];
        this.standarDeviation = new Record(-1, tmp, this.attributes);

        for (int i = 0; i < this.attributes.length; i++) {
            tmp[i] = 0.0;
        }

        for (int i = 0; i < this.records.length; i++) {
            Record curr = this.records[i].clone();
            curr = curr.subtract(this.centroid);
            curr = curr.pow(2);
            curr = curr.multiply(1.0 / this.records.length);
            standarDeviation = standarDeviation.add(curr);
        }
        standarDeviation.pow(0.5);
    }

    public void calcPrior(int n) {
        prior = (this.records.length * 1.0) / n;
    }

    public static Cluster randCluster(Dataset dataset, Random random) {
        Cluster ret = new Cluster();
        ret.setAttributes(dataset.getAttributes());
        int m = dataset.getAttributes().length;
        Object[] tmp = new Object[m];

        for (int i = 0; i < m; i++) {
            if (dataset.getAttributes()[i].getType() == DatasetService.DOUBLE) {
                tmp[i] = random.nextDouble();
            } else {
                tmp[i] = 0;
            }
        }

        Record centroid = new Record(-1, tmp, dataset.getAttributes());
        ret.setCentroid(centroid);
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < centroid.getData().length; i++) {
            sb.append(centroid.getData()[i]);
            if (i != centroid.getData().length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

}
