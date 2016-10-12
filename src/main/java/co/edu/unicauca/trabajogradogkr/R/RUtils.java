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
package co.edu.unicauca.trabajogradogkr.R;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class RUtils {

    public static List<RKmeansOutput> read(String file) throws FileNotFoundException {
        Gson gson = new Gson();
        //Lis<RKmeansOutput> ret = 
        Type resultType = new TypeToken<List<RKmeansOutput>>() {
        }.getType();

        List<RKmeansOutput> list = gson.fromJson(new FileReader(file), resultType);

        return list;
    }

}
