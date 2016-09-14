package co.edu.unicauca.trabajogradogkr;

import co.edu.unicauca.trabajogradogkr.distance.Distance;
import co.edu.unicauca.trabajogradogkr.distance.EuclideanDistance;
import co.edu.unicauca.trabajogradogkr.exception.AttributeException;
import co.edu.unicauca.trabajogradogkr.exception.DatasetException;
import co.edu.unicauca.trabajogradogkr.exception.DistanceException;
import co.edu.unicauca.trabajogradogkr.model.Agent;
import co.edu.unicauca.trabajogradogkr.model.ContingencyMatrix;
import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.model.ECVM;
import co.edu.unicauca.trabajogradogkr.model.Experimenter;
import co.edu.unicauca.trabajogradogkr.model.Params;
import co.edu.unicauca.trabajogradogkr.model.Result;
import co.edu.unicauca.trabajogradogkr.model.gbhs.GBHS;
import co.edu.unicauca.trabajogradogkr.model.gbhs.GBHSCentroids;
import co.edu.unicauca.trabajogradogkr.model.gbhs.GBHSGroups;
import co.edu.unicauca.trabajogradogkr.model.gbhs.GBHSRecords;
import co.edu.unicauca.trabajogradogkr.model.gbhs.GBHSTuner;
import co.edu.unicauca.trabajogradogkr.model.gbhs.Tuner;
import co.edu.unicauca.trabajogradogkr.model.kmeans.KMeans;
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.AIC;
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.BIC;
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.CHI;
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.ObjectiveFunction;
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.SI;
import co.edu.unicauca.trabajogradogkr.model.rgs.Partition;
import co.edu.unicauca.trabajogradogkr.service.Config;
import co.edu.unicauca.trabajogradogkr.utils.Report;
import gnu.getopt.Getopt;
import java.io.File;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * Spring
 */
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author equipo
 */
@RestController
@EnableAutoConfiguration
public class TrabajoGradoGKR {

    public static void main(String[] args) throws DistanceException, Exception {

        SpringApplication.run(TrabajoGradoGKR.class, args);

        /*
        Locale.setDefault(new Locale("es", "CO"));

        if (args.length == 0) {
            System.out.println("Uso: java -jar <Nombre archivo jar> -d <ruta al dataset> etc");
            return;
        }

        String rDataset = null;
        String rDatasets = null;
        Getopt go = new Getopt("Trabajo Grado", args, "c:d:D:e:g:i:k:m:M:o:s:S:t:");
        int c;
        int K = 3;
        int it = 1;
        int nExp = 10;
        int algo = 0;
        boolean e = false;
        boolean p = false;
        boolean g = false;
        boolean tuneUp = false;
        String distanceName = "euclidiana";

        //Parámetros algo
        double minPar = 0.9067848536;
        double maxPar = 0.9598223071;
        double hmcr = 0.1157166472;
        double po = 0.876586636;
        long seed = 10;
        int hms = 11;

        while ((c = go.getopt()) != -1) {
            switch (c) {

                case 'c': //HMCR
                    hmcr = Double.parseDouble(go.getOptarg());
                    break;
                case 'd': //Dataset
                    rDataset = go.getOptarg();
                    break;
                case 'D': //Distance
                    distanceName = go.getOptarg();
                    break;
                case 'e': //Experimentar
                    e = true;
                    nExp = Integer.parseInt(go.getOptarg());
                    break;
                case 'g': //Experimentar gbhs
                    g = true;
                    algo = Integer.parseInt(go.getOptarg());
                    break;
                case 'i': //Iteraciones
                    it = Integer.parseInt(go.getOptarg());
                    break;
                case 'k': //KMeans
                    K = Integer.parseInt(go.getOptarg());
                    p = true;
                    break;
                case 'm': //MinPar
                    minPar = Double.parseDouble(go.getOptarg());
                    break;
                case 'M': //MaxPar
                    maxPar = Double.parseDouble(go.getOptarg());
                    break;
                case 'o': //Porcentaje optimizar
                    po = Double.parseDouble(go.getOptarg());
                    break;
                case 's': //HMS
                    hms = Integer.parseInt(go.getOptarg());
                    break;
                case 'S': //Seed
                    seed = Long.parseLong(go.getOptarg());
                    break;
                case 't': //Afinar
                    tuneUp = true;
                    rDatasets = go.getOptarg();
                    break;
                case '?': //En caso de error
                    System.out.println("Opción inválida");
                    return;
            }
        }

        Distance distance = getDistance(distanceName);
        if (distance == null) {
            System.out.println("Distance incorrecta");
            return;
        }

        Config.getInstance().initResultFolder();

        if (tuneUp) {
            tuneUp(distance, rDatasets, it);
        }

        if (rDataset == null) {
            System.out.println("Indicar dataset");
            return;
        }

        File file = new File(rDataset);
        Dataset dataset = new Dataset();
        dataset.fromFile(file);
        String datasetName = dataset.getName();

        if (datasetName == null || datasetName.isEmpty()) {
            throw new Exception("Falta el nombre del dataset");
        }

        dataset.normalize();

        if (p) {
            testKMeans(it, K, dataset, distance);
        }

        if (e) {
            experiment(dataset, nExp, minPar, maxPar, hmcr, hms, po, seed, distance);
        }

        if (g) {
            GBHS gbhs = null;
            switch (algo) {
                case 1: //Registros
                    gbhs = new GBHSRecords();
                    break;
                case 2: //Centroides
                    gbhs = new GBHSCentroids();
                    break;
                case 3: //Grupos
                    gbhs = new GBHSGroups();
                    break;
            }

            if (gbhs == null) {
                System.out.println("Algoritmos disponibles\n1: registros\n2: centroides\n3: grupos");
            } else {
                testGBHS(dataset, gbhs, minPar, maxPar, hmcr, hms, po, distance);
            }
        }
         */
    }

    public static void testKMeans(int it, int k, Dataset dataset, Distance distance) {
        Random random = new SecureRandom();
        KMeans kmeans = new KMeans();
        String datasetName = dataset.getName();
        SimpleDateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        String tmp = "Prueba_" + datasetName + "_" + dFormat.format(new Date());
        File resultFolder = new File(tmp);
        resultFolder.mkdir();

        Report report = new Report(tmp + "/Resumen.csv");

        StringBuilder sb = new StringBuilder();
        sb.append("\"ICC\"").append("\t\"IIC\"")
                .append("\t\"ER\"\n");
        report.writeLine(sb.toString());

        for (int i = 0; i < it; i++) {
            Partition p = Partition.randPartition(dataset.getN(), k, random);
            Agent a = new Agent();
            a.setP(p);
            Agent sol = kmeans.process(a, dataset, distance, 0.05, 100);
            ContingencyMatrix m = new ContingencyMatrix(sol, dataset);
            ECVM ecvm = new ECVM(m);
            int icc = ecvm.getIcc();
            int iic = dataset.getN() - icc;
            double er = ((double) iic / dataset.getN()) * 100;
            report.writeLine(icc + "\t" + iic + "\t" + er + "\n");
        }

        report.close();
    }

    public static Distance getDistance(String distancia) {
        if (distancia == null || distancia.isEmpty()) {
            return null;
        }

        switch (distancia) {
            case "euclidiana":
                return new EuclideanDistance();
            /*case "manhattan":
                return new DistanceManhattan();*/
        }

        return null;
    }

    public static void testGBHS(Dataset dataset, GBHS algorithm, double minPar, double maxPar,
            double hmcr, int hms, double po, Distance distance) throws Exception {
        int maxK = (int) Math.sqrt(dataset.getN());
        ObjectiveFunction[] functions = getObjectiveFunctions();
        Random random = new SecureRandom();

        String datasetName = dataset.getName();
        if (datasetName == null || datasetName.isEmpty()) {
            throw new Exception("Falta el nombre del dataset");
        }

        for (ObjectiveFunction function : functions) {
            Agent a = algorithm.process(hms, 1000, maxK, 100, 0.0, minPar, maxPar, hmcr,
                    0.7, dataset, function, true, random, distance);
            ContingencyMatrix m = new ContingencyMatrix(a, dataset);

            ECVM ecvm = new ECVM(m);

            System.out.println(ecvm.getER());
        }
    }

    public static GBHS[] getAlgorithms() {
        return new GBHS[]{
            new GBHSRecords(),
            new GBHSCentroids(),
            new GBHSGroups()
        };
    }

    public static ObjectiveFunction[] getObjectiveFunctions() {
        return new ObjectiveFunction[]{
            //new AIC(), new CHI(), //new SI(),
            //new BIC(),
            new SI()
        };
    }

    public static void experiment(Dataset dataset, int nExp, double minPar,
            double maxPar, double hmcr, int hms, double po, long seed,
            Distance distance) throws Exception {
        long milis = System.currentTimeMillis();
        try {
            System.out.println("Experimentar");
            System.out.println("minPar: " + minPar);
            System.out.println("maxPar: " + maxPar);
            System.out.println("hmcr: " + hmcr);
            System.out.println("hms: " + hms);
            System.out.println("po: " + po);
            System.out.println("seed: " + seed);

            String datasetName = dataset.getName();
            if (datasetName == null || datasetName.isEmpty()) {
                throw new Exception("Falta el nombre del dataset");
            }

            SimpleDateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
            String tmp = datasetName + "_" + dFormat.format(new Date());
            File resultFolder = new File(tmp);
            resultFolder.mkdir();

            //Algoritmos
            GBHS[] algorithms = getAlgorithms();

            //Indices internos/Funciones objetivo
            ObjectiveFunction[] objectiveFunctions = getObjectiveFunctions();

            //Parámetros algoritmo
            int maxK = (int) Math.sqrt(dataset.getN() / 2);
            int maxImprovisations = 1000;
            int maxKMeans = 100;

            Result[] results = new Result[algorithms.length * objectiveFunctions.length];

            //Thread
            int maxT = 2;
            ExecutorService pool = Executors.newFixedThreadPool(maxT);
            Future<Result>[][] futureObjs = new Future[algorithms.length][objectiveFunctions.length];

            int pAlgo = 0;
            int pFunc = 0;
            int pos = 0;
            for (GBHS a : algorithms) {
                pFunc = 0;
                for (ObjectiveFunction f : objectiveFunctions) {
                    String id = a.toString() + "-" + f.toString() + ".csv";
                    Experimenter exp = new Experimenter(hms, maxImprovisations, maxK,
                            maxKMeans, nExp, minPar, maxPar, hmcr, po, dataset, f,
                            false, seed, a, id, distance.newInstance());

                    Future<Result> future = pool.submit(exp);
                    futureObjs[pAlgo][pFunc] = future;
                    pFunc++;
                }
                pAlgo++;
            }

            pos = 0;
            for (int i = 0; i < algorithms.length; i++) {
                for (int j = 0; j < objectiveFunctions.length; j++) {
                    results[pos] = futureObjs[i][j].get();
                    pos++;
                }
            }

            Arrays.sort(results);
            int tResults = pos;
            pool.shutdown();

            Report report = new Report(tmp + "/Resumen.csv");
            Report params = new Report(tmp + "/params.csv");
            params.writeLine("minpar\t" + minPar);
            params.writeLine("\nmaxpar\t" + maxPar);
            params.writeLine("\nhmcr\t" + hmcr);
            params.writeLine("\nhms\t" + hms);
            params.writeLine("\npo\t" + po);
            params.writeLine("\nnexp\t" + nExp);
            params.writeLine("\nseed\t" + seed);
            params.close();

            StringBuilder sb = new StringBuilder();
            sb.append("Algoritmo-funcion")
                    .append("\t\"PromICC\"").append("\t\"PromIIC\"")
                    .append("\t\"PromER\"")
                    .append("\t\"SDeviation\"\n");
            report.writeLine(sb.toString());

            sb = new StringBuilder(tmp);
            sb.append("/Soluciones");
            String nCarpetaResults = sb.toString();
            File fCarpetaResults = new File(nCarpetaResults);
            fCarpetaResults.mkdirs();

            for (int i = 0; i < tResults; i++) {
                Result result = results[i];
                result.calcAverages();
                report.writeLine(result.summary());
                Report individualReport = new Report(tmp + "/" + result.getId());
                individualReport.writeLine(result.toString());
                individualReport.close();

                Report agentsReport = new Report(nCarpetaResults + "/" + result.getId());

                for (int j = 0; j < result.getAgents().length; j++) {
                    agentsReport.writeLine("it," + j + "\n");
                    agentsReport.writeLine(result.getAgents()[j].toString());
                }

                agentsReport.close();
            }

            report.close();

        } catch (AttributeException | DatasetException ex) {
            Logger.getLogger(TrabajoGradoGKR.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException | IllegalAccessException | InterruptedException | ExecutionException ex) {
            Logger.getLogger(TrabajoGradoGKR.class.getName()).log(Level.SEVERE, null, ex);
        }
        milis = System.currentTimeMillis() - milis;
        System.out.println((double) milis / 1000);

    }

    public static void tuneUp(Distance distance, String pDatasets, int it) throws Exception {
        long milis = System.currentTimeMillis();
        Tuner tuner;
        SimpleDateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        GBHS[] algorithms = getAlgorithms();
        ObjectiveFunction[] objectiveFunctions = getObjectiveFunctions();
        StringBuilder sb = new StringBuilder();
        File fDatasets = new File(pDatasets);

        if (!fDatasets.isDirectory()) {
            throw new Exception("Indicar la carpeta con los datasets.");
        }

        ArrayList<File> aDatasets = new ArrayList<>();

        for (File listFile : fDatasets.listFiles()) {
            String n = listFile.getName();
            if (n.endsWith(".data")) {
                aDatasets.add(listFile);
            }
        }

        if (aDatasets.isEmpty()) {
            throw new Exception("Ningún dataset");
        }

        Dataset[] datasets = new Dataset[aDatasets.size()];

        for (int i = 0; i < datasets.length; i++) {
            datasets[i] = new Dataset();
            datasets[i].fromFile(aDatasets.get(i));
            if (datasets[i].getName() == null || datasets[i].getName().isEmpty()) {
                throw new Exception("Nombre del dataset");
            }
            datasets[i].normalize();
        }

        //Thread
        int maxT = 2;
        ExecutorService pool = Executors.newFixedThreadPool(maxT);

        sb.append("Afinar_")
                .append("_")
                .append(dFormat.format(new Date())).append(".csv");

        Report report = new Report(sb.toString());

        sb = new StringBuilder();
        sb.append("Algoritmo").append("\t")
                .append("Funcion").append("\t")
                .append("minPar").append("\t")
                .append("maxPar").append("\t")
                .append("hmcr").append("\t")
                .append("hms").append("\t")
                .append("po").append("\t")
                .append("ER").append("\n");
        report.writeLine(sb.toString());

        List<Future<Params>> futureObjs = new ArrayList<>();
        List<Params> params = new ArrayList<>();

        for (GBHS algoritmo : algorithms) {
            for (ObjectiveFunction objectiveFunction : objectiveFunctions) {
                Random random = new SecureRandom();
                //Random random = new Random(1500);
                tuner = new GBHSTuner(datasets, algoritmo, objectiveFunction, distance, random);
                Future<Params> futureObj = pool.submit(tuner);
                futureObjs.add(futureObj);
            }
        }

        for (Future<Params> futureObj : futureObjs) {
            params.add(futureObj.get());
        }

        pool.shutdown();

        for (Params param : params) {
            report.writeLine(param.toString());
        }

        report.close();

        milis = System.currentTimeMillis() - milis;
        System.out.println(milis / 1000 + " Segundos");
    }

}
