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
package de.langmi.spring.batch.examples.readers.simpleflatfile;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.core.io.FileSystemResource;

/**
 * Tests the FlatFileItemReader from Spring Batch without Application Context.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class FlatFileItemReaderWithoutSpringContextTest {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private FlatFileItemReader<String> reader;
    private static final String INPUT_FILE = "src/test/resources/input/input.txt";
    private static final int COUNT = 20;

    /**
     * Tests should successfully read the file.
     *
     * @throws Exception 
     */
    @Test
    public void testSuccessfulReading() throws Exception {
        // set resource
        reader.setResource(new FileSystemResource(INPUT_FILE));
        // open, provide "mock" ExecutionContext
        reader.open(MetaDataInstanceFactory.createStepExecution().getExecutionContext());
        // read
        try {
            String item = null;
            int i = 0;
            do {
                item = reader.read();
                if (item != null) {
                    assertTrue(i == Integer.valueOf(item));
                    i++;
                }
            } while (item != null);
            assertTrue(i == COUNT);
        } catch (Exception e) {
            throw e;
        } finally {
            reader.close();
        }
    }

    /**
     * Initialize reader for tests.
     *
     * @throws Exception 
     */
    @Before
    public void setup() throws Exception {
        // init reader
        reader = new FlatFileItemReader<String>();
        reader.setLineMapper(new PassThroughLineMapper());
    }
}
