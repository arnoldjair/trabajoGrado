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
package co.edu.unicauca.trabajogradogkr.model.gbhs;

import co.edu.unicauca.trabajogradogkr.model.Experimenter;
import co.edu.unicauca.trabajogradogkr.model.JsonParams;
import co.edu.unicauca.trabajogradogkr.model.Result;
import co.edu.unicauca.trabajogradogkr.model.task.Task;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class RandomTuner implements Tuner {

    private List<Task> tasks;
    private JsonParams jsonParams;

    @Override
    public JsonParams tuneUp() {

        if (this.tasks == null) {
            return null;
        }

        double err = 0;

        for (Task task : tasks) {
            try {
                Experimenter exp = new Experimenter(task);
                Result result = exp.experiment();
                result.calcAverages();
                err += result.getAverageEr();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(RandomTuner.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(RandomTuner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        err = err / tasks.size();
        this.jsonParams.setParam("Er", err);
        return this.jsonParams;
    }

    @Override
    public JsonParams call() throws Exception {
        return this.tuneUp();
    }

    @Override
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void setParams(JsonParams params) {
        this.jsonParams = params;
    }

}
