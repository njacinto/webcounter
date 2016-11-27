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

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import net.nfpj.webcounter.exceptions.DuplicatedCounterException;
import net.nfpj.webcounter.model.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author njacinto
 */
@Component("memCounterDM")
public class MemCounterDM implements CounterDM {

    private static final Logger log = LoggerFactory.getLogger(MemCounterDM.class);

    private final ConcurrentMap<String, AtomicInteger> counters;

    public MemCounterDM() {
        counters = new ConcurrentHashMap<>();
    }

    @Override
    public int count() {
        return counters.size();
    }

    @Override
    public boolean contains(String name) {
        if (name == null || name.isEmpty()) {
            throw new NullPointerException("Name cannot be null");
        }
        return counters.containsKey(name);
    }
    
    @Override
    public Iterable<Counter> getAll() {
        return new CountersIterable();
    }

    @Override
    public Counter get(String name) {
        if (name == null || name.isEmpty()) {
            throw new NullPointerException("Name cannot be null");
        }
        AtomicInteger ai = counters.get(name);
        return (ai == null) ? null : new Counter(name, ai.get());
    }

    @Override
    public Counter reset(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new NullPointerException("Name cannot be null");
        }
        AtomicInteger ai = counters.get(name);
        if (ai != null) {
            ai.set(0);
            return new Counter(name, 0);
        } else {
            return null;
        }
    }

    @Override
    public Counter increment(String name) {
        if (name == null || name.isEmpty()) {
            throw new NullPointerException("Name cannot be null");
        }
        AtomicInteger ai = counters.get(name);
        return (ai == null) ? null : new Counter(name, ai.incrementAndGet());
    }

    @Override
    public Counter increment(String name, boolean createIfNotExist) {
        if (name == null || name.isEmpty()) {
            throw new NullPointerException("Name cannot be null");
        }
        AtomicInteger ai = counters.get(name);
        if (ai == null) {
            if (createIfNotExist) {
                AtomicInteger existing = counters.putIfAbsent(name, ai = new AtomicInteger(0));
                if (existing != null) {
                    ai = existing;
                }
            } else {
                return null;
            }
        }
        return new Counter(name, ai.incrementAndGet());
    }

    @Override
    public Counter create(String name) throws DuplicatedCounterException {
        if (name == null || name.isEmpty()) {
            throw new NullPointerException("Name cannot be null");
        }
        if(counters.putIfAbsent(name, new AtomicInteger(0))==null){
            return new Counter(name, 0);
        } else {
            throw new DuplicatedCounterException(name, "Counter with name "+name+" already exists");
        }
    }

    @Override
    public Counter remove(String name) {
        if (name != null && !name.isEmpty()) {
            AtomicInteger ai = counters.remove(name);
            return ai == null ? null : new Counter(name, ai.get());
        }
        return null;
    }

    private class CountersIterable implements Iterable<Counter> {

        public CountersIterable() {
        }

        @Override
        public Iterator<Counter> iterator() {
            return new CountersIterator(counters.entrySet().iterator());
        }
    }

    private static class CountersIterator implements Iterator<Counter> {

        private final Iterator<Map.Entry<String, AtomicInteger>> it;

        public CountersIterator(Iterator<Map.Entry<String, AtomicInteger>> it) {
            this.it = it;
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Counter next() {
            Map.Entry<String, AtomicInteger> entry = it.next();
            return new Counter(entry.getKey(), entry.getValue().get());
        }

    }
}
