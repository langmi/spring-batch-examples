package de.langmi.spring.batch.examples.readers.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

/**
 * Tests if a FlatFileItemReader works with the selfmade 
 * ZipBufferedReaderFactory and reads .zip files successfully.
 * 
 * @author Michael R. Lange
 */
public class ZippedFileDecompressionTest {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private static final String PATH_TO_COMPRESSED_TEST_FILE = "src/test/resources/input/input.txt.zip";
    private static final String OUTPUT_PATH = "target/test-outputs/";
    private static final int EXPECTED_COUNT = 20;

    @Test
    public void readDecompressToFile() throws Exception {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(PATH_TO_COMPRESSED_TEST_FILE);
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                LOG.debug(entry.getName() + " size:" + entry.getSize());
                File file = new File(OUTPUT_PATH + entry.getName());
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);

                int data = 0;
                while ((data = zin.read()) != - 1) {
                    fos.write(data);
                }
                // close early
                fos.close();
            }
            fis.close();
            zin.close();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    /**
     * Read test.
     *
     * @throws Exception 
     */
    @Test
    public void readSimpleCodeVersion() throws Exception {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(PATH_TO_COMPRESSED_TEST_FILE);
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                LOG.debug(entry.getName() + " size:" + entry.getSize());
                ByteArrayOutputStream output = new ByteArrayOutputStream();

                int data = 0;
                while ((data = zin.read()) != - 1) {
                    output.write(data);
                }
                // close early
                output.close();
                // convert output to inputStream
                InputStream in = new ByteArrayInputStream(output.toByteArray());
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(isr);
                String item;
                int count = 0;
                do {
                    item = br.readLine();
                    if (item != null) {
                        count++;
                        LOG.debug(item);
                    }
                } while (item != null);
                br.close();
                assertEquals(EXPECTED_COUNT, count);
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
