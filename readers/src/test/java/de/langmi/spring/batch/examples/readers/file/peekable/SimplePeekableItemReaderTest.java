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
package de.langmi.spring.batch.examples.readers.file.peekable;

import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.item.support.SingleItemPeekableItemReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.core.io.FileSystemResource;

/**
 * Tests for groking the {@link SimpleItemPeekableItemReader}
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class SimplePeekableItemReaderTest {

    /** The delegate reader. */
    private FlatFileItemReader<String> delegateReader = new FlatFileItemReader<String>();
    private static final String INPUT_FILE = "src/test/resources/input/file/simple/input.txt";
    private static final int EXPECTED_COUNT = 20;

    /**
     * Test should read succesfully.
     *
     * @throws Exception 
     */
    @Test
    public void testSuccessfulPeekAhead() throws Exception {
        // init delegate
        delegateReader.setLineMapper(new PassThroughLineMapper());
        delegateReader.setResource(new FileSystemResource(INPUT_FILE));
        // init peekable
        SingleItemPeekableItemReader<String> peekable = new SingleItemPeekableItemReader<String>();
        peekable.setDelegate(delegateReader);
        // open, provide "mock" ExecutionContext
        peekable.open(MetaDataInstanceFactory.createStepExecution().getExecutionContext());
        // read
        try {
            int count = 0;
            String line;
            while ((line = peekable.read()) != null) {
                assertEquals(String.valueOf(count), line);
                // test for peek
                String lineAhead = peekable.peek();
                if (count + 1 < EXPECTED_COUNT) {
                    assertEquals(String.valueOf(count + 1), lineAhead);
                } else {
                    assertNull(lineAhead);
                }
                count++;
            }
            assertEquals(EXPECTED_COUNT, count);
        } catch (Exception e) {
            throw e;
        } finally {
            peekable.close();
        }
    }

    /**
     * Test should read succesfully.
     *
     * @throws Exception 
     */
    @Test
    public void testSuccessfulReading() throws Exception {
        // init delegate
        delegateReader.setLineMapper(new PassThroughLineMapper());
        delegateReader.setResource(new FileSystemResource(INPUT_FILE));
        // init peekable
        SingleItemPeekableItemReader<String> peekable = new SingleItemPeekableItemReader<String>();
        peekable.setDelegate(delegateReader);
        // open, provide "mock" ExecutionContext
        peekable.open(MetaDataInstanceFactory.createStepExecution().getExecutionContext());
        // read
        try {
            int count = 0;
            String line;
            while ((line = peekable.read()) != null) {
                assertEquals(String.valueOf(count), line);
                count++;
            }
            assertEquals(EXPECTED_COUNT, count);
        } catch (Exception e) {
            throw e;
        } finally {
            peekable.close();
        }
    }
}
