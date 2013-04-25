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

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Testing ItemReaders with Spring Context. The ListItemReader exhausts the used 
 * list, this makes it necessary to mark the used spring context as dirty.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de> 
 */
@ContextConfiguration(locations = {
    "classpath*:spring/batch/job/readers/list/list-simple-job.xml",
    "classpath*:spring/batch/setup/**/*.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
public class ItemReaderContextTest {

    @Autowired
    private IteratorItemReader<String> iteratorItemReader;
    @Autowired
    private ListItemReader<String> listItemReader;
    private static final int EXPECTED_COUNT = 20;

    /**
     * Test should read succesfully.
     *
     * @throws Exception 
     */
    @Test
    public void testListItemReader() throws Exception {
        // read
        int count = 0;
        String line;
        while ((line = listItemReader.read()) != null) {
            assertEquals(String.valueOf(count), line);
            count++;
        }
        assertEquals(EXPECTED_COUNT, count);
    }

    /**
     * Test should read succesfully.
     *
     * @throws Exception 
     */
    @Test
    public void testIteratorItemReader() throws Exception {
        // read
        int count = 0;
        String line;
        while ((line = iteratorItemReader.read()) != null) {
            assertEquals(String.valueOf(count), line);
            count++;
        }
        assertEquals(EXPECTED_COUNT, count);
    }
}
