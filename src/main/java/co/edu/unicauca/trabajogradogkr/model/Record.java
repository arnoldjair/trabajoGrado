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
public class Record {

    private int index;
    private Object[] data;
    private Attribute[] attributes;

    public Record() {
    }

    public Record(int index, Object[] data, Attribute[] attributes) {
        this.index = index;
        this.data = data;
        this.attributes = attributes;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Object[] getData() {
        return data;
    }

    public void setData(Object[] data) {
        this.data = data;
    }

    public Attribute[] getAttributes() {
        return attributes;
    }

    public void setAttributes(Attribute[] attributes) {
        this.attributes = attributes;
    }

    public Record add(Record record) {
        Object[] data1 = data;
        Object[] data2 = record.getData();
        Object[] dataRet = new Object[data.length];
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].getType() == Dataset.DOUBLE) {
                dataRet[i] = (double) data1[i] + (double) data2[i];
            } else {
                dataRet[i] = 0;
            }
        }

        return new Record(-1, dataRet, attributes);
    }

    public Record subtract(Record record) {
        Object[] data1 = data;
        Object[] data2 = record.getData();
        Object[] dataRet = new Object[data.length];
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].getType() == Dataset.DOUBLE) {
                dataRet[i] = (double) data1[i] - (double) data2[i];
            } else {
                dataRet[i] = 0;
            }
        }

        return new Record(-1, dataRet, attributes);
    }

    public Record pow(double val) {
        Object[] dataRet = new Object[data.length];
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].getType() == Dataset.DOUBLE) {
                dataRet[i] = Math.pow((double) data[i], val);
            } else {
                dataRet[i] = 0;
            }
        }

        return new Record(-1, dataRet, attributes);
    }

    public Record divide(double val) {
        Object[] dataRet = new Object[data.length];
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].getType() == Dataset.DOUBLE) {
                dataRet[i] = (double) data[i] / val;
            } else {
                dataRet[i] = 0;
            }
        }

        return new Record(-1, dataRet, attributes);
    }

    public double sumValues() {
        double ret = 0;
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].getType() == Dataset.DOUBLE) {
                ret += (double) data[i];
            }
        }

        return ret;
    }
}
