package co.edu.unicauca.trabajogradogkr;

import co.edu.unicauca.trabajogradogkr.R.RKmeansOutput;
import co.edu.unicauca.trabajogradogkr.R.RUtils;
import co.edu.unicauca.trabajogradogkr.model.distance.Distance;
import co.edu.unicauca.trabajogradogkr.model.distance.EuclideanDistance;
import co.edu.unicauca.trabajogradogkr.model.distance.ManhattanDistance;
import co.edu.unicauca.trabajogradogkr.exception.AttributeException;
import co.edu.unicauca.trabajogradogkr.exception.DatasetException;
import co.edu.unicauca.trabajogradogkr.exception.DistanceException;
import co.edu.unicauca.trabajogradogkr.model.Agent;
import co.edu.unicauca.trabajogradogkr.model.ContingencyMatrix;
import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.model.ECVM;
import co.edu.unicauca.trabajogradogkr.model.Experimenter;
import co.edu.unicauca.trabajogradogkr.model.JsonParams;
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
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.BICS;
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.CHI;
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.ObjectiveFunction;
import co.edu.unicauca.trabajogradogkr.model.rgs.Partition;
import co.edu.unicauca.trabajogradogkr.model.task.Task;
import co.edu.unicauca.trabajogradogkr.model.task.TaskBuilder;
import co.edu.unicauca.trabajogradogkr.service.Config;
import co.edu.unicauca.trabajogradogkr.utils.Report;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gnu.getopt.Getopt;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring
 */
/**
 *
 * @author equipo
 */
@SpringBootApplication
public class TrabajoGradoGKR {

    public static void main(String[] args) throws DistanceException, Exception {

        Locale.setDefault(new Locale("es", "CO"));

        if (args.length == 0) {
            System.out.println("Uso: java -jar <Nombre archivo jar> -d <ruta al dataset> etc");
            return;
        }

        Getopt go = new Getopt("Trabajo Grado", args, "p:r:W");
        int c;
        boolean web = false;
        JsonParams params = null;
        String pathResults = null;

        while ((c = go.getopt()) != -1) {
            switch (c) {

                /*case 'c': //HMCR
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
                 */
                case 'p':
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    params = gson.fromJson(new FileReader(go.getOptarg()), JsonParams.class);
                    if (!params.verify()) {
                        throw new Exception("Error en los parámetros");
                    }
                    break;
                case 'r':
                    pathResults = go.getOptarg();
                    System.out.println(pathResults);
                    break;
                /*
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
                 */
                case 'W': //Web
                    web = true;
                    break;
                case '?': //En caso de error
                    System.out.println("Opción inválida");
                    return;
            }
        }

        if (web) {
            SpringApplication.run(TrabajoGradoGKR.class, args);
        } else {

            if (pathResults != null) {
                double promErr = 0;
                List<RKmeansOutput> outputs = RUtils.read(pathResults);
                List<Agent> agents = new ArrayList<>();
                // TODO: Se asume que el archivo no está vacío.
                Dataset dataset = Dataset.fromJson(outputs.get(0).getName());
                for (RKmeansOutput rKmeansOutput : outputs) {
                    int[] rgs = new int[rKmeansOutput.getCluster().size()];

                    for (int i = 0; i < rgs.length; i++) {
                        rgs[i] = rKmeansOutput.getCluster().get(i);
                    }

                    Partition p = Partition.reprocessRGS(rgs);
                    Agent agent = new Agent(p);
                    ContingencyMatrix matrix = new ContingencyMatrix(agent, dataset);
                    ECVM ecvm = new ECVM(matrix);
                    int icc = ecvm.getIcc();
                    int iic = dataset.getN() - icc;
                    double err = ((double) iic / dataset.getN()) * 100;
                    System.out.println("Error: " + err);
                    promErr += err;
                }
                promErr /= outputs.size();
                Map<String, Object> map = new HashMap<>();
                map.put("dataset", dataset.getName());
                map.put("promError", promErr);
                map.put("numIt", outputs.size());
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String resultado = gson.toJson(map, HashMap.class);
                Report tmpReport = new Report("RKmeans_" + dataset.getName() + ".json");
                tmpReport.writeLine(resultado);
                tmpReport.close();
                return;
            }

            Config.getInstance().initResultFolder();

            experiment(params);
        }
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
            case "euclidean":
                return new EuclideanDistance();
            case "manhattan":
                return new ManhattanDistance();
        }

        return null;
    }

    public static List<Distance> getDistances(JsonParams jsonParams) {
        List<Distance> ret = new ArrayList<>();
        for (String distance : jsonParams.getDistances()) {
            ret.add(getDistance(distance));
        }
        return ret;
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

    public static List<GBHS> getAlgorithms(List<String> algorithms) {
        List<GBHS> ret = new ArrayList<>();

        for (int i = 0; i < algorithms.size(); i++) {
            switch (algorithms.get(i)) {
                case "records":
                    ret.add(new GBHSRecords());
                    break;
                case "centroids":
                    ret.add(new GBHSRecords());
                    break;
                case "groups":
                    ret.add(new GBHSRecords());
                    break;
            }
        }

        return ret;
    }

    public static ObjectiveFunction[] getObjectiveFunctions() {

        return new ObjectiveFunction[]{
            new AIC(), new CHI(), //new SI(),
        //new BIC(), //new SI()
        };
    }

    public static List<ObjectiveFunction> getObjectiveFunctions(List<String> objectiveFunctions) {
        List<ObjectiveFunction> ret = new ArrayList();
        for (int i = 0; i < objectiveFunctions.size(); i++) {
            switch (objectiveFunctions.get(i)) {
                case "AIC":
                    ret.add(new AIC());
                    break;
                case "CHI":
                    ret.add(new CHI());
                    break;
                case "BIC":
                    ret.add(new BIC());
                    break;
                case "BICS":
                    ret.add(new BICS());
                    break;
            }
            return ret;
        }

        return ret;
    }

    public static void experiment(JsonParams params) throws Exception {
        long milis = System.currentTimeMillis();
        try {

            SimpleDateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");

            int maxT = 4;
            ExecutorService pool = Executors.newFixedThreadPool(maxT);
            List<Future<Result>> futureObjs = new ArrayList<>();
            List<Task> tasks = TaskBuilder.buildTasks(params);
            List<Result> results = new ArrayList();

            for (Task task : tasks) {
                Experimenter exp = new Experimenter(task);
                Future<Result> future = pool.submit(exp);
                futureObjs.add(future);
            }

            for (Future<Result> futureObj : futureObjs) {
                results.add(futureObj.get());
            }

            pool.shutdown();

            Date date = new Date();
            Report report = new Report("Resumen" + dFormat.format(date) + ".json");

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.setPrettyPrinting().create();

            Type resultType = new TypeToken<List<Result>>() {
            }.getType();

            report.writeLine(gson.toJson(results, resultType));
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
