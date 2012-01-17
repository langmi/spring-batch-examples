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
package de.langmi.spring.batch.examples.complex.aggregating;

import de.langmi.spring.batch.examples.complex.support.SimpleItem;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.StepExecution;
import static org.junit.Assert.*;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.test.MetaDataInstanceFactory;

/**
 * Test for AggregatingItemsWriter.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class AggregatingItemsWriterTest {

    private AggregatingItemsWriter writer;
    private TestItemWriter delegate;
    private StepExecution stepExecution;

    /**
     * Simple test for the standard usecase.
     * Some write sequences and an exhausted reader at the end.
     * 
     * I tried to use mock with Mockito, but i can't use it properly
     * because the items in use are not the same after delegation.
     * 
     * @throws Exception 
     */
    @Test
    public void testWrite() throws Exception {
        // first write sequence, no item should be written out
        List<SimpleItem> testData1 = createTestData(5, 1000);
        writer.write(testData1);
        // check delegate, should have no data
        assertTrue(delegate.getData().isEmpty());

        // second write sequence with another shared id
        List<SimpleItem> testData2 = createTestData(5, 2000);
        writer.write(testData2);
        // check delegate, should have data from sequence before
        assertEquals(5, delegate.getData().size());
        for (SimpleItem item : testData1) {
            assertTrue(delegate.getData().contains(item));
        }
        // clear delegate
        delegate.resetData();

        // third write sequence with yet another shared id
        List<SimpleItem> testData3 = createTestData(5, 3000);
        writer.write(testData3);
        // check delegate, should have data from sequence before
        assertEquals(5, delegate.getData().size());
        for (SimpleItem item : testData2) {
            assertTrue(delegate.getData().contains(item));
        }
        // clear delegate
        delegate.resetData();

        // set reader exhausted for the fourth sequence
        stepExecution.getExecutionContext().put("readerExhausted", Boolean.TRUE);
        // fourth write sequence with yet another shared id
        List<SimpleItem> testData4 = createTestData(5, 4000);
        writer.write(testData4);
        // check delegate, should have data from sequence before
        assertEquals(10, delegate.getData().size());
        for (SimpleItem item : testData3) {
            assertTrue(delegate.getData().contains(item));
        }
        for (SimpleItem item : testData4) {
            assertTrue(delegate.getData().contains(item));
        }
    }

    private List<SimpleItem> createTestData(int count, int sharedId) {
        List<SimpleItem> testData = new ArrayList<SimpleItem>();
        for (int i = 0; i < count; i++) {
            testData.add(new SimpleItem(i, sharedId, String.valueOf(i)));
        }
        return testData;
    }

    /**
     * Test Initialization, prepares the writer under test.
     * @throws Exception 
     */
    @Before
    public void before() throws Exception {
        writer = new AggregatingItemsWriter();
        // delegate
        delegate = new TestItemWriter();
        writer.setDelegate(delegate);
        // dummy stepExecutionContext
        stepExecution = MetaDataInstanceFactory.createStepExecution();
        writer.beforeStep(stepExecution);
    }

    /**
     * The ItemWriter for use as delegate.
     */
    private class TestItemWriter implements ItemWriter<SimpleItem> {

        private List<SimpleItem> data = new ArrayList<SimpleItem>();

        @Override
        public void write(List<? extends SimpleItem> items) throws Exception {
            for (SimpleItem item : items) {
                data.add(item);
            }
        }

        public void resetData() {
            data.clear();
        }

        public List<SimpleItem> getData() {
            return data;
        }
    }
}
