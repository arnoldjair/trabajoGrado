package co.edu.unicauca.trabajogradogkr.model;

import co.edu.unicauca.trabajogradogkr.exception.AttributeException;
import co.edu.unicauca.trabajogradogkr.exception.DatasetException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arnold Jair Jimenez Vargas ajjimenez@unicauca.edu.co
 */
public class Dataset {

    public static final int DOUBLE = 1;
    public static final int STRING = 2;
    public static final int CLASS = 3;

    private int n;
    private int classIndex;
    private boolean hasClass;
    private boolean normalized;
    private double[][] normValues;
    private String name;
    private String[] classes;
    private Record[] records;
    private Record[] oRecords;
    private Attribute[] attributes;

    public Dataset() {
        hasClass = false;
        normalized = false;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getClassIndex() {
        return classIndex;
    }

    public void setClassIndex(int classIndex) {
        this.classIndex = classIndex;
    }

    public boolean isHasClass() {
        return hasClass;
    }

    public void setHasClass(boolean hasClass) {
        this.hasClass = hasClass;
    }

    public boolean isNormalized() {
        return normalized;
    }

    public void setNormalized(boolean normalized) {
        this.normalized = normalized;
    }

    public double[][] getNormValues() {
        return normValues;
    }

    public void setNormValues(double[][] normValues) {
        this.normValues = normValues;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getClasses() {
        return classes;
    }

    public void setClasses(String[] classes) {
        this.classes = classes;
    }

    public Record[] getRecords() {
        return records;
    }

    public void setRecords(Record[] records) {
        this.records = records;
    }

    public Record[] getoRecords() {
        return oRecords;
    }

    public void setoRecords(Record[] oRecords) {
        this.oRecords = oRecords;
    }

    public Attribute[] getAttributes() {
        return attributes;
    }

    public void setAttributes(Attribute[] attributes) {
        this.attributes = attributes;
    }

    public synchronized boolean checkFormat(File file) {
        BufferedReader read;
        String line;
        int fields = 0;
        int attribs = 0;

        try {
            read = new BufferedReader(new FileReader(file));

            while ((line = read.readLine()) != null) {
                if (line.contains("@name") || line.contains("@data")) {
                    fields++;
                    continue;
                }
                if (line.contains("@attr")) {
                    attribs++;
                }
            }

            read.close();

            return fields > 2 && attribs != 0;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     *
     * @param file
     * @throws co.edu.unicauca.trabajogradogkr.exception.DatasetException
     * @throws co.edu.unicauca.trabajogradogkr.exception.AttributeException
     */
    public synchronized void fromFile(File file) throws DatasetException, AttributeException {
        BufferedReader read;
        String line;
        String[] split;
        Map<String, String> attrMap = new LinkedHashMap<>();
        Map<String, Integer> classMap = new LinkedHashMap<>();
        ArrayList<String[]> data = new ArrayList<>();

        try {
            read = new BufferedReader(new FileReader(file));
            if (!checkFormat(file)) {
                throw new DatasetException("Dataset mal formado");
            }
            while ((line = read.readLine()) != null) {
                if (line.contains("@name")) {
                    split = line.split("\t");
                    if (split.length == 2) {
                        name = split[1];
                    }
                }
                if (line.contains("@attribute")) {
                    split = line.split("\t");
                    if (split.length == 3) {
                        if (attrMap.containsKey(split[1])) {
                            throw new AttributeException("Atributo existente");
                        } else {
                            attrMap.put(split[1], split[2]);
                        }
                    } else {
                        throw new AttributeException("Definición incorrecta de atributo");
                    }
                }
                //Leer los datos_
                if (line.contains("@data")) {
                    while ((line = read.readLine()) != null) {
                        split = line.split("\t");
                        if (split.length == attrMap.size()) {
                            data.add(split);
                        }
                    }
                }
            }

            attributes = new Attribute[attrMap.size()];
            int cont = 0;

            for (String keySet : attrMap.keySet()) {
                Attribute a = new Attribute(keySet, Dataset.getAttrType(attrMap.get(keySet)));
                a.setName(keySet);
                if (a.getType() == 0) {
                    throw new AttributeException("Tipo de atributo inválido");
                }
                if (a.getType() == Dataset.CLASS) {
                    hasClass = true;
                    classIndex = cont;
                }
                attributes[cont] = a;
                cont++;
            }

            n = data.size();
            int rows = data.size();
            int cols = cont;

            normValues = new double[cols][2];

            for (int i = 0; i < cols; i++) {
                if (attributes[i].getType() == Dataset.DOUBLE) {
                    this.normValues[i][0] = this.normValues[i][1] = Double.parseDouble(data.get(0)[i]);
                }
            }

            int cCont = 0;
            records = new Record[rows];
            oRecords = new Record[rows];
            Object[] tmp;

            for (int i = 0; i < rows; i++) {
                tmp = new Object[cols];
                for (int j = 0; j < cols; j++) {
                    if (attributes[j].getType() == Dataset.DOUBLE) {
                        tmp[j] = Double.parseDouble(data.get(i)[j]);
                        this.normValues[j][0] = Math.min(this.normValues[j][0], (Double) tmp[i]);
                        this.normValues[j][1] = Math.max(this.normValues[j][1], (Double) tmp[i]);
                    } else {
                        tmp[j] = data.get(i)[j];
                        if (hasClass && j == classIndex) {
                            if (!classMap.containsKey(data.get(i)[j])) {
                                classMap.put(data.get(i)[j], cCont);
                                cCont++;
                            }
                        }
                    }
                }
                records[i] = new Record(i, tmp);
                oRecords[i] = new Record(i, tmp);
            }

            if (this.hasClass) {
                classes = new String[classMap.size()];
                Object[] m = classMap.keySet().toArray();
                for (int i = 0; i < m.length; i++) {
                    classes[i] = (String) m[i];
                }
            }

            //Por sospecha
            normalized = false;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized static int getAttrType(String t) {
        switch (t) {
            case "class":
                return Dataset.CLASS;
            case "double":
                return Dataset.DOUBLE;
            case "string":
                return Dataset.STRING;
        }
        return 0;
    }

}
