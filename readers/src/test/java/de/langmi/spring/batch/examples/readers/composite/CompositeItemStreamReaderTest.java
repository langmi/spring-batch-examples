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
package de.langmi.spring.batch.examples.readers.composite;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.core.io.FileSystemResource;

/**
 * CompositeItemStreamReaderTest.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class CompositeItemStreamReaderTest {

    private final CompositeItemStreamReader<String> reader = new CompositeItemStreamReader<String>();
    private static final String INPUT_FILE_CSV = "src/test/resources/input/composite/input.csv";
    private static final String INPUT_FILE_TXT = "src/test/resources/input/composite/input.txt";
    private static final int EXPECTED_COUNT = 6;

    @Test
    public void test() throws Exception {
        // setup composite reader
        reader.setMapper(new DefaultStringListMapper());
        reader.setItemReaderStreams(new ArrayList<ItemStreamReader<String>>() {

            {
                add(createFlatFileItemReader(INPUT_FILE_CSV));
                add(createFlatFileItemReader(INPUT_FILE_TXT));
            }
        });

        // open, provide "mock" ExecutionContext
        reader.open(MetaDataInstanceFactory.createStepExecution().getExecutionContext());
        // read
        try {
            int count = 0;
            String line;
            while ((line = reader.read()) != null) {
                assertEquals(String.valueOf(count) + String.valueOf(count), line);
                count++;
            }
            assertEquals(EXPECTED_COUNT, count);
        } catch (Exception e) {
            throw e;
        } finally {
            reader.close();
        }
    }

    /**
     * Helpermethod to create FlatFileItemReader.
     *
     * @param inputFile
     * @return 
     */
    private ItemStreamReader<String> createFlatFileItemReader(final String inputFile) {
        FlatFileItemReader<String> ffir = new FlatFileItemReader<String>();
        // init reader
        ffir.setLineMapper(new PassThroughLineMapper());
        ffir.setResource(new FileSystemResource(inputFile));

        return (ItemStreamReader) ffir;
    }
}
