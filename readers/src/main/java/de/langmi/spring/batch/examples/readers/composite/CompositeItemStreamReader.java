/**
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
package de.langmi.spring.batch.examples.readers.composite;

import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * Copies the CompositeItemStream implementation, wraps its readers under one
 * hood, so all read operations are synchronized.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class CompositeItemStreamReader<T> implements ItemStreamReader<T> {

    private List<ItemStreamReader<T>> itemReaderStreams;
    private ObjectListMapper<T> mapper;

    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        // read from all wrapped readers
        List<T> items = new ArrayList<T>();
        for (ItemStreamReader<T> itemReaderStream : itemReaderStreams) {
            items.add(itemReaderStream.read());
        }
        // delegate to mapper
        if (items.size() > 0) {
            return mapper.mapItems(items);
        } else {
            return null;
        }
    }

    /**
     * Simple aggregate {@link ExecutionContext} provider for the contributions
     * registered under the given key.
     * 
     * @see org.springframework.batch.item.ItemStream#update(ExecutionContext)
     */
    @Override
    public void update(ExecutionContext executionContext) {
        for (ItemStream itemStream : itemReaderStreams) {
            itemStream.update(executionContext);
        }
    }

    /**
     * Broadcast the call to close.
     * @throws ItemStreamException
     */
    @Override
    public void close() throws ItemStreamException {
        for (ItemStream itemStream : itemReaderStreams) {
            itemStream.close();
        }
    }

    /**
     * Broadcast the call to open.
     * @throws ItemStreamException
     */
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        for (ItemStream itemStream : itemReaderStreams) {
            itemStream.open(executionContext);
        }
    }

    public void setMapper(ObjectListMapper<T> mapper) {
        this.mapper = mapper;
    }

    public void setItemReaderStreams(List<ItemStreamReader<T>> itemReaderStreams) {
        this.itemReaderStreams = itemReaderStreams;
    }
}
