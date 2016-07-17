package co.edu.unicauca.trabajogradogkr;

import co.edu.unicauca.trabajogradogkr.distance.Distance;
import co.edu.unicauca.trabajogradogkr.distance.EuclideanDistance;
import co.edu.unicauca.trabajogradogkr.exception.DistanceException;
import co.edu.unicauca.trabajogradogkr.model.Agent;
import co.edu.unicauca.trabajogradogkr.model.ContingencyMatrix;
import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.model.ECVM;
import co.edu.unicauca.trabajogradogkr.model.kmeans.KMeans;
import co.edu.unicauca.trabajogradogkr.model.rgs.Partition;
import co.edu.unicauca.trabajogradogkr.service.Config;
import co.edu.unicauca.trabajogradogkr.utils.Report;
import gnu.getopt.Getopt;
import java.io.File;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 *
 * @author equipo
 */
public class TrabajoGradoGKR {

    public static void main(String[] args) throws DistanceException, Exception {

        Locale.setDefault(new Locale("es", "CO"));

        if (args.length == 0) {
            System.out.println("Uso: java -jar <Nombre archivo jar> -d <ruta al dataset> etc");
            return;
        }

        String rDataset = null;
        String rDatasets = null;
        Getopt go = new Getopt("Trabajo Grado", args, "a:c:d:D:e:g:i:k:m:M:o:s:S:");
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
        double minPar = 0.3;
        double maxPar = 0.5;
        double hmcr = 0.9;
        double po = 0.7;
        long seed = 10;
        int hms = 20;

        while ((c = go.getopt()) != -1) {
            switch (c) {
                case 'a': //Afinar
                    tuneUp = true;
                    rDatasets = go.getOptarg();
                    break;
                case 'c': //HMCR
                    hmcr = Double.parseDouble(go.getOptarg());
                    break;
                case 'd': //Dataset
                    rDataset = go.getOptarg();
                    break;
                case 'D': //Distancia
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
                case '?': //En caso de error
                    System.out.println("Opción inválida");
                    return;
            }
        }

        Distance distance = getDistance(distanceName);
        if (distance == null) {
            System.out.println("Distancia incorrecta");
            return;
        }

        Config.getInstance().initCarpetaResultado();

        if (tuneUp) {
            //afinar(distance, rDatasets, it);
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

        //TODO: Cambiar nombre Distancia por algo así como similitud.
        dataset.normalize();
        //dataset.calcularDistancias();

        if (p) {
            testKMeans(it, K, dataset, distance);
        }

        if (e) {
            //experimentar(dataset, nExp, minPar, maxPar, hmcr, hms, po, seed, distance);
        }

        /*if (g) {
            GBHS gbhs = null;
            switch (algo) {
                case 1: //Registros
                    gbhs = new GBHSRegistros();
                    break;
                case 2: //Centroides
                    gbhs = new GBHSCentroides();
                    break;
                case 3: //Grupos
                    gbhs = new GBHSGrupos();
                    break;
            }

            if (gbhs == null) {
                System.out.println("Algoritmos disponibles\n1: registros\n2: centroides\n3: grupos");
            } else {
                probarGBHS(dataset, gbhs, minPar, maxPar, hmcr, hms, po, distance);
            }
        }*/
    }

    public static void testKMeans(int it, int k, Dataset dataset, Distance distance) {
        Random random = new SecureRandom();
        KMeans kmeans = new KMeans();
        String datasetName = dataset.getName();
        SimpleDateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        String tmp = "Prueba_" + datasetName + "_" + dFormat.format(new Date());
        File folderResult = new File(tmp);
        folderResult.mkdir();

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
                return new DistanciaManhattan();*/
        }

        return null;
    }

}
