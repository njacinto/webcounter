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

import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import net.nfpj.webcounter.Application;
import net.nfpj.webcounter.api.model.CounterCreationRequest;
import net.nfpj.webcounter.exceptions.DuplicatedCounterException;
import net.nfpj.webcounter.model.Counter;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.springframework.http.HttpMethod;

/**
 *
 * @author nuno
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes =  Application.class, 
                webEnvironment=WebEnvironment.RANDOM_PORT)
public class CounterIT {
    @Autowired
    private TestRestTemplate restTemplate;
    
    public CounterIT() {
    }
    
    /**
     * Test of health method, of class TestService.
     */
    @Test
    public void testStatus() {
        ResponseEntity<String> entity = 
                restTemplate.getForEntity("/counter/status", String.class);
        assertEquals("Unexpected response status: "+entity.getStatusCodeValue(), 
                200, entity.getStatusCodeValue());
        assertEquals("ok", entity.getBody());
    }

    /**
     * Test of getCounter method, of class CounterRestService.
     */
    @Test
    public void testGetCounter() {
        String name = "C1Get";
        createCounter(name);
        ResponseEntity<Counter> entity = 
                restTemplate.getForEntity("/counter/"+name+"/", Counter.class);
        assertEquals("Unexpected response status: "+entity.getStatusCodeValue(), 
                200, entity.getStatusCodeValue());
        assertEquals(new Counter(name, 0), entity.getBody());
    }
    
    @Test
    public void testGetCounterMissing() {
        String name = "C1Missing";
        ResponseEntity<String> entity = 
                restTemplate.getForEntity("/counter/"+name+"/", String.class);
        assertEquals("Unexpected response status: "+entity.getStatusCodeValue(), 
                HttpStatus.SC_NOT_FOUND, entity.getStatusCodeValue());
    }

    /**
     * Test of getCounters method, of class CounterRestService.
     */
    @Test
    public void testGetCounters() {
        String[] countersName = new String[]{ "C1List", "C2List", "C3List" };
        for(String name : countersName){
            createCounter(name);
        }
        ResponseEntity<Counter[]> entity = 
                restTemplate.getForEntity("/counter/list/", Counter[].class);
        assertEquals("Unexpected response status: "+entity.getStatusCodeValue(), 
                200, entity.getStatusCodeValue());
        Counter[] counters = entity.getBody();
        assertTrue(countersName.length<=counters.length);
        Arrays.sort(countersName, (n1, n2) -> n1.compareTo(n2));
        Arrays.sort(counters, (c1, c2) -> c1.getName().compareTo(c2.getName()));
        int countEquals = 0;
        for(int i=0, j=0; i<countersName.length; ){
            if(countersName[i].equals(counters[j].getName())){
                countEquals++;
                i++;
            }
            j++;
        }
        assertEquals(countersName.length, countEquals);
    }

    /**
     * Test of createCounter method, of class CounterRestService.
     */
    @Test
    public void testCreateCounter() {
        String name = "C1Create";
        ResponseEntity<Counter> entity = 
                restTemplate.postForEntity("/counter/create/", new CounterCreationRequest(name), Counter.class);
        assertEquals("Unexpected response status: "+entity.getStatusCodeValue(), 
                201, entity.getStatusCodeValue());
        assertEquals(new Counter(name, 0), entity.getBody());
    }
    
    @Test
    public void testCreateCounterExisting() throws DuplicatedCounterException {
        String name = "C1CreateExist";
        createCounter(name);
        ResponseEntity<String> entity = 
                restTemplate.postForEntity("/counter/create/", new CounterCreationRequest(name), String.class);
        assertEquals("Unexpected response status: "+entity.getStatusCodeValue(), 
                HttpStatus.SC_BAD_REQUEST, entity.getStatusCodeValue());
    }

    /**
     * Test of createIncCounter method, of class CounterRestService.
     */
    @Test
    public void testCreateIncCounter() {
        String name = "C1CreateInc";
        ResponseEntity<Counter> entity = 
                restTemplate.postForEntity("/counter/"+name+"/", "", Counter.class);
        assertEquals("Unexpected response status: "+entity.getStatusCodeValue(), 
                200, entity.getStatusCodeValue());
        assertEquals(new Counter(name, 1), entity.getBody());
    }

    /**
     * Test of incCounter method, of class CounterRestService.
     */
    @Test
    public void testIncCounter() {
        String name = "C1Inc";
        createCounter(name);
        ResponseEntity<Counter> entity = restTemplate.exchange("/counter/{name}/", HttpMethod.PUT, 
                        null, Counter.class, name);
        assertEquals("Unexpected response status: "+entity.getStatusCodeValue(), 
                200, entity.getStatusCodeValue());
        assertEquals(new Counter(name, 1), entity.getBody());
    }
    
    @Test
    public void testIncCounterMissing() {
        String name = "C1IncMissing";
        ResponseEntity<Counter> entity = 
                restTemplate.exchange("/counter/{name}/", HttpMethod.PUT, 
                        null, Counter.class, name);
        assertEquals("Unexpected response status: "+entity.getStatusCodeValue(), 
                HttpStatus.SC_NOT_FOUND, entity.getStatusCodeValue());
    }
    
    
    private void createCounter(String name) {
        ResponseEntity<Counter> entity = 
                restTemplate.postForEntity("/counter/create", new CounterCreationRequest(name), Counter.class);
        assertEquals("Unexpected response status: "+entity.getStatusCodeValue(), 
                HttpStatus.SC_CREATED, entity.getStatusCodeValue());
        assertEquals(new Counter(name, 0), entity.getBody());
    }
}
