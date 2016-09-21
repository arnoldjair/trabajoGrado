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

import co.edu.unicauca.trabajogradogkr.web.model.Params;
import co.edu.unicauca.trabajogradogkr.web.model.GBHSTask;
import co.edu.unicauca.trabajogradogkr.web.service.GBHSService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
@RestController
@RequestMapping("/gbhs")
public class GBHSApi {

    @Autowired
    GBHSService gbhsService;

    @RequestMapping(value = "gbhsList")
    public ResponseEntity<Map<String, Object>> getDatasets() {
        List<String> algorithms = gbhsService.getAlgorithmsNames();
        Map<String, Object> retorno = new HashMap<>();
        retorno.put("algorithms", algorithms);
        return new ResponseEntity<>(retorno, HttpStatus.OK);
    }

    @RequestMapping(value = "functionList")
    public ResponseEntity<Map<String, Object>> getObjectiveFunctions() {
        List<String> objectiveFunctions = gbhsService.getObjectiveFunctionNames();
        Map<String, Object> retorno = new HashMap<>();
        retorno.put("objectiveFunctions", objectiveFunctions);
        retorno.put("message", "Listo");
        return new ResponseEntity<>(retorno, HttpStatus.OK);
    }

    @RequestMapping(value = "run")
    public ResponseEntity<Map<String, Object>> run(@RequestBody(required = true) Params params) {
        Map<String, Object> retorno = new HashMap<>();

        //TODO; Verificar que los campos de params sean correctos.
        params = gbhsService.addParams(params);
        if (params == null) {
            retorno.put("message", "Error almacenando los parámetros");
            return new ResponseEntity<>(retorno, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        GBHSTask task = new GBHSTask();
        task.setDone(false);
        task.setParams(params);
        task = gbhsService.addTask(task);
        if (task == null) {
            retorno.put("message", "Error creando la tarea");
            return new ResponseEntity<>(retorno, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        retorno.put("message", "Tarea guardada");
        retorno.put("taskId", task.getId());
        return new ResponseEntity<>(retorno, HttpStatus.OK);
    }

}
