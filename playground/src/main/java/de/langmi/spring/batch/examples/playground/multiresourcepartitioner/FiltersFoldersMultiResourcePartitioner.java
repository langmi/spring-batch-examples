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
package de.langmi.spring.batch.examples.playground.multiresourcepartitioner;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * Slightly customized MultiResourcePartitioner, provides a constructor which 
 * sets the resources according to some arguments.
 * Created for question at stackoverflow: http://stackoverflow.com/questions/7953550/multiresourceparitioner-in-spring-batch-to-accept-files-from-multiple-folders.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class FiltersFoldersMultiResourcePartitioner extends MultiResourcePartitioner {

    private static final Logger LOG = LoggerFactory.getLogger(FiltersFoldersMultiResourcePartitioner.class);

    public FiltersFoldersMultiResourcePartitioner(final String filePath, final String acceptedFolder) throws Exception {
        this(filePath, new ArrayList<String>() {

            {
                add(acceptedFolder);
            }
        });
    }

    public FiltersFoldersMultiResourcePartitioner(final String filePath, final List<String> acceptedFolders) throws Exception {
        LOG.debug("entering MRP");
        final List<FileSystemResource> files = new ArrayList<FileSystemResource>();
        runNestedDirs(new File(filePath), files, new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                // accept all files, made easy here, files shall contain a suffix separator
                if (name.contains(".")) {
                    return true;
                } else if (acceptedFolders.contains(name)) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        this.setResources(files.toArray(new Resource[0]));
    }

    /**
     * Runs recursively through the rootFile.listFiles() and adds each file to the 
     * fileList.
     * 
     * @param rootFile
     * @param fileList 
     * @param filenameFilter
     */
    private static void runNestedDirs(File rootFile, List<FileSystemResource> fileList, FilenameFilter filenameFilter) {
        File files[] = rootFile.listFiles(filenameFilter);
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isDirectory()) {
                    runNestedDirs(file, fileList, filenameFilter);
                } else {
                    LOG.debug("adding:" + file.getName());
                    fileList.add(new FileSystemResource(file));
                }
            }
        }
    }
}
