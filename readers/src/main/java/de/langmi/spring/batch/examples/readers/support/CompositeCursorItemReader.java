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
package de.langmi.spring.batch.examples.readers.support;

import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.AbstractCursorItemReader;

/**
 * Copies the CompositeItemStream implementation, wraps its registered readers 
 * under one hood, so all read operations are simultaneously. 
 * Is not threadsafe, at least not on purpose.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class CompositeCursorItemReader<T> implements ItemStreamReader<T> {

    /** Registered ItemStreamReaders. */
    private List<AbstractCursorItemReader<?>> cursorItemReaders;
    /** Mandatory Unifying Mapper Implementation. */
    private UnifyingItemsMapper<T> unifyingMapper;

    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        // read from all registered readers
        List items = new ArrayList();
        for (AbstractCursorItemReader<?> cursorItemReader : cursorItemReaders) {
            items.add(cursorItemReader.read());
        }
        // delegate to mapper
        return unifyingMapper.mapItems(items);
    }

    /**
     * Broadcast the call to update to all registered readers.
     * 
     * @see org.springframework.batch.item.ItemStream#update(ExecutionContext)
     */
    @Override
    public void update(ExecutionContext executionContext) {
        for (ItemStream itemStream : cursorItemReaders) {
            itemStream.update(executionContext);
        }
    }

    /**
     * Broadcast the call to close to all registered readers.
     * 
     * @throws ItemStreamException
     */
    @Override
    public void close() throws ItemStreamException {
        for (ItemStream itemStream : cursorItemReaders) {
            itemStream.close();
        }
    }

    /**
     * Broadcast the call to open to all registered readers.
     * 
     * @throws ItemStreamException
     */
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        for (ItemStream itemStream : cursorItemReaders) {
            itemStream.open(executionContext);
        }
    }

    public void setUnifyingMapper(UnifyingItemsMapper<T> mapper) {
        this.unifyingMapper = mapper;
    }

    public void setCursorItemReaders(List<AbstractCursorItemReader<?>> cursorItemReaders) {
        this.cursorItemReaders = cursorItemReaders;
    }
}
