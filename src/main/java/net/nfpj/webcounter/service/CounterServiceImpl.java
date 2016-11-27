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

import javax.inject.Inject;
import javax.inject.Named;
import net.nfpj.webcounter.dm.CounterDM;
import net.nfpj.webcounter.exceptions.DuplicatedCounterException;
import net.nfpj.webcounter.model.Counter;
import org.springframework.stereotype.Service;

/**
 *
 * @author njacinto
 */
@Service("defaultCounterService")
public class CounterServiceImpl implements CounterService {
    
    private final CounterDM counterDM;

    @Inject
    public CounterServiceImpl(@Named("memCounterDM") CounterDM counterDM) {
        if(counterDM==null){
            throw new NullPointerException("CounterDM cannot be null");
        }
        this.counterDM = counterDM;
    }

    @Override
    public int count() {
        return counterDM.count();
    }

    @Override
    public boolean contains(String name) {
        return counterDM.contains(name);
    }
    
    @Override
    public Iterable<Counter> getAll(){
        return counterDM.getAll();
    }
    
    @Override
    public Counter get(String name) {
        return counterDM.get(name);
    }

    @Override
    public Counter create(String name) throws DuplicatedCounterException {
        return counterDM.create(name);
    }

    @Override
    public Counter remove(String name) {
        return counterDM.remove(name);
    }

    @Override
    public Counter increment(String name) {
        return counterDM.increment(name);
    }

    @Override
    public Counter increment(String name, boolean createIfNotExist) {
        return counterDM.increment(name, createIfNotExist);
    }
    
}
