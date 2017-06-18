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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class JsonParams {

    private Map<String, Object> map;

    public JsonParams() {
        this.map = new HashMap<>();
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Object getParam(String name) {
        if (this.map.containsKey(name)) {
            return this.map.get(name);
        }
        return null;
    }

    public void setParam(String name, Object val) {
        this.map.put(name, val);
    }

    public boolean verifyExperiment() throws Exception {

        boolean ret = true;
        String kmAlgo = (String) getParam("kmeansAlgorithm");
        String initialization = (String) getParam("initialization");

        ret = ret && (double) getParam("minPar") >= 0;
        ret = ret && (double) getParam("maxPar") > 0;
        ret = ret && (double) getParam("hmcr") > 0;
        ret = ret && (double) getParam("po") >= 0;
        ret = ret && toInteger(getParam("hms")) > 0;
        ret = ret && toInteger(getParam("nExp")) > 0;
        ret = ret && toInteger(getParam("nIt")) > 0;
        ret = ret && toInteger(getParam("maxK")) > 0;
        ret = ret && toInteger(getParam("maxKMeans")) > 0;
        ret = ret && toInteger(getParam("threads")) > 0;
        ret = ret && ((List<String>) getParam("datasets")).size() > 0;
        ret = ret && ((List<String>) getParam("objectiveFunctions")).size() > 0;
        ret = ret && ((List<String>) getParam("distances")).size() > 0;
        ret = ret && ((List<String>) getParam("algorithms")).size() > 0;
        ret = ret && kmAlgo != null && !kmAlgo.isEmpty();
        ret = ret && initialization != null && !initialization.isEmpty();

        String datasetsPath = (String) getParam("datasetsPath");
        if (datasetsPath == null || datasetsPath.isEmpty()) {
            throw new Exception("Falta la ruta a los datasets");
        }

        return ret;
    }

    public Integer toInteger(Object var) {
        if (var instanceof Integer) {
            return (Integer) var;
        }

        if (var instanceof Double) {
            Double tmp = (Double) var;
            return tmp.intValue();
        }

        return null;
    }

    public boolean verifyKMeans() throws Exception {

        boolean ret = true;
        // TODO: Si no se encuentra la config, mas vale lanzar una exception.
        String kmAlgo = (String) getParam("kmeansAlgorithm");
        String dataset = (String) getParam("dataset");
        String distance = (String) getParam("distance");
        String objectiveFunction = (String) getParam("objectiveFunction");
        String initialization = (String) getParam("initialization");
        int nExp = ((Double) getParam("nExp")).intValue();
        int k = ((Double) getParam("k")).intValue();
        int maxIt = ((Double) getParam("maxIt")).intValue();
        double po = (double) getParam("po");

        ret = ret && kmAlgo != null && !kmAlgo.isEmpty();
        ret = ret && dataset != null && !dataset.isEmpty();
        ret = ret && distance != null && !distance.isEmpty();
        ret = ret && distance != null && !objectiveFunction.isEmpty();
        ret = ret && initialization != null && !initialization.isEmpty();
        ret = ret && nExp >= 0;
        ret = ret && k >= 0;
        ret = ret && maxIt >= 0;
        ret = ret && po >= 0;

        String datasetsPath = (String) getParam("datasetsPath");
        if (datasetsPath == null || datasetsPath.isEmpty()) {
            throw new Exception("Falta la ruta a los datasets");
        }

        return ret;
    }

    public String getFields() {

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            sb.append(key).append("\t");
        }
        return sb.toString();

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            sb.append(value).append("\t");
        }
        return sb.toString();
    }

}
