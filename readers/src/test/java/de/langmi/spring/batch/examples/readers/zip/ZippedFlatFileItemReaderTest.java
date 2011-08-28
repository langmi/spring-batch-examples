package de.langmi.spring.batch.examples.readers.zip;

import org.junit.Ignore;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.core.io.FileSystemResource;

/**
 * Tests if a FlatFileItemReader works with the selfmade 
 * ZipBufferedReaderFactory and reads .zip files successfully.
 * 
 * @author Michael R. Lange
 */
public class ZippedFlatFileItemReaderTest {

    /** Reader under test. */
    private FlatFileItemReader<String> reader = new FlatFileItemReader<String>();
    private static final String PATH_TO_COMPRESSED_TEST_FILE = "src/test/resources/input/input.txt.zip";
    private static final int EXPECTED_COUNT = 20;

    /**
     * Read test.
     *
     * @throws Exception 
     */
    @Test
    @Ignore
    public void read() throws Exception {
        // setup the reader
        reader.setBufferedReaderFactory(new ZipBufferedReaderFactory());
        reader.setLineMapper(new PassThroughLineMapper());
        reader.setResource(new FileSystemResource(PATH_TO_COMPRESSED_TEST_FILE));
        // open with dummy execution context
        reader.open(MetaDataInstanceFactory.createStepExecution().getExecutionContext());

        // read 
        try {
            int count = 0;
            while (reader.read() != null) {
                count++;
            }
            assertEquals(EXPECTED_COUNT, count);
        } finally {
            reader.close();
        }
    }
}
