/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unicauca.trabajogradogkr.model;

import java.util.Comparator;

/**
 *
 * @author Arnold Jair Jimenez Vargas arnoldjair@hotmail.com
 */
public class AgentComparator implements Comparator<Agent> {

    private boolean minimizes;

    public AgentComparator() {
    }

    public AgentComparator(boolean minimize) {
        this.minimizes = minimize;
    }

    public boolean getMinimizes() {
        return minimizes;
    }

    public void setMinimizes(boolean minimizes) {
        this.minimizes = minimizes;
    }

    @Override
    public int compare(Agent o1, Agent o2) {
        if (o1.getFitness() == o2.getFitness()) {
            return 0;
        }
        if (this.minimizes) {
            return o1.getFitness() < o2.getFitness() ? -1 : 1;
        } else {
            return o1.getFitness() > o2.getFitness() ? -1 : 1;
        }
    }

}
