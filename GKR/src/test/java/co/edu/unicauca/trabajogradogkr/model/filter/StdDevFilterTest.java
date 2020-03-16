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

import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.service.Config;
import co.edu.unicauca.trabajogradogkr.service.DatasetServiceImpl;
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
public class StdDevFilterTest {

    public StdDevFilterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        Config.getInstance().setConfig("datasetsPath", "/home/equipo/Desarrollo/trabajoGrado/Datasets/json/");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of filter method, of class StdDevFilter.
     */
    @Test
    public void testFilter() throws Exception {
        System.out.println("filter");
        String name = "dataset";
        DatasetServiceImpl datasetService = new DatasetServiceImpl();
        Dataset dataset = datasetService.byName(name, true);
        String filter = "stdDev";
        double value = 0.3;
        StdDevFilter instance = new StdDevFilter();
        Dataset expResult = null;
        Dataset result = instance.filter(dataset, value);
        assertNotEquals(expResult, result);
    }

}
