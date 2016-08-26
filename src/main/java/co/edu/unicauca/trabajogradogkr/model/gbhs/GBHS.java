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
package co.edu.unicauca.trabajogradogkr.model.gbhs;

import co.edu.unicauca.trabajogradogkr.distance.Distance;
import co.edu.unicauca.trabajogradogkr.model.Agent;
import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.ObjectiveFunction;
import java.util.Random;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public interface GBHS {

    /**
     * Procesa el algoritmo gbhs.
     *
     * @param hms
     * @param maxImprovisations
     * @param maxK
     * @param maxKMeans
     * @param pKmeans
     * @param minPar
     * @param maxPar
     * @param hmcr
     * @param pOptimize
     * @param dataset
     * @param f
     * @param log
     * @param random
     * @param distance
     * @return
     */
    public Agent process(int hms, int maxImprovisations, int maxK,
            int maxKMeans, double pKmeans, double minPar, double maxPar, double hmcr, double pOptimize,
            Dataset dataset, ObjectiveFunction f, boolean log, Random random,
            Distance distance);

    public GBHS newInstance();
}
