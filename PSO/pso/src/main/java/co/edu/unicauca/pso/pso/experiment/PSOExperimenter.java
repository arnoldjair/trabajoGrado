/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unicauca.pso.pso.experiment;

import co.edu.unicauca.trabajogradogkr.model.Experimenter;
import co.edu.unicauca.trabajogradogkr.model.Result;
import java.util.concurrent.Callable;

/**
 *
 * @author equipo
 */
public class PSOExperimenter implements Callable<Result>, Experimenter {

    @Override
    public synchronized Result experiment() {
        return null;
    }

    @Override
    public Result call() throws Exception {
        return this.experiment();
    }

}
