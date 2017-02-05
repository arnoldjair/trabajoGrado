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
package co.edu.unicauca.trabajogradogkr.model.distance;

import co.edu.unicauca.trabajogradogkr.model.Attribute;
import co.edu.unicauca.trabajogradogkr.model.Record;
import co.edu.unicauca.trabajogradogkr.service.DatasetService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Arnold Jair Jimenez Vargas arnoldjair@hotmail.com
 */
public class ManhattanDistanceTest {

    public ManhattanDistanceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of distance method, of class ManhattanDistance.
     */
    @Test
    public void testDistance() {
        System.out.println("distance");
        Object[] data1 = new Object[]{1.0, 2.0, 3.0, 4.0};
        Object[] data2 = new Object[]{4.0, 3.0, 2.0, 1.0};
        Attribute[] attribs = new Attribute[4];
        for (int i = 0; i < 4; i++) {
            attribs[i] = new Attribute(Integer.toString(i), DatasetService.DOUBLE);
        }
        Record r1 = new Record(0, data1, attribs);
        Record r2 = new Record(1, data2, attribs);
        ManhattanDistance instance = new ManhattanDistance();
        double expResult = 8;
        double result = instance.distance(r1, r2);
        assertEquals(expResult, result, 1e-4);
    }

}
