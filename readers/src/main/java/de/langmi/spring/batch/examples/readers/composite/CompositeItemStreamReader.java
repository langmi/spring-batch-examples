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
package de.langmi.spring.batch.examples.readers.composite;

import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.support.CompositeItemStream;

/**
 * - die wrapped readers werden durch spring automatisch opened/closed/updated
 *   nicht nochmal extra als Listener registrieren !
 * - pro
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class CompositeItemStreamReader<T> extends CompositeItemStream implements ItemReader<T> {

    private ItemStreamReader[] readers;
    private ObjectListMapper<T> mapper;

    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        // read from all wrapped readers
        List items = new ArrayList();
        for (int i = 0; i < readers.length; i++) {
            items.add(readers[i].read());
        }
        // delegate to mapper
        if (items.size() > 0) {
            return mapper.mapItems(items);
        } else {
            return null;
        }
    }

    /** */
    @Override
    public void setStreams(ItemStream[] listeners) {
        super.setStreams(readers);
    }
}
