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

import co.edu.unicauca.trabajogradogkr.web.model.Params;
import co.edu.unicauca.trabajogradogkr.repository.ParamsRepository;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
@Component("gbhsService")
@Transactional
public class GBHSServiceImpl implements GBHSService {

    @Autowired
    ParamsRepository paramsRepository;

    @Override
    public List<String> getAlgorithmsNames() {
        List<String> retorno = new ArrayList<>();
        retorno.add("Records");
        retorno.add("Centroids");
        retorno.add("Groups");
        return retorno;
    }

    @Override
    public List<String> getObjectiveFunctionNames() {
        List<String> retorno = new ArrayList<>();
        retorno.add("AIC");
        retorno.add("BIC");
        retorno.add("CHI");
        return retorno;
    }

    @Override
    public co.edu.unicauca.trabajogradogkr.web.model.Params addParams(Params params) {
        return paramsRepository.save(params);
    }

}
