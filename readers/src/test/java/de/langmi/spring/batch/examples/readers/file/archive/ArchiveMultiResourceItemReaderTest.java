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
package de.langmi.spring.batch.examples.readers.file.archive;

import java.io.FilenameFilter;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * ArchiveMultiResourceItemReaderTest, without Spring context.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class ArchiveMultiResourceItemReaderTest {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * Test with one ZIP file containing two text files with 20 lines each.
     * One file is a .csv, the other a .txt, only the .csv should be used.
     *
     * @throws Exception 
     */
    @Test
    public void testOneZipFileWithFilter() throws Exception {
        LOG.debug("testOneZipFileWithFilter");
        ArchiveMultiResourceItemReader<String> mReader = new ArchiveMultiResourceItemReader<String>();
        // setup multResourceReader
        // zip file contains 2 files, one with suffix .csv, which contains 20 lines
        mReader.setArchives(new Resource[]{new FileSystemResource("src/test/resources/input/file/archive/input-mixed-files.zip")});
        // setup filter
        DefaultArchiveFileNameFilter fileNameFilter = new DefaultArchiveFileNameFilter();
        fileNameFilter.setSuffixes(new String[]{".csv"});
        mReader.setFilenameFilter(fileNameFilter);
        // call general setup last, includes call to afterPropertiesSet
        generalMultiResourceReaderWithFilterSetup(mReader, fileNameFilter);


        // open with mock context
        mReader.open(MetaDataInstanceFactory.createStepExecution().getExecutionContext());

        // read
        try {
            String item = null;
            int count = 0;
            do {
                item = mReader.read();
                if (item != null) {
                    assertEquals(String.valueOf(count), item);
                    count++;
                }
            } while (item != null);
            assertEquals(20, count);
        } catch (Exception e) {
            throw e;
        } finally {
            mReader.close();
        }
    }

    /**
     * Test with one ZIP file containing one text file with 20 lines.
     *
     * @throws Exception 
     */
    @Test
    public void testOneZipFile() throws Exception {
        LOG.debug("testOneZipFile");
        ArchiveMultiResourceItemReader<String> mReader = new ArchiveMultiResourceItemReader<String>();
        // setup multResourceReader
        mReader.setArchives(new Resource[]{new FileSystemResource("src/test/resources/input/file/archive/input.txt.zip")});

        // call general setup last
        generalMultiResourceReaderSetup(mReader);

        // open with mock context
        mReader.open(MetaDataInstanceFactory.createStepExecution().getExecutionContext());

        // read
        try {
            String item = null;
            int count = 0;
            do {
                item = mReader.read();
                if (item != null) {
                    assertEquals(String.valueOf(count), item);
                    count++;
                }
            } while (item != null);
            assertEquals(20, count);
        } catch (Exception e) {
            throw e;
        } finally {
            mReader.close();
        }
    }

    /**
     * Test with tar file with nested directories, contains 4 text files with
     * 20 lines each.
     * 
     * @throws Exception 
     */
    @Test
    public void testOneTarFileNestedDirs() throws Exception {
        LOG.debug("testOneTarFileNestedDirs");
        ArchiveMultiResourceItemReader<String> mReader = new ArchiveMultiResourceItemReader<String>();
        // setup multResourceReader
        mReader.setArchives(new Resource[]{new FileSystemResource("src/test/resources/input/file/archive/input_nested_dir.tar")});

        // call general setup last
        generalMultiResourceReaderSetup(mReader);

        // open with mock context
        mReader.open(MetaDataInstanceFactory.createStepExecution().getExecutionContext());

        // read
        try {
            String item = null;
            int count = 0;
            do {
                item = mReader.read();
                if (item != null) {
                    count++;
                }
            } while (item != null);
            assertEquals(80, count);
        } catch (Exception e) {
            throw e;
        } finally {
            mReader.close();
        }
    }

    /**
     * Test with tar file which contains 2 text files, with 20 lines each.
     * 
     * @throws Exception 
     */
    @Test
    public void testOneTarFile() throws Exception {
        LOG.debug("testOneTarFile");
        ArchiveMultiResourceItemReader<String> mReader = new ArchiveMultiResourceItemReader<String>();
        // setup multResourceReader
        mReader.setArchives(new Resource[]{new FileSystemResource("src/test/resources/input/file/archive/input.tar")});

        // call general setup last
        generalMultiResourceReaderSetup(mReader);

        // open with mock context
        mReader.open(MetaDataInstanceFactory.createStepExecution().getExecutionContext());

        // read
        try {
            String item = null;
            int count = 0;
            do {
                item = mReader.read();
                if (item != null) {
                    count++;
                }
            } while (item != null);
            assertEquals(40, count);
        } catch (Exception e) {
            throw e;
        } finally {
            mReader.close();
        }
    }

    /**
     * Test with tar file which contains 2 text files, with 20 lines each.
     * 
     * @throws Exception 
     */
    @Test
    public void testOneGzippedTarFile() throws Exception {
        LOG.debug("testOneGzippedTarFile");
        ArchiveMultiResourceItemReader<String> mReader = new ArchiveMultiResourceItemReader<String>();
        // setup multResourceReader
        mReader.setArchives(new Resource[]{new FileSystemResource("src/test/resources/input/file/archive/input_nested_dir.tar.gz")});

        // call general setup last
        generalMultiResourceReaderSetup(mReader);

        // open with mock context
        mReader.open(MetaDataInstanceFactory.createStepExecution().getExecutionContext());

        // read
        try {
            String item = null;
            int count = 0;
            do {
                item = mReader.read();
                if (item != null) {
                    count++;
                }
            } while (item != null);
            assertEquals(80, count);
        } catch (Exception e) {
            throw e;
        } finally {
            mReader.close();
        }
    }

    /**
     * Test with multiple tar files, together they contain 6 text files with 20 
     * lines each.
     *
     * @throws Exception 
     */
    @Test
    public void testMultipleTarFiles() throws Exception {
        LOG.debug("testMultipleTarFiles");
        ArchiveMultiResourceItemReader<String> mReader = new ArchiveMultiResourceItemReader<String>();
        // setup multResourceReader
        mReader.setArchives(
                new Resource[]{
                    new FileSystemResource("src/test/resources/input/file/archive/input_nested_dir.tar"),
                    new FileSystemResource("src/test/resources/input/file/archive/input.tar")});

        // call general setup last
        generalMultiResourceReaderSetup(mReader);

        // open with mock context
        mReader.open(MetaDataInstanceFactory.createStepExecution().getExecutionContext());

        // read
        try {
            String item = null;
            int count = 0;
            do {
                item = mReader.read();
                if (item != null) {
                    count++;
                }
            } while (item != null);
            assertEquals(120, count);
        } catch (Exception e) {
            throw e;
        } finally {
            mReader.close();
        }
    }

    /**
     * Overloaded helper method to setup the used MultiResourceItemReader with 
     * specific FileNameFilter.
     *
     * @param mReader
     * @param filenameFilter
     * @throws Exception 
     */
    private void generalMultiResourceReaderWithFilterSetup(
            ArchiveMultiResourceItemReader<String> mReader,
            FilenameFilter filenameFilter) throws Exception {
        // set filter if needed
        if (filenameFilter != null) {
            mReader.setFilenameFilter(filenameFilter);
        }
        generalMultiResourceReaderSetup(mReader);
    }

    /**
     * Helper method to setup the used MultiResourceItemReader.
     *
     * @param mReader
     * @throws Exception 
     */
    private void generalMultiResourceReaderSetup(ArchiveMultiResourceItemReader<String> mReader) throws Exception {
        // setup delegate
        FlatFileItemReader<String> reader = new FlatFileItemReader<String>();
        reader.setLineMapper(new PassThroughLineMapper());
        mReader.setDelegate(reader);
    }
}
