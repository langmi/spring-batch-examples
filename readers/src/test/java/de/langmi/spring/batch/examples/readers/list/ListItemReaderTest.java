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
package de.langmi.spring.batch.examples.readers.list;

import de.langmi.spring.batch.examples.readers.simple.TestDataFactoryBean;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.batch.item.support.ListItemReader;

/**
 * Tests the ListItemReader from Spring Batch without Application Context.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class ListItemReaderTest {

    /** Reader under test. */
    private ListItemReader<String> reader;

    /**
     * Test should read succesfully.
     *
     * @throws Exception 
     */
    @Test
    public void testSuccessfulReading() throws Exception {
        // setup reader with test data
        List<String> testData = new TestDataFactoryBean().getObject();
        reader = new ListItemReader<String>(testData);
        // read
        int count = 0;
        String line;
        while ((line = reader.read()) != null) {
            assertEquals(String.valueOf(count), line);
            count++;
        }
        assertEquals(testData.size(), count);
    }
}
