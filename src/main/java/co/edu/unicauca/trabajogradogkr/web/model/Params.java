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
package co.edu.unicauca.trabajogradogkr.web.model;

import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
@Entity
@Table(name = "params")
public class Params {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String dataset;
    @ElementCollection
    private List<String> algorithms;
    @ElementCollection
    private List<String> objectiveFunctions;
    private int it;
    private double minPar;
    private double maxPar;
    private double hmcr;
    private int hms;
    private double op;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public List<String> getAlgorithms() {
        return algorithms;
    }

    public void setAlgorithms(List<String> algorithms) {
        this.algorithms = algorithms;
    }

    public List<String> getObjectiveFunctions() {
        return objectiveFunctions;
    }

    public void setObjectiveFunctions(List<String> objectiveFunctions) {
        this.objectiveFunctions = objectiveFunctions;
    }

    public int getIt() {
        return it;
    }

    public void setIt(int it) {
        this.it = it;
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

    public int getHms() {
        return hms;
    }

    public void setHms(int hms) {
        this.hms = hms;
    }

    public double getOp() {
        return op;
    }

    public void setOp(double op) {
        this.op = op;
    }

}
