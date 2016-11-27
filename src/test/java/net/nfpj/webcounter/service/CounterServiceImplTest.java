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
package net.nfpj.webcounter.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.nfpj.webcounter.dm.CounterDM;
import net.nfpj.webcounter.exceptions.DuplicatedCounterException;
import net.nfpj.webcounter.model.Counter;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.Mockito;

/**
 *
 * @author njacinto
 */
public class CounterServiceImplTest {
    
    private CounterDM counterDM;
    
    public CounterServiceImplTest() {
    }
    
    @Before
    public void before(){
        counterDM = Mockito.mock(CounterDM.class);
    }
    
    @Test
    public void testCount() {
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Mockito.when(counterDM.count()).thenReturn(1);
        int result = instance.count();
        assertEquals(1, result);
    }
    
    @Test
    public void testCountEmpty() {
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Mockito.when(counterDM.count()).thenReturn(0);
        int result = instance.count();
        assertEquals(0, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testContainsNullName() {
        String name = null;
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Mockito.when(counterDM.contains(name)).thenThrow(NullPointerException.class);
        boolean result = instance.contains(name);
    }
    
    @Test
    public void testContainsMissing() {
        String name = "C1";
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Mockito.when(counterDM.contains(name)).thenReturn(false);
        boolean result = instance.contains(name);
        assertFalse(result);
    }
    
    @Test
    public void testContains() {
        String name = "C1";
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Mockito.when(counterDM.contains(name)).thenReturn(true);
        boolean result = instance.contains(name);
        assertTrue(result);
    }
    
    /**
     * Test of getAll method, of class CounterServiceImpl.
     */
    @Test
    public void testGetAll() {
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        List<Counter> counters = Arrays.asList(
                new Counter("C1", 1), new Counter("C2", 2), new Counter("C3", 3));
        Mockito.when(counterDM.getAll()).thenReturn(counters);
        Iterable<Counter> result = instance.getAll();
        assertNotNull(result);
        Iterator<Counter> it = result.iterator();
        int count = 0;
        while(it.hasNext()){
            Counter c = it.next();
            assertTrue("Not found: "+c.toString(), counters.contains(c));
            count++;
        }
        assertEquals(counters.size(), count);
    }
    
    @Test
    public void testGetAllEmpty() {
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Mockito.when(counterDM.getAll()).thenReturn(Collections.EMPTY_LIST);
        Iterable<Counter> result = instance.getAll();
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
    }

    /**
     * Test of get method, of class CounterServiceImpl.
     */
    @Test
    public void testGet() {
        String name = "C1";
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Counter expResult = new Counter(name, 0);
        Mockito.when(counterDM.get(name)).thenReturn(expResult);
        Counter result = instance.get(name);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetMissing() {
        String name = "C1";
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Counter expResult = null;
        Mockito.when(counterDM.get(name)).thenReturn(expResult);
        Counter result = instance.get(name);
        assertEquals(expResult, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetNullName() {
        String name = null;
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Mockito.when(counterDM.get(name)).thenThrow(NullPointerException.class);
        Counter result = instance.get(name);
    }

    /**
     * Test of create method, of class CounterServiceImpl.
     */
    @Test
    public void testCreate() throws DuplicatedCounterException {
        String name = "C1";
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Counter expResult = new Counter(name, 0);
        Mockito.when(counterDM.create(name)).thenReturn(expResult);
        Counter result = instance.create(name);
        assertEquals(expResult, result);
    }
    
    @Test(expected = DuplicatedCounterException.class)
    public void testCreateExisting() throws DuplicatedCounterException {
        String name = "C1";
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Mockito.when(counterDM.create(name)).thenThrow(DuplicatedCounterException.class);
        Counter result = instance.create(name);
    }
    
    @Test(expected = NullPointerException.class)
    public void testCreateNullName() throws DuplicatedCounterException {
        String name = null;
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Mockito.when(counterDM.create(name)).thenThrow(NullPointerException.class);
        Counter result = instance.create(name);
    }

    /**
     * Test of remove method, of class CounterServiceImpl.
     */
    @Test
    public void testRemove() {
        String name = "C1";
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Counter expResult = new Counter(name, 0);
        Mockito.when(counterDM.remove(name)).thenReturn(expResult);
        Counter result = instance.remove(name);
        assertEquals(expResult, result);
        Mockito.when(counterDM.remove(name)).thenReturn(null);
        result = instance.remove(name);
        assertNull(result);
    }
    
    @Test
    public void testRemoveMissing() {
        String name = "C1";
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Counter expResult = null;
        Mockito.when(counterDM.remove(name)).thenReturn(expResult);
        Counter result = instance.remove(name);
        assertEquals(expResult, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testRemoveNullName() {
        String name = "";
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Counter expResult = null;
        Mockito.when(counterDM.remove(name)).thenThrow(NullPointerException.class);
        Counter result = instance.remove(name);
    }

    /**
     * Test of increment method, of class CounterServiceImpl.
     */
    @Test
    public void testIncrement() {
        String name = "C1";
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Counter expResult = new Counter(name, 1);
        Mockito.when(counterDM.increment(name)).thenReturn(expResult);
        Counter result = instance.increment(name);
        assertEquals(expResult, result);
    }
    @Test
    public void testIncrementMissing() {
        String name = "C1";
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Counter expResult = null;
        Mockito.when(counterDM.increment(name)).thenReturn(expResult);
        Counter result = instance.increment(name);
        assertEquals(expResult, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testIncrementNullName() {
        String name = null;
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Mockito.when(counterDM.increment(name)).thenThrow(NullPointerException.class);
        Counter result = instance.increment(name);
    }

    /**
     * Test of increment method, of class CounterServiceImpl.
     */
    @Test
    public void testDontCreateAndIncrementMissing() {
        String name = "C1";
        boolean createIfNotExist = false;
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Counter expResult = null;
        Mockito.when(counterDM.increment(name, createIfNotExist)).thenReturn(expResult);
        Counter result = instance.increment(name, createIfNotExist);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testDontCreateAndIncrement() {
        String name = "C1";
        boolean createIfNotExist = false;
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Counter expResult = new Counter(name, 1);
        Mockito.when(counterDM.increment(name, createIfNotExist)).thenReturn(expResult);
        Counter result = instance.increment(name, createIfNotExist);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCreateAndIncrement() {
        String name = "C1";
        boolean createIfNotExist = true;
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Counter expResult = new Counter(name, 1);
        Mockito.when(counterDM.increment(name, createIfNotExist)).thenReturn(expResult);
        Counter result = instance.increment(name, createIfNotExist);
        assertEquals(expResult, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testCreateAndIncrementNullName() {
        String name = null;
        boolean createIfNotExist = true;
        CounterServiceImpl instance = new CounterServiceImpl(counterDM);
        Mockito.when(counterDM.increment(name, createIfNotExist)).thenThrow(NullPointerException.class);
        Counter result = instance.increment(name, createIfNotExist);
    }
    
}
