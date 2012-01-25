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
package de.langmi.spring.batch.examples.complex.aggregating.writer;

import de.langmi.spring.batch.examples.complex.aggregating.AggregatedItem;
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
public class AggregatingSimpleItemsWriterTest {

    private AggregateSimpleItemsWriter writer;
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
    public void testWriteInOrderedSequences() throws Exception {
        int count = 5;
        // first write sequence, no item should be written out
        int valueFirstSequence = 1;
        int sharedIdFirstSequence = 1000;
        List<SimpleItem> testData1 = createTestData(count,
                                                    sharedIdFirstSequence,
                                                    valueFirstSequence);
        writer.write(testData1);
        // check delegate, should have no data yet
        assertTrue(delegate.getData().isEmpty());


        // second write sequence with another shared id
        int valueSecondSequence = 2;
        int sharedIdSecondSequence = 2000;
        List<SimpleItem> testData2 = createTestData(count,
                                                    sharedIdSecondSequence,
                                                    valueSecondSequence);
        writer.write(testData2);
        // check delegate, should have one entry from sequence before
        assertEquals(1, delegate.getData().size());
        // check shared id
        assertEquals(sharedIdFirstSequence, delegate.getData().get(0).getId());
        // check sum
        assertEquals(valueFirstSequence * count, delegate.getData().get(0).getSum());
        // clear test delegate
        delegate.resetData();

        // third write sequence with yet another shared id
        int valueThirdSequence = 3;
        int sharedIdThirdSequence = 3000;
        List<SimpleItem> testData3 = createTestData(count,
                                                    sharedIdThirdSequence,
                                                    valueThirdSequence);
        writer.write(testData3);
        // check delegate, should have one entry from sequence before
        assertEquals(1, delegate.getData().size());
        // check shared id
        assertEquals(sharedIdSecondSequence, delegate.getData().get(0).getId());
        // check sum
        assertEquals(valueSecondSequence * count, delegate.getData().get(0).getSum());
        // clear test delegate
        delegate.resetData();

        // set reader exhausted for the fourth sequence
        stepExecution.getExecutionContext().put("readerExhausted", Boolean.TRUE);
        int valueFourthSequence = 4;
        int sharedIdFourthSequence = 4000;
        List<SimpleItem> testData4 = createTestData(count,
                                                    sharedIdFourthSequence,
                                                    valueFourthSequence);
        writer.write(testData4);
        // check delegate, should have one entry from sequence before and the last one
        assertEquals(2, delegate.getData().size());
        // check shared id
        assertEquals(sharedIdThirdSequence, delegate.getData().get(0).getId());
        assertEquals(sharedIdFourthSequence, delegate.getData().get(1).getId());
        // check sum
        assertEquals(valueThirdSequence * count, delegate.getData().get(0).getSum());
        assertEquals(valueFourthSequence * count, delegate.getData().get(1).getSum());
    }

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
    public void testWriteWithChangeInsideSequence() throws Exception {
        int count = 5;
        // create a combined set of data with different shared id
        int valueFirstSequence = 1;
        int sharedIdFirstSequence = 1000;
        List<SimpleItem> testData1 = createTestData(count,
                                                    sharedIdFirstSequence,
                                                    valueFirstSequence);
        int valueSecondSequence = 2;
        int sharedIdSecondSequence = 2000;
        List<SimpleItem> testData2 = createTestData(count,
                                                    sharedIdSecondSequence,
                                                    valueSecondSequence);
        testData1.addAll(testData2);
        writer.write(testData1);
        // check delegate, should have one entry from sequence before
        assertEquals(1, delegate.getData().size());
        // check shared id
        assertEquals(sharedIdFirstSequence, delegate.getData().get(0).getId());
        // check sum
        assertEquals(valueFirstSequence * count, delegate.getData().get(0).getSum());
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

    /**
     * Test Initialization, prepares the writer under test.
     * @throws Exception 
     */
    @Before
    public void before() throws Exception {
        writer = new AggregateSimpleItemsWriter();
        // delegate
        delegate = new TestItemWriter();
        writer.setDelegate(delegate);
        // dummy stepExecutionContext
        stepExecution = MetaDataInstanceFactory.createStepExecution();
        writer.beforeStep(stepExecution);
    }

    /**
     * The ItemWriter for use as delegate and for easy checking of values.
     */
    private class TestItemWriter implements ItemWriter<AggregatedItem> {

        private List<AggregatedItem> data = new ArrayList<AggregatedItem>();

        @Override
        public void write(List<? extends AggregatedItem> items) throws Exception {
            for (AggregatedItem item : items) {
                data.add(item);
            }
        }

        public void resetData() {
            data.clear();
        }

        public List<AggregatedItem> getData() {
            return data;
        }
    }
}
