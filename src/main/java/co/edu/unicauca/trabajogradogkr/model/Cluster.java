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
public class Cluster {

    private int index;
    private Record centroid;
    private Record[] records;
    private Attribute[] attributes;

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

    public void calcCentroid() {
        centroid = new Record();
        Object[] centroidData = new Object[attributes.length];

        for (int i = 0; i < attributes.length; i++) {
            centroidData[i] = 0.0;
        }

        for (Record record : records) {
            Object[] tmp = record.getData();
            for (int i = 0; i < attributes.length; i++) {
                if (attributes[i].getType() == Dataset.DOUBLE) {
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

}
