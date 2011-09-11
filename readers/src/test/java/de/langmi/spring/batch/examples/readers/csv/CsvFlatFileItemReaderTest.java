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
package de.langmi.spring.batch.examples.readers.csv;

import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.core.io.FileSystemResource;

/**
 * Tests the CSV FlatFileItemReader without Application Context.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class CsvFlatFileItemReaderTest {

    /** Reader under test. */
    private FlatFileItemReader<FieldSet> reader = new FlatFileItemReader<FieldSet>();
    private static final String INPUT_FILE = "src/test/resources/input/csv/input.csv";
    private static final int EXPECTED_COUNT = 3;

    /**
     * Test should read succesfully.
     *
     * @throws Exception 
     */
    @Test
    public void testSuccessfulReading() throws Exception {
        // init linetokenizer
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(new String[]{"id", "value"});
        // init linemapper
        DefaultLineMapper<FieldSet> lineMapper = new DefaultLineMapper<FieldSet>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new PassThroughFieldSetMapper());
        // init reader
        reader.setLineMapper(lineMapper);
        reader.setResource(new FileSystemResource(INPUT_FILE));
        // open, provide "mock" ExecutionContext
        reader.open(MetaDataInstanceFactory.createStepExecution().getExecutionContext());
        // read
        try {
            int count = 0;
            FieldSet line;
            while ((line = reader.read()) != null) {
                count++;
            }
            assertEquals(EXPECTED_COUNT, count);
        } catch (Exception e) {
            throw e;
        } finally {
            reader.close();
        }
    }
}
