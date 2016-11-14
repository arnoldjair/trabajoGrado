/*
 * Copyright (C) 2016 Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>.
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
package co.edu.unicauca.trabajogradogkr.utils;

import co.edu.unicauca.trabajogradogkr.model.Agent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class Report {

    private String path;
    private File file;
    private BufferedWriter bw;

    public Report(String path, boolean append) {
        try {
            this.path = path;
            this.bw = new BufferedWriter(new FileWriter(new File(path), true));
        } catch (IOException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Report(String path) {
        try {
            this.path = path;
            this.bw = new BufferedWriter(new FileWriter(new File(path)));
        } catch (IOException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeLine(String line) {
        if (bw != null) {
            try {
                bw.write(line);
            } catch (IOException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void writeHarmonyMemory(List<Agent> agents, String text) {
        if (bw != null) {
            writeLine(text);
            writeLine("\n");
            for (Agent agent : agents) {
                writeLine("f: " + agent.getFitness() + " ");
                writeLine(agent.getP().toString());
                writeLine("\n");
            }
        }
    }

    public void close() {
        if (bw != null) {
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
