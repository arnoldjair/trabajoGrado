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
package co.edu.unicauca.trabajogradogkr.web.api;

import co.edu.unicauca.trabajogradogkr.model.Dataset;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import co.edu.unicauca.trabajogradogkr.web.service.JsonDatasetService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
@RestController
@RequestMapping("/dataset")
public class DatasetApi {

    @Autowired
    JsonDatasetService jsonDatasetService;

    @RequestMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> upload(
            @RequestParam(value = "dataset", required = true) MultipartFile multipartFile) {

        Map<String, Object> retorno = new HashMap<>();

        try {
            Dataset dataset = new Dataset();
            dataset.fromFile(multipartFile);

            Dataset.JSonDataset tmp = jsonDatasetService.findByName(dataset.getName());
            if (tmp != null) {
                retorno.put("dataset", tmp.getData());
                retorno.put("message", "Dataset existente");
                return new ResponseEntity<>(retorno, HttpStatus.OK);
            }

            String json = dataset.toGson();
            Dataset.JSonDataset jsonDataset = new Dataset.JSonDataset();
            jsonDataset.setName(dataset.getName());
            //jsonDataset.setData(json);

            jsonDataset = jsonDatasetService.saveDataset(jsonDataset);

            /*File file = new File(dataset.getName() + ".json");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(json);
            writer.close();
             */
            retorno.put("dataset", jsonDataset.getData());
            retorno.put("message", "Dataset agregado");

            return new ResponseEntity<>(retorno, HttpStatus.OK);

        } catch (Exception ex) {
            Logger.getLogger(DatasetApi.class.getName()).log(Level.SEVERE, null, ex);
            retorno.put("message", ex.getMessage());
            return new ResponseEntity<>(retorno, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "datasetList")
    public ResponseEntity<Map<String, Object>> getDatasets() {
        List<String> datasets = jsonDatasetService.getDatasetsNames();
        Map<String, Object> retorno = new HashMap<>();
        retorno.put("datasets", datasets);
        return new ResponseEntity<>(retorno, HttpStatus.OK);
    }
}
