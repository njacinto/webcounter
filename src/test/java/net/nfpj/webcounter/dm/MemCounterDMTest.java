/*
 * Copyright (C) 2016 njacinto.
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
package net.nfpj.webcounter.dm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.nfpj.webcounter.exceptions.DuplicatedCounterException;
import net.nfpj.webcounter.model.Counter;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author njacinto
 */
public class MemCounterDMTest {
    
    public MemCounterDMTest() {
    }
    
    @Test
    public void testCount() throws DuplicatedCounterException {
        MemCounterDM instance = new MemCounterDM();
        instance.create("C1");
        int result = instance.count();
        assertEquals(1, result);
    }
    
    @Test
    public void testCountEmpty() {
        MemCounterDM instance = new MemCounterDM();
        int result = instance.count();
        assertEquals(0, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testContainsNullName() {
        String name = null;
        MemCounterDM instance = new MemCounterDM();
        boolean result = instance.contains(name);
    }
    
    @Test
    public void testContainsMissing() {
        String name = "C1";
        MemCounterDM instance = new MemCounterDM();
        boolean result = instance.contains(name);
        assertFalse(result);
    }
    
    @Test
    public void testContains() throws DuplicatedCounterException {
        String name = "C1";
        MemCounterDM instance = new MemCounterDM();
        instance.create(name);
        boolean result = instance.contains(name);
        assertTrue(result);
    }
    
    /**
     * Test of getAll method, of class MemCounterDM.
     */
    @Test
    public void testGetAllEmpty() {
        MemCounterDM instance = new MemCounterDM();
        Iterable<Counter> result = instance.getAll();
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
    }
    
    @Test
    public void testGetAll() throws DuplicatedCounterException {
        String[] counters = new String[]{"C1", "C2", "C3"};
        int[] incs = new int[]{1, 2, 3};
        MemCounterDM instance = new MemCounterDM();
        List<Counter> expected = new ArrayList<>();
        for(int i=0; i<counters.length; i++){
            expected.add(new Counter(counters[i], incs[i]));
            instance.create(counters[i]);
            for(int j=0; j<incs[i]; j++){
                instance.increment(counters[i]);
            }
        }
        Iterable<Counter> result = instance.getAll();
        assertNotNull(result);
        assertEquals(expected.size(), instance.count());
        Iterator<Counter> it = result.iterator();
        while(it.hasNext()){
            Counter c = it.next();
            assertTrue("Not found: "+c.toString(), expected.contains(c));
        }
    }

    /**
     * Test of get method, of class MemCounterDM.
     */
    @Test
    public void testGetEmpty() {
        String name = "C1";
        MemCounterDM instance = new MemCounterDM();
        Counter expResult = null;
        Counter result = instance.get(name);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGet() throws DuplicatedCounterException {
        String name = "C1";
        MemCounterDM instance = new MemCounterDM();
        Counter expResult = new Counter(name, 0);
        instance.create(name);
        Counter result = instance.get(name);
        assertEquals(expResult, result);
    }

    /**
     * Test of reset method, of class MemCounterDM.
     */
    @Test
    public void testReset() throws DuplicatedCounterException {
        String name = "C1";
        MemCounterDM instance = new MemCounterDM();
        Counter expected = new Counter(name, 0);
        instance.increment(name, true);
        assertEquals(expected, instance.reset(name));
    }
    
    @Test
    public void testResetMissing() {
        String name = "C1";
        MemCounterDM instance = new MemCounterDM();
        assertNull(instance.reset(name));
    }

    /**
     * Test of increment method, of class MemCounterDM.
     */
    @Test
    public void testIncrement() throws DuplicatedCounterException {
        String name = "C1";
        MemCounterDM instance = new MemCounterDM();
        Counter expResult = new Counter(name, 1);
        instance.create(name);
        Counter result = instance.increment(name);
        assertEquals(expResult, result);
        expResult = new Counter(name, 2);
        result = instance.increment(name);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testIncrementMissing() {
        String name = "C1";
        MemCounterDM instance = new MemCounterDM();
        Counter expResult = null;
        Counter result = instance.increment(name);
        assertEquals(expResult, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testIncrementNullName() {
        String name = null;
        MemCounterDM instance = new MemCounterDM();
        Counter result = instance.increment(name);
    }

    /**
     * Test of increment method, of class MemCounterDM.
     */
    @Test
    public void testDontCreateAndIncrementMissing() {
        String name = "C1";
        boolean createIfNotExist = false;
        MemCounterDM instance = new MemCounterDM();
        Counter expResult = null;
        Counter result = instance.increment(name, createIfNotExist);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testDontCreateAndIncrement() throws DuplicatedCounterException {
        String name = "C1";
        boolean createIfNotExist = false;
        MemCounterDM instance = new MemCounterDM();
        instance.create(name);
        Counter expResult = new Counter(name, 1);
        Counter result = instance.increment(name, createIfNotExist);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCreateAndIncrement() {
        String name = "C1";
        boolean createIfNotExist = true;
        MemCounterDM instance = new MemCounterDM();
        Counter expResult = new Counter(name, 1);
        Counter result = instance.increment(name, createIfNotExist);
        assertEquals(expResult, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testCreateAndIncrementNullName() {
        String name = null;
        boolean createIfNotExist = true;
        MemCounterDM instance = new MemCounterDM();
        Counter result = instance.increment(name, createIfNotExist);
    }

    /**
     * Test of create method, of class MemCounterDM.
     */
    @Test
    public void testCreate() throws DuplicatedCounterException {
        String name = "C1";
        MemCounterDM instance = new MemCounterDM();
        Counter expResult = new Counter(name, 0);
        Counter result = instance.create(name);
        assertEquals(expResult, result);
    }
    
    @Test(expected = DuplicatedCounterException.class)
    public void testCreateExisting() throws DuplicatedCounterException {
        String name = "C1";
        MemCounterDM instance = new MemCounterDM();
        instance.create(name);
        instance.create(name);
    }
    
    @Test(expected = NullPointerException.class)
    public void testCreateNull() throws DuplicatedCounterException {
        String name = null;
        MemCounterDM instance = new MemCounterDM();
        Counter result = instance.create(name);
    }

    /**
     * Test of remove method, of class MemCounterDM.
     */
    @Test
    public void testRemove() {
        String name = "C1";
        MemCounterDM instance = new MemCounterDM();
        Counter expResult = new Counter(name, 1);
        instance.increment(name, true);
        Counter result = instance.remove(name);
        assertEquals(expResult, result);
        result = instance.remove(name);
        assertNull(result);
    }
    
    @Test
    public void testRemoveMissing() {
        String name = "C1";
        MemCounterDM instance = new MemCounterDM();
        Counter expResult = null;
        Counter result = instance.remove(name);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRemoveNull() {
        String name = null;
        MemCounterDM instance = new MemCounterDM();
        Counter result = instance.remove(name);
        assertNull(result);
    }
}
