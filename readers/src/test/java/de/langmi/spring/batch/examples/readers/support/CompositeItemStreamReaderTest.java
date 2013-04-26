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
package de.langmi.spring.batch.examples.readers.support;

import java.util.ArrayList;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import static org.junit.Assert.*;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.core.io.FileSystemResource;

/**
 * CompositeItemStreamReaderTest with FlatFileItemReaders.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class CompositeItemStreamReaderTest {

    private final CompositeItemStreamReader<String> reader = new CompositeItemStreamReader<String>();
    private static final String INPUT_FILE_1 = "src/test/resources/input/file/composite/input1.txt";
    private static final String INPUT_FILE_2 = "src/test/resources/input/file/composite/input2.txt";
    private static final int EXPECTED_COUNT = 20;

    @Test
    public void testRestart() throws Exception {
        // setup composite reader
        reader.setUnifyingMapper(new DefaultUnifyingStringItemsMapper());
        reader.setItemReaderStreams(new ArrayList<ItemStreamReader<?>>() {

            {
                add(createFlatFileItemReader(INPUT_FILE_1));
                add(createFlatFileItemReader(INPUT_FILE_2));
            }
        });

        // open, provide "mock" ExecutionContext
        // fake restart scenario, it works because the name of the FlatFileItemReader
        // is set to its input_file
        int alreadyRead = 2;
        ExecutionContext ec = new ExecutionContext();
        ec.put(INPUT_FILE_1 + "." + "read.count", alreadyRead);
        ec.put(INPUT_FILE_2 + "." + "read.count", alreadyRead);
        reader.open(ec);
        // read
        try {
            // this makes sure we test a restart scenario, first read item
            // should be alreadyRead, for my example files this is a "2" and
            // not the first line a "0"
            int count = alreadyRead;
            String line;
            while ((line = reader.read()) != null) {
                assertEquals(String.valueOf(count) + String.valueOf(count), line);
                count++;
            }
            // read count includes the alreadyRead items too
            assertEquals(EXPECTED_COUNT, count);
        } catch (Exception e) {
            throw e;
        } finally {
            reader.close();
        }
    }

    @Test
    public void testRead() throws Exception {
        // setup composite reader
        reader.setUnifyingMapper(new DefaultUnifyingStringItemsMapper());
        reader.setItemReaderStreams(new ArrayList<ItemStreamReader<?>>() {

            {
                add(createFlatFileItemReader(INPUT_FILE_1));
                add(createFlatFileItemReader(INPUT_FILE_2));
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
     * Helpermethod to create FlatFileItemReader, sets the name too, to make restart
     * scenario possible - otherwise one flatFileItemReader would overwrite the 
     * other (in context).
     *
     * @param inputFile
     * @return configured FlatFileItemReader cast as ItemStreamReader
     */
    private ItemStreamReader<String> createFlatFileItemReader(final String inputFile) {
        FlatFileItemReader<String> ffir = new FlatFileItemReader<String>();
        // init reader
        ffir.setLineMapper(new PassThroughLineMapper());
        ffir.setResource(new FileSystemResource(inputFile));
        ffir.setName(inputFile);

        return (ItemStreamReader<String>) ffir;
    }
}
