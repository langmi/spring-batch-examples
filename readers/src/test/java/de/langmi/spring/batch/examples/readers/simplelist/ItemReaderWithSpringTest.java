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
package de.langmi.spring.batch.examples.readers.simplelist;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Testing ItemReaders with Spring Context.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de> 
 */
@ContextConfiguration(locations = {
    "classpath*:spring/batch/job/simple-list-job.xml",
    "classpath*:spring/batch/setup/**/*.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ItemReaderWithSpringTest {

    /** Logger. */
    private final Logger LOG = LoggerFactory.getLogger(getClass());
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
        while (listItemReader.read() != null) {
            count++;
        }
        assertEquals(count, EXPECTED_COUNT);
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
        while (iteratorItemReader.read() != null) {
            count++;
        }
        assertEquals(count, EXPECTED_COUNT);
    }
}
