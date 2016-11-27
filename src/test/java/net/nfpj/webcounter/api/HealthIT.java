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

import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import net.nfpj.webcounter.Application;
import org.junit.Test;

/**
 *
 * @author nuno
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes =  Application.class, 
                webEnvironment=WebEnvironment.RANDOM_PORT)
public class HealthIT {
    @Autowired
    private TestRestTemplate restTemplate;
    
    public HealthIT() {
    }
    
    /**
     * Test of health method.
     */
    @Test
    public void testHealthGet() {
        ResponseEntity<String> entity = 
                restTemplate.getForEntity("/health", String.class);
        assertEquals("Unexpected response status: "+entity.getStatusCodeValue(), 
                200, entity.getStatusCodeValue());
        assertEquals("{ \"status\": \"up\" }", entity.getBody());
    }
    
    @Test
    public void testHealthPost() {
        ResponseEntity<String> entity = 
                restTemplate.postForEntity("/health", "", String.class);
        assertEquals("Unexpected response status: "+entity.getStatusCodeValue(), 
                200, entity.getStatusCodeValue());
        assertEquals("{ \"status\": \"up\" }", entity.getBody());
    }
    
    @Test
    public void testNotFound() {
        ResponseEntity<String> entity = 
                restTemplate.getForEntity("/health/invalid/", String.class);
        System.out.println(entity.getBody());
        assertEquals("Unexpected response status: "+entity.getStatusCodeValue(), 
                404, entity.getStatusCodeValue());
    }
}
