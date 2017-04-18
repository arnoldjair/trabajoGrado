package co.edu.unicauca.pso;

import co.edu.unicauca.trabajogradogkr.model.JsonParams;
import co.edu.unicauca.trabajogradogkr.model.Result;
import co.edu.unicauca.trabajogradogkr.model.task.Task;
import co.edu.unicauca.trabajogradogkr.model.task.TaskBuilder;
import co.edu.unicauca.trabajogradogkr.service.Config;
import co.edu.unicauca.trabajogradogkr.utils.Report;
import com.google.gson.Gson;
import gnu.getopt.Getopt;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws FileNotFoundException {
        int c;
        Map<String, Object> tmpMap = new HashMap<>();
        Getopt go = new Getopt("PSO", args, "p:");
        JsonParams params = null;
        Gson gson = new Gson();

        while ((c = go.getopt()) != -1) {
            switch (c) {
                case 'p':
                    tmpMap = gson.fromJson(new FileReader(go.getOptarg()), tmpMap.getClass());
                    params = new JsonParams();
                    params.setMap(tmpMap);
                    Config.getInstance().setConfig("datasetsPath", (String) params.getParam("datasetsPath"));
                    break;
            }
        }

        experiment(params);

    }

    public static void experiment(JsonParams params) {
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
                
            }

        } catch (Exception e) {

        }
    }
}
