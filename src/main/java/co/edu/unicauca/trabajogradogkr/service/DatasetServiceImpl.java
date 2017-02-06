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
package co.edu.unicauca.trabajogradogkr.service;

import co.edu.unicauca.trabajogradogkr.model.Attribute;
import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.model.JSONDataset;
import co.edu.unicauca.trabajogradogkr.model.Record;
import co.edu.unicauca.trabajogradogkr.model.filter.Filter;
import co.edu.unicauca.trabajogradogkr.model.filter.StdDevFilter;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arnold Jair Jimenez Vargas arnoldjair@hotmail.com
 */
public class DatasetServiceImpl implements DatasetService {

    @Override
    public List<Dataset> getDatasets(boolean normalize) throws FileNotFoundException {
        String datasetsPath = Config.getInstance().getConfig("datasetsPath");

        List<Dataset> datasets = new ArrayList<>();

        File file = new File(datasetsPath);
        if (!file.isDirectory()) {
            System.out.println("Directorio de datasets inválido");
            return null;
        }

        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        };

        File[] files = file.listFiles(filter);

        for (int i = 0; i < files.length; i++) {
            Dataset tmp = this.fromJson(files[i].getAbsolutePath(), normalize);
            datasets.add(tmp);
        }

        return datasets;
    }

    @Override
    public Dataset fromJson(String path, boolean normalize) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        Gson gson = new Gson();
        JSONDataset jsonDataset = gson.fromJson(reader, JSONDataset.class);

        Dataset ret = new Dataset();
        Attribute[] attrs = new Attribute[jsonDataset.getAttributes().size()];
        int index = 0;

        for (Dataset.JsonAttribute attribute : jsonDataset.getAttributes()) {
            Attribute tmp = new Attribute();
            tmp.setName(attribute.getName());
            tmp.setType(getAttrType(attribute.getType()));
            if (tmp.getType() == DatasetService.CLASS) {
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
            Object[] tmpData;// = new Object[list.size()];
            tmpData = list.toArray();
            if (ret.isHasClass()) {
                if (tmpData[ret.getClassIndex()] instanceof Double) {
                    tmpData[ret.getClassIndex()] = Double.toString((Double) tmpData[ret.getClassIndex()]);
                } else {
                    tmpData[ret.getClassIndex()] = (String) tmpData[ret.getClassIndex()];
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
            if (attrs[i].getType() == DatasetService.DOUBLE) {
                norm[i][0] = norm[i][1] = (double) records[0].getData()[i];
            }
        }

        for (int i = 0; i < ret.getAttributes().length; i++) {
            if (attrs[i].getType() == DatasetService.DOUBLE) {
                for (int j = 0; j < records.length; j++) {
                    norm[i][0] = Math.min((double) records[j].getData()[i], norm[i][0]);
                    norm[i][1] = Math.max((double) records[j].getData()[i], norm[i][1]);
                }
            }
        }

        if (ret.isHasClass()) {
            Map<String, Integer> classMap = new LinkedHashMap<>();
            int cont = 0;
            for (Record record : records) {
                if (!classMap.containsKey(record.getData()[ret.getClassIndex()])) {
                    Object tmpData = record.getData()[ret.getClassIndex()];
                    if (tmpData instanceof Double) {
                        classMap.put(Double.toString((Double) record.getData()[ret.getClassIndex()]), cont);
                    } else {
                        classMap.put((String) record.getData()[ret.getClassIndex()], cont);
                    }
                    //classMap.put((String) record.getData()[ret.classIndex], cont);
                    cont++;
                }
            }
            String[] classes = new String[classMap.size()];
            Object[] m = classMap.keySet().toArray();
            for (int i = 0; i < m.length; i++) {
                classes[i] = (String) m[i];
            }
            ret.setClasses(classes);
        }

        ret.setNormValues(norm);
        ret.setN(records.length);

        if (normalize) {
            System.out.println("Dataset normalizado");
            this.normalize(ret);
        } else {
            System.out.println("Dataset no normalizado");
            ret.setRecords(ret.getoRecords());
        }

        ret.setAttributes(attrs);
        ret.setName(jsonDataset.getName());

        return ret;
    }

    @Override
    public void normalize(Dataset dataset) {

        int rows = dataset.getN();
        int cols = dataset.getAttributes().length;
        Object tempD[];
        Record[] records = new Record[dataset.getN()];
        if (!dataset.isNormalized()) {
            for (int i = 0; i < rows; i++) {
                tempD = new Object[cols];
                for (int j = 0; j < cols; j++) {
                    if (dataset.getAttributes()[j].getType() == DatasetService.DOUBLE) {
                        if (dataset.getNormValues()[j][0] == dataset.getNormValues()[j][1]) {
                            tempD[j] = 1.0;
                        } else {
                            tempD[j]
                                    = ((Double) dataset.getoRecords()[i].getData()[j] - dataset.getNormValues()[j][0])
                                    / (dataset.getNormValues()[j][1] - dataset.getNormValues()[j][0]);
                        }
                    } else {
                        tempD[j] = dataset.getoRecords()[i].getData()[j];
                    }
                }
                records[i] = new Record(i, tempD, dataset.getAttributes());
            }
            dataset.setRecords(records);
        }
        dataset.setNormalized(true);
    }

    @Override
    public int getAttrType(String t) {
        switch (t) {
            case "class":
                return DatasetService.CLASS;
            case "double":
                return DatasetService.DOUBLE;
            case "string":
                return DatasetService.STRING;
        }
        return 0;
    }

    @Override
    public Dataset byName(String name, boolean normalize) {
        try {
            String datasetsPath = Config.getInstance().getConfig("datasetsPath") + "/" + name + ".json";
            return this.fromJson(datasetsPath, normalize);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatasetServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Dataset filter(Dataset dataset, String filter, Object value) {
        try {
            switch (filter) {
                case "stdDev":
                    Filter currFilter = new StdDevFilter();
                    return currFilter.filter(dataset, (double) value);
            }
            return null;
        } catch (Exception ex) {
            Logger.getLogger(DatasetServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
