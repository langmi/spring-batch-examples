/**
 * Copyright 2012 Michael R. Lange <michael.r.lange@langmi.de>.
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
package de.langmi.spring.batch.examples.complex.aggregating.reader;

import de.langmi.spring.batch.examples.complex.aggregating.AggregatedItem;
import de.langmi.spring.batch.examples.complex.support.SimpleItem;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.IteratorItemReader;

/**
 * AggregateSimpleItemsReaderTest.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class AggregateSimpleItemsReaderTest {

    /** Reader under test. */
    private AggregateSimpleItemsReader reader = null;

    /**
     * Test with ID change, should result in two aggregated items.
     */
    @Test
    public void testReadWithIdChange() throws Exception {
        // create first set of simple items with shared id
        int count = 5;
        int sharedId = 1000;
        int value = 1;
        List<SimpleItem> testData = createTestData(count, sharedId, value);
        // create second set of simple items with shared id
        int countSecond = 6;
        int sharedIdSecond = 2000;
        int valueSecond = 3;
        List<SimpleItem> testData2 = createTestData(countSecond, sharedIdSecond, valueSecond);
        // combine them
        testData.addAll(testData2);
        // setup wrapped delegate
        ItemReader<SimpleItem> delegate = new IteratorItemReader<SimpleItem>(testData);
        reader.setDelegate(delegate);

        // 1. aggregated item
        AggregatedItem item = reader.read();
        assertNotNull(item);
        assertEquals(sharedId, item.getId());
        assertEquals(count * value, item.getSum());
        // 2. aggregated item
        AggregatedItem itemSecond = reader.read();
        assertNotNull(itemSecond);
        assertEquals(sharedIdSecond, itemSecond.getId());
        assertEquals(countSecond * valueSecond, itemSecond.getSum());
        // there should be no next item        
        assertNull(reader.read());
    }

    /**
     * Test without ID change, should result in one aggregated item only.
     */
    @Test
    public void testReadWithoutIdChange() throws Exception {
        int count = 5;
        int sharedId = 1000;
        int value = 1;
        ItemReader<SimpleItem> delegate =
                new IteratorItemReader<SimpleItem>(createTestData(count, sharedId, value));
        reader.setDelegate(delegate);

        AggregatedItem item = reader.read();
        assertNotNull(item);
        assertEquals(sharedId, item.getId());
        assertEquals(count * value, item.getSum());
        // there should be no next item
        assertNull(reader.read());
    }

    @Before
    public void before() {
        this.reader = new AggregateSimpleItemsReader();
    }

    /**
     * Testdata util method.
     * 
     * @param count
     * @param sharedId
     * @param value
     * @return 
     */
    private List<SimpleItem> createTestData(int count, int sharedId, int value) {
        List<SimpleItem> testData = new ArrayList<SimpleItem>();
        for (int i = 0; i < count; i++) {
            testData.add(new SimpleItem(i, sharedId, value));
        }
        return testData;
    }
}
