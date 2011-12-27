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
package de.langmi.spring.batch.examples.complex.file.split;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.batch.item.ItemWriter;
import static org.junit.Assert.*;

/**
 * Tests for {@link SplitFilesItemWriter}.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
@RunWith(Parameterized.class)
public class SplitFilesItemWriterTest {

    @Parameters
    public static List<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                    {0}, {1}, {2}, {3}, {157}, {312}, {1099}
                });
    }
    private int count;

    public SplitFilesItemWriterTest(int count) {
        this.count = count;
    }

    @Test
    public void testWrite() throws Exception {
        // setup
        SplitFilesItemWriter writer = new SplitFilesItemWriter();
        ListItemWriter<String> firstWriter = new ListItemWriter<String>();
        writer.setFirstWriter(firstWriter);
        ListItemWriter<String> secondWriter = new ListItemWriter<String>();
        writer.setSecondWriter(secondWriter);
        writer.setInputLineCount(count);

        // write
        writer.write(getTestItems(count));

        // assertions
        int firstHalf = count / 2;
        int secondHalf = count - firstHalf;
        assertEquals(firstHalf, firstWriter.getItems().size());
        assertEquals(secondHalf, secondWriter.getItems().size());

    }

    /**
     * Create item list for writer.
     *
     * @param count
     * @return 
     */
    private List<String> getTestItems(int count) {
        List<String> items = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            items.add(String.valueOf(i));
        }
        return items;
    }

    /**
     * Helper ItemWriter Implementation.
     *
     * @param <String> 
     */
    private class ListItemWriter<String> implements ItemWriter<String> {

        private List<String> items = new ArrayList<String>();

        @Override
        public void write(List<? extends String> items) throws Exception {
            this.items.addAll(items);
        }

        public List<String> getItems() {
            return items;
        }
    }
}
