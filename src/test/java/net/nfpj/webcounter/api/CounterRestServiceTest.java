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
package net.nfpj.webcounter.api;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Iterator;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import net.nfpj.webcounter.api.model.CounterCreationRequest;
import net.nfpj.webcounter.exceptions.DuplicatedCounterException;
import net.nfpj.webcounter.model.Counter;
import net.nfpj.webcounter.service.CounterService;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

/**
 *
 * @author njacinto
 */
public class CounterRestServiceTest {
    
    private CounterService counterService;
    
    public CounterRestServiceTest() {
    }

    
    @Before
    public void before(){
        counterService = Mockito.mock(CounterService.class);
    }
    
    /**
     * Test of ok method, of class CounterRestService.
     */
    @Test
    public void testOk() {
        CounterRestService instance = new CounterRestService(counterService);
        String expResult = "ok";
        String result = instance.ok();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCounter method, of class CounterRestService.
     */
    @Test
    public void testGetCounter() {
        String name = "C1";
        CounterRestService instance = new CounterRestService(counterService);
        Counter expResult = new Counter(name, 0);
        Mockito.when(counterService.get(name)).thenReturn(expResult);
        Counter result = instance.getCounter(name);
        assertEquals(expResult, result);
    }
    
    @Test(expected = NotFoundException.class)
    public void testGetCounterMissing() {
        String name = "C1";
        CounterRestService instance = new CounterRestService(counterService);
        Mockito.when(counterService.get(name)).thenReturn(null);
        Counter result = instance.getCounter(name);
    }

    /**
     * Test of getCounters method, of class CounterRestService.
     */
    @Test
    public void testGetCounters() {
        CounterRestService instance = new CounterRestService(counterService);
        Iterable<Counter> expResult = Arrays.asList(new Counter("C1", 1), new Counter("C2", 2));
        Mockito.when(counterService.getAll()).thenReturn(expResult);
        Iterator<Counter> result = instance.getCounters();
        Iterator<Counter> expectedIt = expResult.iterator();
        while(result.hasNext()){
            assertEquals(expectedIt.next(), result.next());
        }
    }
    
    @Test
    public void testGetCountersEmpty() {
        CounterRestService instance = new CounterRestService(counterService);
        Mockito.when(counterService.getAll()).thenReturn(Arrays.asList());
        Iterator<Counter> result = instance.getCounters();
        assertNotNull(result);
        assertFalse(result.hasNext());
    }

    /**
     * Test of createCounter method, of class CounterRestService.
     */
    @Test
    public void testCreateCounter() throws URISyntaxException, DuplicatedCounterException {
        String name = "C1";
        CounterCreationRequest counterCreate = new CounterCreationRequest(name);
        CounterRestService instance = new CounterRestService(counterService);
        Counter expResult = new Counter(name, 0);
        Mockito.when(counterService.create(name)).thenReturn(expResult);
        Response result = instance.createCounter(counterCreate);
        assertEquals(expResult, result.getEntity());
    }
    
    @Test(expected = BadRequestException.class)
    public void testCreateCounterExisting() throws URISyntaxException, DuplicatedCounterException {
        String name = "C1";
        CounterCreationRequest counterCreate = new CounterCreationRequest(name);
        CounterRestService instance = new CounterRestService(counterService);
        Counter expResult = new Counter(name, 0);
        Mockito.when(counterService.create(name)).thenThrow(DuplicatedCounterException.class);
        Response result = instance.createCounter(counterCreate);
        assertEquals(expResult, result.getEntity());
    }

    /**
     * Test of createIncCounter method, of class CounterRestService.
     */
    @Test
    public void testCreateIncCounter() {
        String name = "C1";
        CounterRestService instance = new CounterRestService(counterService);
        Counter expResult = new Counter(name, 1);
        Mockito.when(counterService.increment(name, true)).thenReturn(expResult);
        Counter result = instance.createIncCounter(name);
        assertEquals(expResult, result);
    }

    /**
     * Test of incCounter method, of class CounterRestService.
     */
    @Test
    public void testIncCounter() {
        String name = "C1";
        CounterRestService instance = new CounterRestService(counterService);
        Counter expResult = new Counter(name, 1);
        Mockito.when(counterService.increment(name)).thenReturn(expResult);
        Counter result = instance.incCounter(name);
        assertEquals(expResult, result);
    }
    
    @Test(expected = NotFoundException.class)
    public void testIncCounterMissing() {
        String name = "C1";
        CounterRestService instance = new CounterRestService(counterService);
        Mockito.when(counterService.increment(name)).thenReturn(null);
        Counter result = instance.incCounter(name);
    }
    
}
