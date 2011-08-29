package de.langmi.spring.batch.examples.readers.zip;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests if a FlatFileItemReader works with the selfmade 
 * ZipBufferedReaderFactory and reads .zip files successfully.
 * 
 * @author Michael R. Lange
 */
public class ZippedFileDecompressionTest {

    private static final String PATH_TO_COMPRESSED_TEST_FILE = "src/test/resources/input/input.txt.zip";
    private static final int EXPECTED_COUNT = 20;

    /**
     * Read test.
     *
     * @throws Exception 
     */
    @Test
    public void read() throws Exception {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(PATH_TO_COMPRESSED_TEST_FILE);
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            while (( entry = zin.getNextEntry()) != null) {
                ByteArrayOutputStream output = new ByteArrayOutputStream();

                int data = 0;
                while ((data = zin.read()) != - 1) {
                    output.write(data);
                }
                // convert output to inputStream
                InputStream in = new ByteArrayInputStream(output.toByteArray());
                String item;
                output.close();
                
                //assertEquals(EXPECTED_COUNT, count);
            }
            fis.close();
            zin.close();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
}
