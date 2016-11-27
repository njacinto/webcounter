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
package net.nfpj.webcounter.api.errorhandling;

import net.nfpj.webcounter.api.model.MessageResponse;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP 400 Handler
 * 
 * @author nuno
 */
@Provider
public class BadRequestHandler implements ExceptionMapper<BadRequestException> {
    private static final Logger log = LoggerFactory.getLogger(BadRequestHandler.class);

    @Override
    public Response toResponse(BadRequestException exception) {
        log.warn("Bad request error catched: "+exception.getMessage(), exception);
        //
        MessageResponse response = new MessageResponse("");
		return Response.status(BAD_REQUEST).entity(response).build();
    }
    
}
