package co.edu.unicauca.trabajogradogkr;

import co.edu.unicauca.trabajogradogkr.R.RKmeansOutput;
import co.edu.unicauca.trabajogradogkr.R.RUtils;
import co.edu.unicauca.trabajogradogkr.model.distance.Distance;
import co.edu.unicauca.trabajogradogkr.exception.AttributeException;
import co.edu.unicauca.trabajogradogkr.exception.DatasetException;
import co.edu.unicauca.trabajogradogkr.exception.DistanceException;
import co.edu.unicauca.trabajogradogkr.model.Agent;
import co.edu.unicauca.trabajogradogkr.model.ContingencyMatrix;
import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.model.ECVM;
import co.edu.unicauca.trabajogradogkr.model.Experimenter;
import co.edu.unicauca.trabajogradogkr.model.JsonParams;
import co.edu.unicauca.trabajogradogkr.model.Result;
import co.edu.unicauca.trabajogradogkr.model.distance.DistanceFactory;
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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import co.edu.unicauca.trabajogradogkr.model.kmeans.KMeans;
import co.edu.unicauca.trabajogradogkr.model.kmeans.KMeansFactory;

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

        //Config.getInstance().setConfig("datasetsPath", "/home/equipo/Documentos/TrabajoGradoRGS/Codigo/TrabajoGradoGKR/Código/Datasets/json");
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

        if (web) {
            SpringApplication.run(TrabajoGradoGKR.class, args);
        } else {

            // TODO: Toca volver a hacer esto
            if (fromR) {
                double promErr = 0;
                List<RKmeansOutput> outputs = RUtils.read(pathResults);
                List<Agent> agents = new ArrayList<>();
                // TODO: Se asume que el archivo no está vacío.
                Dataset dataset = datasetService.fromJson(outputs.get(0).getName(), true);
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
                gson = new GsonBuilder().setPrettyPrinting().create();
                String resultado = gson.toJson(map, HashMap.class);
                Report tmpReport = new Report("RKmeans_" + dataset.getName() + ".json");
                tmpReport.writeLine(resultado);
                tmpReport.close();
                return;
            }

            if (kmeans) {
                testKMeans(params);
                return;
            }

            Config.getInstance().initResultFolder();

            // TODO: Configuración aparte para afinar.
            if ((String) params.getParam("tuneUp") != null && !((String) params.getParam("tuneUp")).isEmpty()) {
                tuneUp(params);
                return;
            }

            experiment(params);
        }

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
                Experimenter exp = new Experimenter(task);
                Future<Result> future = pool.submit(exp);
                futureObjs.add(future);
            }

            for (Future<Result> futureObj : futureObjs) {
                results.add(futureObj.get());
            }

            pool.shutdown();

            /*
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.setPrettyPrinting().create();

            Type resultType = new TypeToken<List<Result>>() {
            }.getType();
             */
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

        } catch (AttributeException | DatasetException ex) {
            Logger.getLogger(TrabajoGradoGKR.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException | IllegalAccessException | InterruptedException | ExecutionException ex) {
            Logger.getLogger(TrabajoGradoGKR.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            milis = System.currentTimeMillis() - milis;
            report.writeLine("tiempo\t" + milis + "\tmilis\n\n");
            System.out.println("tiempo\t" + milis + "\tmilis");
            report.close();
        }

    }

    public static void tuneUp(JsonParams jsonParams) throws Exception {
        /*long milis = System.currentTimeMillis();
        Tuner tuner = new RandomTuner();
        SimpleDateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");

        switch (jsonParams.getTuneUp()) {
            case "random":
                tuner = new RandomTuner();
                break;
            case "gbhs":
                //TODO: Cambiar parámetros
                break;
        }

        //Thread
        int maxT = jsonParams.getThreads();
        ExecutorService pool = Executors.newFixedThreadPool(maxT);

        Report report = new Report("Afinar" + dFormat.format(new Date()) + ".csv");

        StringBuilder sb = new StringBuilder();

        sb.append("minPar").append("\t")
                .append("maxPar").append("\t")
                .append("HMCR").append("\t")
                .append("PO").append("\t")
                .append("HMS").append("\t")
                .append("nExp").append("\t")
                .append("nIt").append("\t")
                .append("maxK").append("\t")
                .append("maxKMeans").append("\t")
                .append("ER").append("\n");
        report.writeLine(sb.toString());

        List<Future<JsonParams>> futureObjs = new ArrayList<>();

        int tuneUpIt = jsonParams.getTuneUpIt() == 0 ? 1 : jsonParams.getTuneUpIt();

        for (int i = 0; i < tuneUpIt; i++) {
            Tuner currTuner = tuner.getClass().newInstance();
            Random random = new SecureRandom();
            JsonParams currParams = new JsonParams();
            currParams.setMinPar(random.nextDouble());
            currParams.setMaxPar(currParams.getMinPar() + (1 - currParams.getMinPar()) * random.nextDouble());
            currParams.setHmcr(random.nextDouble());
            currParams.setPo(random.nextDouble());
            currParams.setHms(1 + random.nextInt(50));
            currParams.setDatasetsPath(jsonParams.getDatasetsPath());
            currParams.setnExp(jsonParams.getnExp());
            currParams.setMaxK(2 + random.nextInt(20));
            currParams.setMaxKMeans(10 + random.nextInt(100));
            currParams.setPo(random.nextDouble());
            currParams.setnIt(100 + random.nextInt(1000));
            currParams.setThreads(jsonParams.getThreads());
            currParams.setDatasets(jsonParams.getDatasets());
            currParams.setObjectiveFunctions(jsonParams.getObjectiveFunctions());
            currParams.setDistances(jsonParams.getDistances());
            currParams.setAlgorithms(jsonParams.getAlgorithms());
            List<Task> tasks = TaskBuilder.buildTasks(currParams);
            currTuner.setTasks(tasks);
            currTuner.setParams(currParams);
            Future<JsonParams> futureObj = pool.submit(currTuner);
            futureObjs.add(futureObj);
        }

        List<JsonParams> params = new ArrayList<>();

        for (Future<JsonParams> futureObj : futureObjs) {
            JsonParams curr = futureObj.get();
            sb = new StringBuilder();
            sb.append(curr.getMinPar()).append("\t")
                    .append(curr.getMaxPar()).append("\t")
                    .append(curr.getHmcr()).append("\t")
                    .append(curr.getPo()).append("\t")
                    .append(curr.getHms()).append("\t")
                    .append(curr.getnExp()).append("\t")
                    .append(curr.getnIt()).append("\t")
                    .append(curr.getMaxK()).append("\t")
                    .append(curr.getMaxKMeans()).append("\t")
                    .append(curr.getEr()).append("\n");
            report.writeLine(sb.toString());
        }

        pool.shutdown();

        report.close();

        milis = System.currentTimeMillis() - milis;
        System.out.println(milis / 1000 + " Segundos");
         */
    }

}
