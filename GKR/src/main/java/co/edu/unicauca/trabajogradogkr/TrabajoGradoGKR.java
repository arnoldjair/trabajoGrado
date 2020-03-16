package co.edu.unicauca.trabajogradogkr;

import co.edu.unicauca.trabajogradogkr.model.distance.Distance;
import co.edu.unicauca.trabajogradogkr.exception.AttributeException;
import co.edu.unicauca.trabajogradogkr.exception.DatasetException;
import co.edu.unicauca.trabajogradogkr.exception.DistanceException;
import co.edu.unicauca.trabajogradogkr.model.Agent;
import co.edu.unicauca.trabajogradogkr.model.ContingencyMatrix;
import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.model.ECVM;
import co.edu.unicauca.trabajogradogkr.model.GBHSExperimenter;
import co.edu.unicauca.trabajogradogkr.model.JsonParams;
import co.edu.unicauca.trabajogradogkr.model.Result;
import co.edu.unicauca.trabajogradogkr.model.distance.DistanceFactory;
import co.edu.unicauca.trabajogradogkr.model.gbhs.RandomTuner;
import co.edu.unicauca.trabajogradogkr.model.gbhs.Tuner;
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.ObjectiveFunction;
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.ObjectiveFunctionFactory;
import co.edu.unicauca.trabajogradogkr.model.rgs.Partition;
import co.edu.unicauca.trabajogradogkr.model.task.Task;
import co.edu.unicauca.trabajogradogkr.model.task.TaskBuilder;
import co.edu.unicauca.trabajogradogkr.service.Config;
import co.edu.unicauca.trabajogradogkr.service.DatasetServiceImpl;
import co.edu.unicauca.trabajogradogkr.service.DatasetService;
import co.edu.unicauca.trabajogradogkr.utils.Report;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gnu.getopt.Getopt;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import co.edu.unicauca.trabajogradogkr.model.kmeans.KMeans;
import co.edu.unicauca.trabajogradogkr.model.kmeans.KMeansFactory;

/**
 * Spring
 */
/**
 *
 * @author equipo
 */
public class TrabajoGradoGKR {

    public static void main(String[] args) throws DistanceException, Exception {

        Locale.setDefault(new Locale("es", "CO"));

        if (args.length == 0) {
            System.out.println("Argumentos");
            return;
        }

        Getopt go = new Getopt("Trabajo Grado", args, "jk:p:r:W");
        int c;
        int k = 0;
        boolean web = false;
        boolean kmeans = false;
        boolean fromR = false;
        JsonParams params = null;
        Map<String, Object> tmpMap = new HashMap<>();
        String pathResults = null;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        DatasetService datasetService = new DatasetServiceImpl();

        while ((c = go.getopt()) != -1) {
            switch (c) {

                case 'k': //KMeans
                    tmpMap = gson.fromJson(new FileReader(go.getOptarg()), tmpMap.getClass());
                    params = new JsonParams();
                    params.setMap(tmpMap);
                    if (!params.verifyKMeans()) {
                        throw new Exception("Error en los parámetros");
                    }
                    Config.getInstance().setConfig("datasetsPath", (String) params.getParam("datasetsPath"));
                    kmeans = true;
                    break;
                case 'j':
                    //TODO: Esto ya no debe funcionar.
                    List<Dataset> datasets = datasetService.getDatasets(true);
                    for (Dataset dataset : datasets) {
                        dataset.toFile();
                    }
                    break;
                case 'p':
                    tmpMap = gson.fromJson(new FileReader(go.getOptarg()), tmpMap.getClass());
                    params = new JsonParams();
                    params.setMap(tmpMap);
                    if (!params.verifyExperiment()) {
                        throw new Exception("Error en los parámetros");
                    }
                    Config.getInstance().setConfig("datasetsPath", (String) params.getParam("datasetsPath"));
                    break;
                case 'r':
                    // TODO: Se utilizó para calcular el error del clustering desde R.
                    pathResults = go.getOptarg();
                    fromR = true;
                    break;
                case 'W': //Web
                    web = true;
                    break;
                case '?': //En caso de error
                    System.out.println("Opción inválida");
                    return;
            }
        }

        if (kmeans) {
            testKMeans(params);
            return;
        }

        Config.getInstance().initResultFolder();

        // TODO: Configuración aparte para afinar.
        Boolean tuneUp = (Boolean) params.getParam("tuneUp");
        if (tuneUp != null && tuneUp) {
            tuneUp(params);
            return;
        }

        experiment(params);

    }

    public static void testKMeans(JsonParams params) throws FileNotFoundException, Exception {
        long milis = System.currentTimeMillis();
        Report report = new Report("ReporteKmeans.csv", true);
        Random random = new SecureRandom();
        DatasetService datasetService = new DatasetServiceImpl();
        boolean normalize = (boolean) (params.getParam("normalize") == null ? false : params.getParam("normalize"));
        Dataset dataset = datasetService.byName((String) params.getParam("dataset"), normalize);
        Distance distance = DistanceFactory.getDistance((String) params.getParam("distance"));
        SimpleDateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        ObjectiveFunction objectiveFunction
                = ObjectiveFunctionFactory.
                        getObjectiveFuncion((String) params.getParam("objectiveFunction"));
        int nExp = ((Double) params.getParam("nExp")).intValue();
        int k = ((Double) params.getParam("k")).intValue();
        int maxIt = ((Double) params.getParam("maxIt")).intValue();
        double po = (double) params.getParam("po");
        KMeans kmeans = KMeansFactory.getKMeans((String) params.getParam("kmeansAlgorithm"));
        String initialization = (String) params.getParam("initialization");
        StringBuilder sb = new StringBuilder();
        sb.append("date").append("\t")
                .append("averageEr").append("\t")
                .append(params.getFields()).append("\n");
        report.writeLine(sb.toString());

        double err = 0;

        for (int i = 0; i < nExp; i++) {
            Partition p = null;
            switch (initialization) {
                case "random":
                    p = Partition.randPartition(dataset.getN(), k, random);
                    break;
                case "kmeanspp":
                    p = Partition.RandPartitionKmeanspp(k, dataset, distance, random);
                    break;
                default:
                    throw new Exception("El método de init es incorrecto caballero");
            }

            Agent agent = new Agent();
            agent.setP(p);
            Agent sol;
            sol = kmeans.process(agent, dataset, distance, po, maxIt, objectiveFunction);
            ContingencyMatrix m = new ContingencyMatrix(sol, dataset);
            ECVM ecvm = new ECVM(m);
            int icc = ecvm.getIcc();
            int iic = dataset.getN() - icc;
            err += ((double) iic / dataset.getN()) * 100;
        }
        err /= nExp;
        sb = new StringBuilder();
        Date date = new Date();
        sb.append(dFormat.format(date)).append("\t")
                .append(Double.toString(err)).append("\t")
                .append(params.toString()).append("\n");
        report.writeLine(sb.toString());
        milis = System.currentTimeMillis() - milis;
        report.writeLine("tiempo\t" + milis + "\tmilis\n\n");
        System.out.println("tiempo\t" + milis + "\tmilis");
        report.close();
    }

    public static void experiment(JsonParams params) throws Exception {
        long milis = System.currentTimeMillis();
        Report report = new Report("Reporte.csv", true);
        try {

            SimpleDateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");

            int maxT = 4;
            ExecutorService pool = Executors.newFixedThreadPool(maxT);
            List<Future<Result>> futureObjs = new ArrayList<>();
            List<Task> tasks = TaskBuilder.buildTasks(params);
            List<Result> results = new ArrayList();

            for (Task task : tasks) {
                GBHSExperimenter exp = new GBHSExperimenter(task);
                Future<Result> future = pool.submit(exp);
                futureObjs.add(future);
            }

            for (Future<Result> futureObj : futureObjs) {
                results.add(futureObj.get());
            }

            pool.shutdown();

            StringBuilder sb = new StringBuilder();
            sb.append("date").append("\t")
                    .append("averageIcc").append("\t")
                    .append("averageIic").append("\t")
                    .append("averageEr").append("\t")
                    .append("standardDeviation").append("\t")
                    .append("dataset").append("\t")
                    .append("objectiveFunction").append("\t")
                    .append("distance").append("\t")
                    .append("algorithm").append("\t")
                    .append(params.getFields()).append("\n");
            report.writeLine(sb.toString());
            Date date = new Date();
            for (Result result : results) {
                sb = new StringBuilder();
                sb.append(dFormat.format(date)).append("\t")
                        .append(result.toString()).append("\t")
                        .append(params.toString()).append("\n");
                report.writeLine(sb.toString());
            }

        } catch (AttributeException | DatasetException | InstantiationException | IllegalAccessException | InterruptedException | ExecutionException ex) {
            Logger.getLogger(TrabajoGradoGKR.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            milis = System.currentTimeMillis() - milis;
            report.writeLine("tiempo\t" + milis + "\tmilis\n\n");
            System.out.println("tiempo\t" + milis + "\tmilis");
            report.close();
        }

    }

    public static void tuneUp(JsonParams jsonParams) throws Exception {
        long milis = System.currentTimeMillis();
        Tuner tuner = new RandomTuner();

        Double tmp = (Double) jsonParams.getParam("threads");
        int maxT = tmp.intValue();
        ExecutorService pool = Executors.newFixedThreadPool(maxT);

        Report report = new Report("Afinar.csv", true);

        StringBuilder sb = new StringBuilder();

        sb
                .append("minPar").append("\t")
                .append("maxPar").append("\t")
                .append("hmcr").append("\t")
                .append("hms").append("\t")
                .append("po").append("\t")
                .append("seed").append("\t")
                .append("nIt").append("\t")
                .append("maxK").append("\t")
                .append("maxKMeans").append("\t")
                .append("initialization").append("\t")
                .append("normalize").append("\t")
                .append("ER").append("\n");
        report.writeLine(sb.toString());

        List<Future<JsonParams>> futureObjs = new ArrayList<>();
        tmp = (Double) jsonParams.getParam("tuneUpIt");
        int tuneUpIt = tmp.intValue() == 0 ? 1 : tmp.intValue();

        for (int i = 0; i < tuneUpIt; i++) {
            Tuner currTuner = new RandomTuner();
            Random random = new SecureRandom();
            JsonParams currParams = new JsonParams();
            double minPar = random.nextDouble();
            currParams.setParam("minPar", minPar);
            currParams.setParam("maxPar", minPar + (1 - minPar) * random.nextDouble());
            currParams.setParam("hmcr", random.nextDouble());
            currParams.setParam("hms", random.nextInt(50));
            currParams.setParam("nExp", jsonParams.getParam("nExp"));
            currParams.setParam("po", random.nextDouble());
            currParams.setParam("seed", random.nextInt(2000));
            currParams.setParam("nIt", (100 + random.nextInt(1000)));
            currParams.setParam("maxK", (2 + random.nextInt(20)));
            currParams.setParam("maxKMeans", (10 + random.nextInt(100)));
            currParams.setParam("threads", jsonParams.getParam("threads"));
            currParams.setParam("datasets", jsonParams.getParam("datasets"));
            currParams.setParam("algorithms", jsonParams.getParam("algorithms"));
            currParams.setParam("distances", jsonParams.getParam("distances"));
            currParams.setParam("objectiveFunctions", jsonParams.getParam("objectiveFunctions"));
            currParams.setParam("datasetsPath", jsonParams.getParam("datasetsPath"));
            currParams.setParam("kmeansAlgorithm", jsonParams.getParam("kmeansAlgorithm"));
            currParams.setParam("initialization", jsonParams.getParam("initialization"));
            currParams.setParam("normalize", jsonParams.getParam("normalize"));
            currParams.setParam("fixedK", jsonParams.getParam("fixedK"));
            currParams.setParam("log", jsonParams.getParam("log"));

            List<Task> tasks = TaskBuilder.buildTasks(currParams);
            currTuner.setTasks(tasks);
            currTuner.setParams(currParams);
            Future<JsonParams> futureObj = pool.submit(currTuner);
            futureObjs.add(futureObj);
        }

        for (Future<JsonParams> futureObj : futureObjs) {
            JsonParams curr = futureObj.get();
            sb = new StringBuilder();

            sb
                    .append(curr.getParam("minPar")).append("\t")
                    .append(curr.getParam("maxPar")).append("\t")
                    .append(curr.getParam("hmcr")).append("\t")
                    .append(curr.getParam("hms")).append("\t")
                    .append(curr.getParam("po")).append("\t")
                    .append(curr.getParam("seed")).append("\t")
                    .append(curr.getParam("nIt")).append("\t")
                    .append(curr.getParam("maxK")).append("\t")
                    .append(curr.getParam("maxKMeans")).append("\t")
                    .append(curr.getParam("initialization")).append("\t")
                    .append(curr.getParam("normalize")).append("\t")
                    .append(curr.getParam("ER")).append("\n");

            report.writeLine(sb.toString());
        }

        pool.shutdown();

        report.close();

        milis = System.currentTimeMillis() - milis;
        System.out.println(milis / 1000 + " Segundos");
    }

}
