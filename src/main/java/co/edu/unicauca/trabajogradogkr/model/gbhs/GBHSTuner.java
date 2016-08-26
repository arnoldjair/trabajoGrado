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
import co.edu.unicauca.trabajogradogkr.model.ContingencyMatrix;
import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.model.ECVM;
import co.edu.unicauca.trabajogradogkr.model.Params;
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.ObjectiveFunction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class GBHSTuner implements Tuner {

    private final GBHS algorithm;
    private final ObjectiveFunction objectiveFunction;
    private final Distance distance;
    private final Random random;
    private final Dataset[] datasets;

    public GBHSTuner(Dataset[] datasets, GBHS algorithm, ObjectiveFunction objectiveFunction,
            Distance distance, Random random) {
        this.algorithm = algorithm;
        this.objectiveFunction = objectiveFunction;
        this.distance = distance;
        this.random = random;
        this.datasets = datasets;
    }

    @Override
    public Params tuneUp() {
        System.out.println("Afinando.");

        double minPar = 0.3;
        double maxPar = 0.5;
        int hms = 20;
        double hmcr = 0.9;
        double po = 0.7;
        int maxImprovisations = 30;
        List<Params> harmonyMemory = generateHarmonyMemory(hms, algorithm, datasets, distance, objectiveFunction);
        double par = minPar + (maxPar - minPar) / maxImprovisations;
        int pos;

        for (int cIt = 0; cIt < maxImprovisations; cIt++) {
            System.out.println("Improvisación afinador: " + cIt);
            Params params = new Params();

            //minPar y maxPar
            double num = random.nextDouble();
            if (num <= hmcr) {
                pos = random.nextInt(hms);
                params.minPar = harmonyMemory.get(pos).minPar;
                params.maxPar = harmonyMemory.get(pos).maxPar;
                if (random.nextDouble() < par) {
                    params.minPar = harmonyMemory.get(0).minPar;
                    params.maxPar = harmonyMemory.get(0).maxPar;
                }
            } else {
                params.minPar = random.nextDouble();
                params.maxPar = params.minPar + (1 - params.minPar) * random.nextDouble();
            }

            //hms
            num = random.nextDouble();
            if (num <= hmcr) {
                pos = random.nextInt(hms);
                params.hms = harmonyMemory.get(pos).hms;
                if (random.nextDouble() < par) {
                    params.hms = harmonyMemory.get(0).hms;
                }
            } else {
                params.hms = 1 + random.nextInt(50);
            }

            //hmcr
            num = random.nextDouble();
            if (num <= hmcr) {
                pos = random.nextInt(hms);
                params.hmcr = harmonyMemory.get(pos).hmcr;
                if (random.nextDouble() < par) {
                    params.hmcr = harmonyMemory.get(0).hmcr;
                }
            } else {
                params.hmcr = random.nextDouble();
            }

            //po
            num = random.nextDouble();
            if (num <= hmcr) {
                pos = random.nextInt(hms);
                params.po = harmonyMemory.get(pos).po;
                if (random.nextDouble() < par) {
                    params.po = harmonyMemory.get(0).po;
                }
            } else {
                params.po = random.nextDouble();
            }

            params.objectiveFunction = objectiveFunction.toString();
            params.algorithm = algorithm.toString();
            params.ER = 0;

            for (Dataset dataset : datasets) {
                System.out.println("Dataset: " + dataset.getName());
                int maxK = (int) Math.sqrt(dataset.getN());
                for (int j = 0; j < 30; j++) {
                    System.out.println("Improvisacion param: " + j);
                    Agent a = algorithm.process(hms, 100, maxK, 100, 0, params.minPar,
                            params.maxPar, params.hmcr, params.po, dataset, objectiveFunction, false, random, distance);
                    ContingencyMatrix m = new ContingencyMatrix(a, dataset);
                    ECVM ecvm = new ECVM(m);
                    int icc = ecvm.getIcc();
                    int iic = dataset.getN() - icc;
                    params.ER += ((double) iic / dataset.getN()) * 100;
                    System.out.println("ER acumulado: " + params.ER);
                }
            }

            params.ER /= (30 * datasets.length);
            System.out.println("Promedio error: " + params.ER);

            if (harmonyMemory.get(harmonyMemory.size() - 1).ER > params.ER) {
                harmonyMemory.set(harmonyMemory.size() - 1, params);
            }

            Collections.sort(harmonyMemory);
        }

        return harmonyMemory.get(0);
    }

    private List<Params> generateHarmonyMemory(int hms, GBHS algorithm, Dataset[] datasets, Distance distance, ObjectiveFunction f) {
        System.out.println("Generando memoria armónica.");

        List<Params> ret = new ArrayList<>();

        double currE = 0;

        for (int i = 0; i < hms; i++) {
            System.out.println("Pos memoria: " + i);
            Params tmp = new Params();
            tmp.minPar = random.nextDouble();
            tmp.maxPar = tmp.minPar + (1 - tmp.minPar) * random.nextDouble();
            tmp.hmcr = random.nextDouble();
            tmp.po = random.nextDouble();
            tmp.hms = 1 + random.nextInt(50);
            tmp.algorithm = algorithm.toString();
            tmp.objectiveFunction = objectiveFunction.toString();
            tmp.ER = 0;

            for (Dataset dataset : datasets) {
                System.out.println("Dataset: " + dataset.getName());
                int maxK = (int) Math.sqrt(dataset.getN());
                for (int j = 0; j < 30; j++) {
                    System.out.println("Improvisación: " + j);
                    Agent a = algorithm.process(hms, 100, maxK, 100, 0, tmp.minPar, tmp.maxPar, tmp.hmcr, tmp.po, dataset, f, true, random, distance);
                    ContingencyMatrix m = new ContingencyMatrix(a, dataset);
                    ECVM ecvm = new ECVM(m);
                    int icc = ecvm.getIcc();
                    int iic = dataset.getN() - icc;
                    tmp.ER += ((double) iic / dataset.getN()) * 100;
                }
            }

            tmp.ER /= (30 * datasets.length);
            ret.add(tmp);
        }

        Collections.sort(ret);

        System.out.println("Lista memoria armónica.");
        return ret;
    }

    @Override
    public Params call() throws Exception {
        return this.tuneUp();
    }

}
