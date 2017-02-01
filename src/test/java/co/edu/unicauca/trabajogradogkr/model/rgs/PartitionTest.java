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
package co.edu.unicauca.trabajogradogkr.model.rgs;

import co.edu.unicauca.trabajogradogkr.model.Dataset;
import co.edu.unicauca.trabajogradogkr.model.distance.Distance;
import co.edu.unicauca.trabajogradogkr.model.distance.EuclideanDistance;
import co.edu.unicauca.trabajogradogkr.service.Config;
import co.edu.unicauca.trabajogradogkr.service.DatasetServiceImpl;
import java.io.FileNotFoundException;
import java.security.SecureRandom;
import java.util.Random;
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
public class PartitionTest {

    public PartitionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        Config.getInstance().setConfig("datasetsPath", "/home/equipo/Documentos/TrabajoGradoRGS/Codigo/TrabajoGradoGKR/Código/Datasets/json");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of randPartition method, of class Partition.
     */
    @Test
    public void testRandPartition_int_int() {
        System.out.println("randPartition");
        int n = 10;
        int k = 3;
        Partition expResult = null;
        Partition result = Partition.randPartition(n, k);
        if (result == null) {
            fail("El resultado es nulo");
        }
        if (result.getK() > k) {
            fail("El valor de k obtenido es mayor que el deseado");
        }

        if (result.getN() != n) {
            fail("El valor de n obtenido es diferente al deseado");
        }
    }

    /**
     * Test of randPartition method, of class Partition.
     */
    @Test
    public void testRandPartition_3args() {
        System.out.println("randPartition");
        Random r1 = new Random(10);
        Random r2 = new Random(10);
        int n = 10;
        int k = 3;
        Partition p1 = Partition.randPartition(n, k, r1);
        Partition p2 = Partition.randPartition(n, k, r2);
        assertEquals(p1, p2);

    }

    /**
     * Test of reprocessRGS method, of class Partition.
     */
    @Test
    public void testReprocessRGS() {
        System.out.println("reprocessRGS");
        int[] oRGS = new int[]{1, 1, 1, 1, 2, 2, 0, 0, 0, 0};
        int[] eRGS = new int[]{0, 0, 0, 0, 1, 1, 2, 2, 2, 2};
        Partition result = new Partition(eRGS, 10, 3);
        Partition eResult = Partition.reprocessRGS(oRGS);
        assertEquals(result, eResult);
    }

    /**
     * Test of RandPartitionKmeanspp method, of class Partition.
     */
    @Test
    public void testRandPartitionKmeanspp() {
        try {
            System.out.println("RandPartitionKmeanspp");
            String datasetName = "/home/equipo/Documentos/TrabajoGradoRGS/Codigo/TrabajoGradoGKR/Código/Datasets/json/iris.json";
            DatasetServiceImpl instance = new DatasetServiceImpl();
            Dataset dataset = instance.fromJson(datasetName);
            instance.normalize(dataset);
            int n = dataset.getN();
            int k = 3;
            Distance distance = new EuclideanDistance();
            Random random = new SecureRandom();
            Partition expResult = null;
            Partition result = Partition.RandPartitionKmeanspp(k, dataset, distance, random);
            assertNotEquals(expResult, result);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PartitionTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
