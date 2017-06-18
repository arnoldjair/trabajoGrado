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

import co.edu.unicauca.trabajogradogkr.model.JsonParams;
import co.edu.unicauca.trabajogradogkr.model.gbhs.GBHS;
import co.edu.unicauca.trabajogradogkr.model.gbhs.GBHSCentroids;
import co.edu.unicauca.trabajogradogkr.model.gbhs.GBHSGroups;
import co.edu.unicauca.trabajogradogkr.model.gbhs.GBHSRecords;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class TaskBuilder {

    public static List<Task> buildTasks(JsonParams params) throws Exception {
        if (!params.verifyExperiment()) {
            return null;
        }

        boolean fixedK = false;
        boolean log = false;

        List<Task> ret = new ArrayList<>();
        String kmAlgo = (String) params.getParam("kmeansAlgorithm");
        String initialization = (String) params.getParam("initialization");
        boolean normalize = (boolean) (params.getParam("normalize") == null ? false : params.getParam("normalize"));
        Map<String, Object> filters = null;
        if (params.getParam("filters") != null) {
            List<Map<String, Object>> tmp = (List<Map<String, Object>>) params.getParam("filters");
            filters = new HashMap<>();
            for (Map<String, Object> map : tmp) {
                filters.put((String) map.get("filter"), map.get("value"));
            }
        }

        if (params.getParam("fixedK") != null) {
            fixedK = (boolean) params.getParam("fixedK");
        }

        if (params.getParam("log") != null) {
            log = (boolean) params.getParam("log");
        }

        for (String algorithm : (List<String>) params.getParam("algorithms")) {
            for (String dataset : (List<String>) params.getParam("datasets")) {
                for (String distance : (List<String>) params.getParam("distances")) {
                    for (String objectiveFunction : (List<String>) params.getParam("objectiveFunctions")) {
                        Task task = new Task(
                                (double) params.getParam("minPar"),
                                (double) params.getParam("maxPar"),
                                (double) params.getParam("hmcr"),
                                (double) params.getParam("po"),
                                params.toInteger(params.getParam("hms")),
                                params.toInteger(params.getParam("nExp")),
                                params.toInteger(params.getParam("nIt")),
                                params.toInteger(params.getParam("maxK")),
                                params.toInteger(params.getParam("maxKMeans")),
                                params.toInteger(params.getParam("threads")),
                                params.toInteger(params.getParam("seed")),
                                dataset, objectiveFunction, distance, algorithm,
                                false, kmAlgo, initialization, normalize,
                                fixedK, log, filters);
                        ret.add(task);
                    }
                }
            }
        }
        /*for (String algorithm : params.getAlgorithms()) {
            for (String dataset : params.getDatasets()) {
                for (String distance : params.getDistances()) {
                    for (String objectiveFunction : params.getObjectiveFunctions()) {
                        Task task = new Task(
                                params.getMinPar(),
                                params.getMaxPar(), params.getHmcr(), params.getPo(),
                                params.getHms(), params.getnExp(), params.getnIt(),
                                params.getMaxK(), params.getMaxKMeans(),
                                params.getThreads(), params.getSeed(),
                                dataset, objectiveFunction, distance, algorithm, false);
                        ret.add(task);
                    }
                }
            }
        }*/

        return ret;
    }

    public static GBHS getAlgorithm(String algorithm) {
        switch (algorithm) {
            case "records":
                return new GBHSRecords();
            case "centroids":
                return new GBHSCentroids();
            case "groups":
                return new GBHSGroups();
        }
        return null;
    }

}
