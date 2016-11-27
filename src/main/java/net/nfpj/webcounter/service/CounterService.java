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

import net.nfpj.webcounter.exceptions.DuplicatedCounterException;
import net.nfpj.webcounter.model.Counter;

/**
 *
 * @author njacinto
 */
public interface CounterService {
    /**
     * 
     * @return number of counters
     */
    int count();
    /**
     * 
     * @param name the name of the counter
     * @return true if exists, false otherwise
     * @throws NullPointerException if name is null.
     */
    boolean contains(String name);
    /**
     * 
     * @return the list of all counters 
     */
    Iterable<Counter> getAll();
    /**
     * 
     * @param name the name of the counter
     * @return the counter or null if doesn't exists.
     * @throws NullPointerException if name is null.
     */
    Counter get(String name);
    /**
     * 
     * @param name the name of the counter 
     * @return the counter created
     * @throws NullPointerException if name is null.
     * @throws IllegalArgumentException if counter already exists
     */
    Counter create(String name) throws DuplicatedCounterException;
    /**
     * 
     * @param name the name of the counter
     * @return the counter removed if exists
     */
    Counter remove(String name);
    /**
     * 
     * @param name the name of the counter
     * @return the counter with the incremented value or null if doesn't exists
     * @throws NullPointerException if name is null.
     */
    Counter increment(String name);
    /**
     * 
     * @param name the name of the counter
     * @param createIfNotExist true if the counter should be created if doesn't exist
     * @return the counter with the incremented value or null if doesn't exists and
     *      it was not created
     * @throws NullPointerException if name is null.
     */
    Counter increment(String name, boolean createIfNotExist);
}
