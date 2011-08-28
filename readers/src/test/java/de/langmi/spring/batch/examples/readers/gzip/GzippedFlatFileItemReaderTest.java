package de.langmi.spring.batch.examples.readers.gzip;

import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.core.io.FileSystemResource;

/**
 * Tests if a FlatFileItemReader works with the selfmade 
 * GZipBufferedReaderFactory and reads .gz files successfully.
 * 
 * @author Michael R. Lange
 */
public class GzippedFlatFileItemReaderTest {

    /** Reader under test. */
    private FlatFileItemReader<String> reader = new FlatFileItemReader<String>();
    private static final String PATH_TO_COMPRESSED_TEST_FILE = "src/test/resources/input/input.txt.gz";
    private static final int EXPECTED_COUNT = 20;

    /**
     * Read test.
     *
     * @throws Exception 
     */
    @Test
    public void read() throws Exception {
        // setup the reader
        reader.setBufferedReaderFactory(new GZipBufferedReaderFactory());
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
            assertEquals(count, EXPECTED_COUNT);
        } finally {
            reader.close();
        }
    }
}
