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
package co.edu.unicauca.trabajogradogkr.distance;

import co.edu.unicauca.trabajogradogkr.model.Record;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class EuclideanDistance implements Distance {

    @Override
    public double distance(Record r1, Record r2) {
        double ret = 0;
        Record tmp = r1.subtract(r2);
        tmp = tmp.pow(2);
        ret = tmp.sumValues();
        ret = Math.sqrt(ret);
        return ret;
    }

    @Override
    public Distance newInstance() {
        return new EuclideanDistance();
    }

}
