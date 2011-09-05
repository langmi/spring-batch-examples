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
package de.langmi.spring.batch.examples.readers.tar;

import java.util.Comparator;
import org.junit.Test;
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

    @Test
    public void testOneZipFile() throws Exception {
        LOG.debug("testOneZipFile");
        ArchiveMultiResourceItemReader<String> mReader = new ArchiveMultiResourceItemReader<String>();
        // setup multResourceReader
        mReader.setArchives(new Resource[]{new FileSystemResource("src/test/resources/input/input.txt.zip")});

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
                    LOG.debug(item);
                }
            } while (item != null);
            LOG.debug(String.valueOf(count));
        } catch (Exception e) {
            throw e;
        } finally {
            mReader.close();
        }
    }

    @Test
    public void testOneTarFileNestedDirs() throws Exception {
        LOG.debug("testOneTarFileNestedDirs");
        ArchiveMultiResourceItemReader<String> mReader = new ArchiveMultiResourceItemReader<String>();
        // setup multResourceReader
        mReader.setArchives(new Resource[]{new FileSystemResource("src/test/resources/input/input_nested_dir.tar")});

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
                    LOG.debug(item);
                }
            } while (item != null);
            LOG.debug(String.valueOf(count));
        } catch (Exception e) {
            throw e;
        } finally {
            mReader.close();
        }
    }

    @Test
    public void testOneTarFile() throws Exception {
        LOG.debug("testOneTarFile");
        ArchiveMultiResourceItemReader<String> mReader = new ArchiveMultiResourceItemReader<String>();
        // setup multResourceReader
        mReader.setArchives(new Resource[]{new FileSystemResource("src/test/resources/input/input.tar")});

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
                    LOG.debug(item);
                }
            } while (item != null);
            LOG.debug(String.valueOf(count));
        } catch (Exception e) {
            throw e;
        } finally {
            mReader.close();
        }
    }

    @Test
    public void testMultipleTarFiles() throws Exception {
        LOG.debug("testMultipleTarFiles");
        ArchiveMultiResourceItemReader<String> mReader = new ArchiveMultiResourceItemReader<String>();
        // setup multResourceReader
        mReader.setArchives(
                new Resource[]{
                    new FileSystemResource("src/test/resources/input/input_nested_dir.tar"),
                    new FileSystemResource("src/test/resources/input/input.tar")});

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
                    LOG.debug(item);
                }
            } while (item != null);
            LOG.debug(String.valueOf(count));
        } catch (Exception e) {
            throw e;
        } finally {
            mReader.close();
        }
    }

    private void generalMultiResourceReaderSetup(ArchiveMultiResourceItemReader<String> mReader) throws Exception {
        // overwrite comparator, do not use fileName, but resource description instead
        mReader.setComparator(new Comparator<Resource>() {

            /**
             * Compares resource filenames.
             */
            @Override
            public int compare(Resource r1, Resource r2) {
                return r1.getDescription().compareTo(r2.getDescription());
            }
        });
        // setup delegate
        FlatFileItemReader<String> reader = new FlatFileItemReader<String>();
        reader.setLineMapper(new PassThroughLineMapper());
        mReader.setDelegate(reader);


        // initialize multiResourceReader - is important, extracts the file from
        // the provided archive
        mReader.afterPropertiesSet();
    }
}
