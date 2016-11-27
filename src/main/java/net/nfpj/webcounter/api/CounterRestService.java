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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.nfpj.webcounter.api.model.CounterCreationRequest;
import net.nfpj.webcounter.exceptions.DuplicatedCounterException;
import net.nfpj.webcounter.model.Counter;
import net.nfpj.webcounter.service.CounterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author nuno
 */
@Component
@Path("/counter/")
public class CounterRestService {
    private static final Logger log = LoggerFactory.getLogger(CounterRestService.class);
    
    private CounterService counterService;

    @Inject
    public CounterRestService(@Named("defaultCounterService") CounterService counterService) {
        if(counterService==null){
            throw new NullPointerException("CounterService cannot be null");
        }
        this.counterService = counterService;
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/status")
    public String ok() {
        return "ok";
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{name: \\w+ }/")
    public Counter getCounter(@PathParam("name") String name) {
        log.trace("Get value request for counter {}", name);
        Counter counter = counterService.get(name);
        if(counter!=null){
            return counter;
        } else{
            log.info("No counter found with the name "+name);
            throw new NotFoundException("Counter doesn't exist.");
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list/")
    public Iterator<Counter> getCounters() {
        log.trace("Get counters request");
        return counterService.getAll().iterator();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("create/")
    public Response createCounter(@Valid CounterCreationRequest counterCreate) throws URISyntaxException {
        log.trace("Post create request for counter {}", counterCreate.getName());
        try {
            Counter counter = counterService.create(counterCreate.getName());
            return Response.created(new URI("/counter/"+counter.getName()+"/")).entity(counter).build();
        } catch(DuplicatedCounterException ex){
            throw new BadRequestException("Counter already exists");
        }
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{name: \\w+}")
    public Counter createIncCounter(@PathParam("name") String name) {
        log.trace("Post increment request for counter {}", name);
        return counterService.increment(name, true);
    }
    
    
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{name: \\w+}")
    public Counter incCounter(@PathParam("name") String name) {
        log.trace("Put increment request for counter {}", name);
        Counter counter = counterService.increment(name);
        if(counter!=null){
            return counter;
        } else {
            log.warn("No counter found with the name "+name);
            throw new NotFoundException("No counter found with the name "+name);
        }
    }
}
