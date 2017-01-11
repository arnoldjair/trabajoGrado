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
package co.edu.unicauca.trabajogradogkr.service.interfaces;

import co.edu.unicauca.trabajogradogkr.model.Dataset;
import java.io.FileNotFoundException;
import java.util.List;

/**
 *
 * @author Arnold Jair Jimenez Vargas arnoldjair@hotmail.com
 */
public interface DatasetService {

    public static final int DOUBLE = 1;
    public static final int STRING = 2;
    public static final int CLASS = 3;

    public List<Dataset> getDatasets() throws FileNotFoundException;

    public Dataset fromJson(String path) throws FileNotFoundException;

    public void normalize(Dataset dataset);
    
    public int getAttrType(String t);
    
    public Dataset byName(String name);

}
