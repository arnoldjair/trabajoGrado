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

import java.io.File;
import java.security.SecureRandom;
import java.util.Random;
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
public class ConfigTest {

    public ConfigTest() {
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
     * Test of getInstance method, of class Config.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        Config expResult = null;
        Config result = Config.getInstance();
        assertNotEquals(expResult, result);
    }

    /**
     * Test of initResultFolder method, of class Config.
     */
    @Test
    public void testInitResultFolder() {
        System.out.println("initResultFolder");
        Config instance = Config.getInstance();
        instance.initResultFolder();
    }

    /**
     * Test of getResultFolder method, of class Config.
     */
    @Test
    public void testGetResultFolder() {
        System.out.println("getResultFolder");
        Config instance = Config.getInstance();
        File expResult = null;
        File result = instance.getResultFolder();
        assertNotEquals(expResult, result);
    }

    /**
     * Test of setRandom method, of class Config.
     */
    @Test
    public void testSetRandom() {
        System.out.println("setRandom");
        Random random = new SecureRandom();
        Config instance = Config.getInstance();
        instance.setRandom(random);
    }

    /**
     * Test of getRandom method, of class Config.
     */
    @Test
    public void testGetRandom() {
        System.out.println("getRandom");
        Config instance = Config.getInstance();
        Random expResult = null;
        Random result = instance.getRandom();
        assertNotEquals(expResult, result);
    }

    /**
     * Test of getConfig method, of class Config.
     */
    @Test
    public void testGetConfig() {
        System.out.println("getConfig");
        String config = "/home/equipo/Documentos/TrabajoGradoRGS/Codigo/TrabajoGradoGKR/Código/Datasets/json";
        Config instance = Config.getInstance();
        instance.setConfig("datasetsPath", "/home/equipo/Documentos/TrabajoGradoRGS/Codigo/TrabajoGradoGKR/Código/Datasets/json");
        String expResult = "/home/equipo/Documentos/TrabajoGradoRGS/Codigo/TrabajoGradoGKR/Código/Datasets/json";
        String result = instance.getConfig("datasetsPath");
        assertEquals(expResult, result);
    }

    /**
     * Test of setConfig method, of class Config.
     */
    @Test
    public void testSetConfig() {
        System.out.println("setConfig");
        String key = "datasetsPath";
        String value = "/home/equipo/Documentos/TrabajoGradoRGS/Codigo/TrabajoGradoGKR/Código/Datasets/json";
        Config instance = Config.getInstance();
        instance.setConfig(key, value);
    }

}
