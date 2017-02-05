/*
 * Copyright (C) 2017 Pivotal Software, Inc..
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
package co.edu.unicauca.trabajogradogkr.model.filter;

import co.edu.unicauca.trabajogradogkr.model.Attribute;
import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.model.Record;
import co.edu.unicauca.trabajogradogkr.service.DatasetService;
import java.util.ArrayList;
import java.util.List;

/**
 * Filtrar por StdDev
 *
 * @author Arnold Jair Jimenez Vargas arnoldjair@hotmail.com
 */
public class StdDevFilter implements Filter {

    @Override
    public Dataset filter(Dataset dataset, double value) throws Exception {

        if (!dataset.isNormalized()) {
            throw new Exception("El dataset debe estar normalizado");
        }

        Object[] data = new Object[dataset.getAttributes().length];

        for (int i = 0; i < dataset.getAttributes().length; i++) {
            data[i] = 0.0;
        }
        Record means = new Record(-1, data.clone(), dataset.getAttributes());
        Record stdDev = new Record(-1, data.clone(), dataset.getAttributes());
        Record[] records = new Record[dataset.getN()];

        for (int i = 0; i < dataset.getN(); i++) {
            Record currRecord = dataset.getRecord(i);
            means = means.add(currRecord);
        }

        means = means.divide(dataset.getN());
        Record tmp;

        for (int i = 0; i < dataset.getN(); i++) {
            Record currRecord = dataset.getRecord(i);
            tmp = currRecord.subtract(means);
            tmp = tmp.pow(2);
            stdDev = stdDev.add(tmp);
        }

        //Varianza
        stdDev = stdDev.divide(dataset.getN() - 1);

        //Desviación estándar
        stdDev = stdDev.pow(0.5);

        Dataset ret = new Dataset();

        List<Attribute> attrs = new ArrayList<>();
        for (int i = 0; i < dataset.getAttributes().length; i++) {
            if (dataset.getAttributes()[i].getType() == DatasetService.DOUBLE) {
                if ((double) stdDev.getData()[i] >= value) {
                    attrs.add(dataset.getAttributes()[i]);
                }
            } else {
                attrs.add(dataset.getAttributes()[i]);
            }
        }

        Attribute[] attributes = new Attribute[attrs.size()];
        //ret.setAttributes((Attribute[]) attrs.toArray());
        for (int i = 0; i < attrs.size(); i++) {
            attributes[i] = attrs.get(i);
        }
        ret.setAttributes(attributes);

        //La nueva pos de classIndex.
        for (int i = 0; i < ret.getAttributes().length; i++) {
            if (ret.getAttributes()[i].getType() == DatasetService.CLASS) {
                ret.setClassIndex(i);
                ret.setHasClass(true);
            }
        }

        /**
         * Fijar a cada registro los datos de acuerdo al valor del filtro.
         */
        for (int i = 0; i < dataset.getN(); i++) {
            List<Object> newData = new ArrayList<>();
            Record currRecord = dataset.getRecord(i);

            for (int j = 0; j < dataset.getAttributes().length; j++) {
                if (dataset.getAttributes()[j].getType() == DatasetService.DOUBLE) {
                    if ((double) stdDev.getData()[j] >= value) {
                        newData.add(currRecord.getData()[j]);
                    }
                } else {
                    newData.add(currRecord.getData()[j]);
                }
            }
            Record newRecord = new Record(i, newData.toArray(), ret.getAttributes());
            records[i] = newRecord;
        }

        ret.setRecords(records);
        return ret;
    }

}
