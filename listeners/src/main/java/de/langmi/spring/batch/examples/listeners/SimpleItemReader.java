/*
 * Copyright 2011 Michael R. Lange <michael.r.lange@langmi.de>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.langmi.spring.batch.examples.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * SimpleItemReader - is not threadsafe, uses in memory testdata.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class SimpleItemReader implements ItemStream, ItemReader<String> {

    /** Testdata for read. */
    private List<String> testData;
    private Iterator<String> iterator;

    /** Iterates over testdata. */
    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return null;
        }
    }

    /** Opens the Iterator for the testdata. */
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.testData = createTestData(20);
        this.iterator = testData.iterator();
    }

    /** Method without function */
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        // no-op
    }

    /** Method without function */
    @Override
    public void close() throws ItemStreamException {
        // no-op
    }

    /**
     * Creates some testdata.
     *
     * @param count
     * @return 
     */
    private List<String> createTestData(int count) {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            data.add(String.valueOf(i));
        }
        return data;
    }
}
