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
package de.langmi.spring.batch.examples.readers.file.gzip;

import de.langmi.spring.batch.examples.readers.file.gzip.GZipBufferedReaderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.core.io.FileSystemResource;

/**
 * GZipBufferedReaderFactoryTest.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class GZipBufferedReaderFactoryTest {

    private GZipBufferedReaderFactory gzbrf;
    private static final String PATH_TO_COMPRESSED_TEST_FILE = "src/test/resources/input/file/gzip/input.txt.gz";
    private static final String PATH_TO_COMPRESSED_TEST_FILE_OTHERSUFFIX = "src/test/resources/input/file/gzip/input.txt.gzfoo";
    private static final String PATH_TO_UNCOMPRESSED_TEST_FILE = "src/test/resources/input/file/gzip/input.txt";

    @Test
    public void testChangingSuffix() throws Exception {
        final String suffix = "foo";
        gzbrf.setGzipSuffixes(new ArrayList<String>() {

            {
                add(suffix);
            }
        });
        assertNotNull(gzbrf.getGzipSuffixes());
        assertTrue(gzbrf.getGzipSuffixes().contains(suffix));
    }

    @Test
    public void testAddingSuffix() throws Exception {
        String suffix = "foo";
        gzbrf.getGzipSuffixes().add(suffix);
        assertTrue(gzbrf.getGzipSuffixes().contains(suffix));
    }

    @Test
    public void testDefaultSuffixes() throws Exception {
        assertNotNull(gzbrf.getGzipSuffixes());
        assertTrue(gzbrf.getGzipSuffixes().size() > 0);
        for (String suffix : gzbrf.getGzipSuffixes()) {
            assertTrue(".gz".equals(suffix) || ".gzip".equals(suffix));
        }
    }

    @Test
    public void testWithGzipFileOtherSuffix() throws Exception {
        // adjust suffixes
        gzbrf.getGzipSuffixes().add(".gzfoo");
        // suffixes still ok?
        assertNotNull(gzbrf.getGzipSuffixes());
        assertTrue(gzbrf.getGzipSuffixes().size() > 0);
        // suffixes really changed?
        for (String suffix : gzbrf.getGzipSuffixes()) {
            assertTrue(
                    ".gz".equals(suffix)
                    || ".gzip".equals(suffix)
                    || ".gzfoo".equals(suffix));
        }
        // try to create 
        BufferedReader reader = gzbrf.create(
                new FileSystemResource(PATH_TO_COMPRESSED_TEST_FILE_OTHERSUFFIX),
                Charset.defaultCharset().name());
        // creation successful?
        assertNotNull(reader);
        // clean up, close the reader
        reader.close();
    }

    @Test
    public void testFailWithFileNotInGzipFormat() throws Exception {
        // adjust suffix
        gzbrf.setGzipSuffixes(new ArrayList<String>() {

            {
                add(".txt");
            }
        });

        try {
            gzbrf.create(
                    new FileSystemResource(PATH_TO_UNCOMPRESSED_TEST_FILE),
                    Charset.defaultCharset().name());
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
            assertTrue(e.getMessage().contains("Not in GZIP format"));
        }
    }

    @Test
    public void testDefaultWithGzipFile() throws Exception {
        // try to create 
        BufferedReader reader = gzbrf.create(
                new FileSystemResource(PATH_TO_COMPRESSED_TEST_FILE),
                Charset.defaultCharset().name());
        // creation successful?
        assertNotNull(reader);
        // clean up, close the reader
        reader.close();
    }

    @Test
    public void testDefaultWithNormalFile() throws Exception {
        // try to create 
        BufferedReader reader = gzbrf.create(
                new FileSystemResource(PATH_TO_UNCOMPRESSED_TEST_FILE),
                Charset.defaultCharset().name());
        // creation successful?
        assertNotNull(reader);
        // clean up, close the reader
        reader.close();
    }

    @Before
    public void before() {
        gzbrf = new GZipBufferedReaderFactory();
    }
}
