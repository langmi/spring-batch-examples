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
package de.langmi.spring.batch.examples.readers.file.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Trying to grok the Java ZIP API.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class ZipTest {

    /** Logger for tests. */
    private static final Logger LOG = LoggerFactory.getLogger(ZipTest.class);
    private static final String ZIP_INPUT_SINGLE_FILE = "src/test/resources/input/file/archive/input.txt.zip";
    private static final String ZIP_INPUT_MULTIPLE_FILES = "src/test/resources/input/file/archive/input-mixed-files.zip";
    private static final String ZIP_INPUT_NESTED_DIRS = "src/test/resources/input/file/archive/input-nested-dir.zip";
    /** 0xFFFF = 65535 = 64kB */
    private static final byte[] buffer = new byte[0xFFFF];
    private static final String OUTPUT_DIR = "target/test-outputs/readers/file/ziptest/";

    /**
     * Read filename from file in zip archive.
     *
     * @throws Exception 
     */
    @Test
    public void testZipFileSimple() throws Exception {
        LOG.debug("testZipFileSimple");
        ZipFile zipFile = new ZipFile(ZIP_INPUT_SINGLE_FILE);
        try {
            Enumeration<? extends ZipEntry> zipEntryEnum = zipFile.entries();
            while (zipEntryEnum.hasMoreElements()) {
                ZipEntry zipEntry = zipEntryEnum.nextElement();
                LOG.debug("extracting:" + zipEntry.getName());
            }
        } finally {
            zipFile.close();
        }
    }

    /**
     * Extract file from zip archive.
     *
     * @throws Exception 
     */
    @Test
    public void testZipExtractFileSimple() throws Exception {
        LOG.debug("testZipExtractFileSimple");
        ZipFile zipFile = new ZipFile(ZIP_INPUT_SINGLE_FILE);
        try {
            Enumeration<? extends ZipEntry> zipEntryEnum = zipFile.entries();
            while (zipEntryEnum.hasMoreElements()) {
                ZipEntry zipEntry = zipEntryEnum.nextElement();
                extractEntry(zipFile, zipEntry, OUTPUT_DIR);
            }
        } finally {
            zipFile.close();
        }
    }

    /**
     * Extract files from zip archive with multiple files.
     *
     * @throws Exception 
     */
    @Test
    public void testZipExtractMultipleFiles() throws Exception {
        LOG.debug("testZipExtractMultipleFiles");
        ZipFile zipFile = new ZipFile(ZIP_INPUT_MULTIPLE_FILES);

        try {
            Enumeration<? extends ZipEntry> zipEntryEnum = zipFile.entries();
            while (zipEntryEnum.hasMoreElements()) {
                ZipEntry zipEntry = zipEntryEnum.nextElement();
                extractEntry(zipFile, zipEntry, OUTPUT_DIR);
            }
        } finally {
            zipFile.close();
        }
    }

    /**
     * Extract files from zip archive with multiple files in nested directories.
     *
     * @throws Exception 
     */
    @Test
    public void testZipNestedDirectories() throws Exception {
        LOG.debug("testZipNestedDirectories");
        ZipFile zipFile = new ZipFile(ZIP_INPUT_NESTED_DIRS);

        try {
            Enumeration<? extends ZipEntry> zipEntryEnum = zipFile.entries();
            while (zipEntryEnum.hasMoreElements()) {
                ZipEntry zipEntry = zipEntryEnum.nextElement();
                extractEntry(zipFile, zipEntry, OUTPUT_DIR);
            }
        } finally {
            zipFile.close();
        }
    }

    /**
     * Util method to extract zip file entries.
     * Shameless copied from <a href="Galileo Java Zip">http://www.iks.hs-merseburg.de/~uschroet/Literatur/Java_Lit/JAVA_Insel/javainsel_14_010.htm#mjf11c23787f57cfbb5654d7d851f226bc</a>.
     * @param zf
     * @param entry
     * @param destDir
     * @throws IOException 
     */
    private static void extractEntry(ZipFile zf, ZipEntry entry, String destDir) throws IOException {
        LOG.debug("extracting:" + entry.getName());

        File file = new File(destDir, entry.getName());
        if (entry.isDirectory()) {
            file.mkdirs();
        } else {
            new File(file.getParent()).mkdirs();
            InputStream is = null;
            OutputStream os = null;

            try {
                is = zf.getInputStream(entry);
                os = new FileOutputStream(file);
                for (int len; (len = is.read(buffer)) != -1;) {
                    os.write(buffer, 0, len);
                }
            } finally {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            }
        }
    }
}
