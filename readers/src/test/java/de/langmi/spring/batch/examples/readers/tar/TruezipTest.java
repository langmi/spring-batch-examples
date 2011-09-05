/*
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

import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Trying to grok Truezip.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class TruezipTest {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Test
    public void testGzipFileSimple() throws Exception {
        LOG.debug("testGzipFileSimple");
        TFile file = new TFile("src/test/resources/input/input.txt.gz");
        LOG.debug("truezip can't handle pure gzipped files, 'just' tar.gz.");
    }

    @Test
    public void testZipFileSimple() throws Exception {
        LOG.debug("testZipFileSimple");
        TFile file = new TFile("src/test/resources/input/input.txt.zip");
        TFile files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            TFile tFile = files[i];
            LOG.debug(tFile.getName());
        }
    }

    @Test
    public void testTarFileSimple() throws Exception {
        LOG.debug("testTarFileSimple");
        TFile file = new TFile("src/test/resources/input/input.tar");
        TFile files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            TFile tFile = files[i];
            LOG.debug(tFile.getName());
        }
    }

    @Test
    public void testTarFileNestedDirectories() throws Exception {
        LOG.debug("testTarFileNestedDirectories");
        TFile file = new TFile("src/test/resources/input/input_nested_dir.tar");
        List<TFile> result = new ArrayList<TFile>();
        runNestedDirs(file, result);
        for (TFile extractedFile : result) {
            LOG.debug(extractedFile.getName());
            TFileInputStream tfis = new TFileInputStream(extractedFile);
            try {
                DataInputStream in = new DataInputStream(tfis);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    LOG.debug(strLine);
                }
            } finally {
                tfis.close();
            }
        }
        // do we need umount?
        //TFile.umount(file);
    }

    private static void runNestedDirs(TFile rootFile, List<TFile> fileList) {
        TFile files[] = rootFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            TFile tFile = files[i];
            if (tFile.isDirectory()) {
                runNestedDirs(tFile, fileList);
            } else {
                fileList.add(tFile);
            }
        }
    }
}
