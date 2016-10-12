package co.edu.unicauca.trabajogradogkr.model;

import co.edu.unicauca.trabajogradogkr.exception.AttributeException;
import co.edu.unicauca.trabajogradogkr.exception.DatasetException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Arnold Jair Jimenez Vargas ajjimenez@unicauca.edu.co
 */
public class Dataset {

    public static final int DOUBLE = 1;
    public static final int STRING = 2;
    public static final int CLASS = 3;

    public static final String datasetsPath = "Datasets";

    private int n;
    private int classIndex;
    private boolean hasClass;
    private boolean normalized;
    private double[][] normValues;
    private String name;
    private Record mean;
    private String[] classes;
    private Record[] records;
    private Record[] oRecords;
    private Attribute[] attributes;

    public Dataset() {
        hasClass = false;
        normalized = false;
    }

    public synchronized int getN() {
        return n;
    }

    public synchronized void setN(int n) {
        this.n = n;
    }

    public synchronized int getClassIndex() {
        return classIndex;
    }

    public synchronized void setClassIndex(int classIndex) {
        this.classIndex = classIndex;
    }

    public synchronized boolean isHasClass() {
        return hasClass;
    }

    public synchronized void setHasClass(boolean hasClass) {
        this.hasClass = hasClass;
    }

    public synchronized boolean isNormalized() {
        return normalized;
    }

    public synchronized void setNormalized(boolean normalized) {
        this.normalized = normalized;
    }

    public synchronized double[][] getNormValues() {
        return normValues;
    }

    public synchronized void setNormValues(double[][] normValues) {
        this.normValues = normValues;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized void setName(String name) {
        this.name = name;
    }

    public synchronized String[] getClasses() {
        return classes;
    }

    public synchronized void setClasses(String[] classes) {
        this.classes = classes;
    }

    public synchronized Record[] getRecords() {
        return records;
    }

    public synchronized Record getRecord(int index) {
        return records[index];
    }

    public synchronized void setRecords(Record[] records) {
        this.records = records;
    }

    public synchronized Record[] getoRecords() {
        return oRecords;
    }

    public synchronized void setoRecords(Record[] oRecords) {
        this.oRecords = oRecords;
    }

    public Record getMean() {
        if (mean == null) {
            calcMean();
        }
        return mean;
    }

    public void setMean(Record mean) {
        this.mean = mean;
    }

    private void calcMean() {
        mean = records[0];

        for (int i = 1; i < records.length; i++) {
            mean = mean.add(records[i]);
        }

        mean = mean.divide(n);
    }

    public synchronized Attribute[] getAttributes() {
        return attributes;
    }

    public synchronized void setAttributes(Attribute[] attributes) {
        this.attributes = attributes;
    }

    public synchronized boolean checkFormat(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        int fields = 0;
        int attribs = 0;

        try {
            stream.mark(0);
            while ((line = reader.readLine()) != null) {
                if (line.contains("@name") || line.contains("@data")) {
                    fields++;
                    continue;
                }
                if (line.contains("@attr")) {
                    attribs++;
                }
                if (fields >= 2) {
                    break;
                }
            }
            if (stream.markSupported()) {
                stream.reset();
            }
            return fields >= 2 && attribs != 0;

        } catch (Exception e) {
            return false;
        }
    }

    public synchronized void fromFile(MultipartFile multipartFile) throws IOException, DatasetException, AttributeException {

        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(multipartFile.getInputStream());
            //in = multipartFile.getInputStream();
            this.fromFile(in);

        } catch (IOException ex) {
            throw ex;
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public synchronized void fromFile(File file) throws IOException {
        try {
            fromFile(new FileInputStream(file));
        } catch (FileNotFoundException | DatasetException | AttributeException ex) {
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param in
     * @throws co.edu.unicauca.trabajogradogkr.exception.DatasetException
     * @throws co.edu.unicauca.trabajogradogkr.exception.AttributeException
     * @throws java.io.IOException
     */
    public synchronized void fromFile(InputStream in) throws DatasetException, AttributeException, IOException {
        //BufferedReader read;
        String line;
        String[] split;
        Map<String, String> attrMap = new LinkedHashMap<>();
        Map<String, Integer> classMap = new LinkedHashMap<>();
        ArrayList<String[]> data = new ArrayList<>();

        BufferedReader read = new BufferedReader(new InputStreamReader(in));

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
                    this.normValues[j][0] = Math.min(this.normValues[j][0], (Double) tmp[j]);
                    this.normValues[j][1] = Math.max(this.normValues[j][1], (Double) tmp[j]);
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
            records[i] = new Record(i, tmp, attributes);
            oRecords[i] = new Record(i, tmp, attributes);
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

    }

    public synchronized String toGson() {
        String ret = "";

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        ret = gson.toJson(this, Dataset.class);

        return ret;
    }

    public synchronized static Dataset fromJson(String name) throws FileNotFoundException {
        String path = "/home/equipo/Documentos/TrabajoGradoRGS/Datasets" + "/json/" + name + ".json";
        BufferedReader reader = new BufferedReader(new FileReader(path));
        Gson gson = new Gson();
        JSonDataset jsonDataset = gson.fromJson(reader, JSonDataset.class);

        Dataset ret = new Dataset();
        Attribute[] attrs = new Attribute[jsonDataset.getAttributes().size()];
        int index = 0;

        for (JsonAttribute attribute : jsonDataset.getAttributes()) {
            Attribute tmp = new Attribute();
            tmp.setName(attribute.getName());
            tmp.setType(getAttrType(attribute.getType()));
            if (tmp.getType() == Dataset.CLASS) {
                ret.setClassIndex(index);
                ret.setHasClass(true);
            }
            attrs[index] = tmp;
            index++;
        }
        ret.setAttributes(attrs);
        index = 0;
        Record[] records = new Record[jsonDataset.getData().size()];
        for (List<Object> list : jsonDataset.getData()) {
            Object[] tmpData = new Object[list.size()];
            tmpData = list.toArray();
            if (ret.hasClass) {
                if (tmpData[ret.classIndex] instanceof Double) {
                    tmpData[ret.classIndex] = Double.toString((Double) tmpData[ret.classIndex]);
                } else {
                    tmpData[ret.classIndex] = (String) tmpData[ret.classIndex];
                }
            }
            Record record = new Record(index, tmpData, attrs);
            records[index] = record;
            index++;
        }

        ret.setoRecords(records);

        //Valores normalización
        double[][] norm = new double[ret.getAttributes().length][2];
        for (int i = 0; i < ret.getAttributes().length; i++) {
            if (attrs[i].getType() == Dataset.DOUBLE) {
                norm[i][0] = norm[i][1] = (double) records[0].getData()[i];
            }
        }

        for (int i = 0; i < ret.getAttributes().length; i++) {
            if (attrs[i].getType() == Dataset.DOUBLE) {
                for (int j = 0; j < records.length; j++) {
                    norm[i][0] = Math.min((double) records[j].getData()[i], norm[i][0]);
                    norm[i][1] = Math.max((double) records[j].getData()[i], norm[i][1]);
                }
            }
        }

        if (ret.hasClass) {
            Map<String, Integer> classMap = new LinkedHashMap<>();
            int cont = 0;
            for (Record record : records) {
                if (!classMap.containsKey(record.getData()[ret.classIndex])) {
                    Object tmpData = record.getData()[ret.classIndex];
                    if (tmpData instanceof Double) {
                        classMap.put(Double.toString((Double) record.getData()[ret.classIndex]), cont);
                    } else {
                        classMap.put((String) record.getData()[ret.classIndex], cont);
                    }
                    //classMap.put((String) record.getData()[ret.classIndex], cont);
                    cont++;
                }
            }
            ret.classes = new String[classMap.size()];
            Object[] m = classMap.keySet().toArray();
            for (int i = 0; i < m.length; i++) {
                ret.classes[i] = (String) m[i];
            }
        }

        ret.setNormValues(norm);
        ret.setN(records.length);
        ret.normalize();
        ret.setAttributes(attrs);
        ret.setName(name);

        return ret;
    }

    public synchronized void normalize() {
        int rows = n;
        int cols = this.attributes.length;
        Object tempD[];
        records = new Record[rows];
        if (!normalized) {
            for (int i = 0; i < rows; i++) {
                tempD = new Object[cols];
                for (int j = 0; j < cols; j++) {
                    if (attributes[j].getType() == Dataset.DOUBLE) {
                        if (this.normValues[j][0] == this.normValues[j][1]) {
                            tempD[j] = 1.0;
                        } else {
                            tempD[j]
                                    = ((Double) oRecords[i].getData()[j] - this.normValues[j][0])
                                    / (this.normValues[j][1] - this.normValues[j][0]);
                        }
                    } else {
                        tempD[j] = oRecords[i].getData()[j];
                    }
                }
                this.records[i] = new Record(i, tempD, attributes);
            }
        }
        this.normalized = true;
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

    public synchronized static String getAttrType(int t) {
        switch (t) {
            case Dataset.CLASS:
                return "class";
            case Dataset.DOUBLE:
                return "double";
            case Dataset.STRING:
                return "string";
        }
        return "?";
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static class JsonAttribute {

        private String name;
        private String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }

    public static class JSonDataset {

        private List<JsonAttribute> attributes;
        private List<List<Object>> data;
        private String name;

        public List<JsonAttribute> getAttributes() {
            return attributes;
        }

        public void setAttributes(List<JsonAttribute> attributes) {
            this.attributes = attributes;
        }

        public List<List<Object>> getData() {
            return data;
        }

        public void setData(List<List<Object>> data) {
            this.data = data;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
