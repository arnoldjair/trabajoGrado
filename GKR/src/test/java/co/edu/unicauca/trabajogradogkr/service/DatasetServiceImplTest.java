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

import co.edu.unicauca.trabajogradogkr.model.Dataset;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class DatasetServiceImplTest {

    public DatasetServiceImplTest() {
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
     * Test of getDatasets method, of class DatasetServiceImpl.
     */
    @Test
    public void testGetDatasets() {
        try {
            System.out.println("getDatasets");
            DatasetServiceImpl instance = new DatasetServiceImpl();
            List<Dataset> expResult = null;
            List<Dataset> result = instance.getDatasets(true);
            assertNotEquals(expResult, result);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatasetServiceImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of fromJson method, of class DatasetServiceImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testFromJson() throws Exception {
        System.out.println("fromJson");
        String name = Config.getInstance().getConfig("datasetsPath") + "/dataset.json";
        DatasetServiceImpl instance = new DatasetServiceImpl();
        Dataset expResult = null;
        Dataset result = instance.fromJson(name, true);
        assertNotEquals(expResult, result);
    }

    /**
     * Test of normalize method, of class DatasetServiceImpl.
     */
    @Test
    public void testNormalize() {
        try {
            System.out.println("normalize");
            String datasetName = "/home/equipo/Desarrollo/trabajoGrado/Datasets/json/iris.json";
            DatasetServiceImpl instance = new DatasetServiceImpl();
            Dataset dataset = instance.fromJson(datasetName, true);
            instance.normalize(dataset);
            System.out.println("");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatasetServiceImplTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
    }

    /**
     * Test of getAttrType method, of class DatasetServiceImpl.
     */
    @Test
    public void testGetAttrType() {
        System.out.println("getAttrType");
        String t = "class";
        DatasetServiceImpl instance = new DatasetServiceImpl();
        int expResult = DatasetService.CLASS;
        int result = instance.getAttrType(t);
        assertEquals(expResult, result);
    }

    /**
     * Test of byName method, of class DatasetServiceImpl.
     */
    @Test
    public void testByName() {
        System.out.println("byName");
        String name = "iris";
        DatasetServiceImpl instance = new DatasetServiceImpl();
        Dataset expResult = null;
        Dataset result = instance.byName(name, true);
        assertNotEquals(expResult, result);
    }
}
