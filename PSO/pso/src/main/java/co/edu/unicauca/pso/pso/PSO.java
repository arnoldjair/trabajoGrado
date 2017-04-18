/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unicauca.pso.pso;

import co.edu.unicauca.trabajogradogkr.model.Agent;
import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.model.objectivefunction.ObjectiveFunction;

/**
 *
 * @author equipo
 */
public interface PSO {

    public Agent process(Dataset dataset, ObjectiveFunction objectiveFunction);
}
