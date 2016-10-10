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
import java.util.List;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class TaskBuilder {

    public static List<Task> buildTasks(JsonParams params) throws Exception {
        if (!params.verify()) {
            return null;
        }

        List<Task> ret = new ArrayList<>();

        for (String algorithm : params.getAlgorithms()) {
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
        }

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
