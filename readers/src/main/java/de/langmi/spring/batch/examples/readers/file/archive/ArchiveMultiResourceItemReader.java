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

import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileInputStream;
import de.schlichtherle.truezip.fs.FsSyncException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

/**
 * ArchiveMultiResourceItemReader works with TAR, TAR.GZ, ZIP, B2ZIP, WAR, EAR and JAR 
 * Files. Falls back to normal MultiResourceItemReader behaviour if no archive is set.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class ArchiveMultiResourceItemReader<T> extends MultiResourceItemReader<T> {

    private static final Logger LOG = LoggerFactory.getLogger(ArchiveMultiResourceItemReader.class);
    private Resource[] archives;
    private TFile[] wrappedArchives;
    private FilenameFilter filenameFilter = new DefaultArchiveFileNameFilter();

    /**
     * Tries to extract all files in the archives and adds them as resources to
     * the normal MultiResourceItemReader. Overwrites the Comparator from
     * the super class to get it working with itemstreams.
     *
     * @param executionContext
     * @throws ItemStreamException 
     */
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        // really used with archives?
        if (archives != null) {
            // overwrite the comparator to use description instead of filename
            this.setComparator(new Comparator<Resource>() {

                /** Compares resource descriptions. */
                @Override
                public int compare(Resource r1, Resource r2) {
                    return r1.getDescription().compareTo(r2.getDescription());
                }
            });
            // get the inputStreams from all files inside the archives
            wrappedArchives = new TFile[archives.length];
            List<Resource> extractedResources = new ArrayList<Resource>();
            try {
                for (int i = 0; i < archives.length; i++) {
                    wrappedArchives[i] = new TFile(archives[i].getFile());
                    // iterate over each TFile and get the file list                
                    // extract only the files, ignore directories
                    List<TFile> fileList = new ArrayList<TFile>();
                    runNestedDirs(wrappedArchives[i], fileList, filenameFilter);
                    for (TFile tFile : fileList) {
                        extractedResources.add(new InputStreamResource(new TFileInputStream(tFile), tFile.getName()));
                        LOG.info("using extracted file:" + tFile.getName());
                    }
                }
            } catch (Exception ex) {
                throw new ItemStreamException(ex);
            }
            // propagate extracted resources
            this.setResources(extractedResources.toArray(new Resource[extractedResources.size()]));
        }
        super.open(executionContext);
    }

    /**
     * Calls super.close() and tries to unmount all used archive files afterwards.
     *
     * @throws ItemStreamException 
     */
    @Override
    public void close() throws ItemStreamException {
        super.close();
        if (wrappedArchives != null) {
            for (int i = 0; i < wrappedArchives.length; i++) {
                // release all
                try {
                    TFile.umount(wrappedArchives[i]);
                } catch (FsSyncException ex) {
                    throw new ItemStreamException(ex);
                }
            }
        }
    }

    /**
     * Runs recursively through the rootFile.listFiles() and adds each file to the 
     * fileList. Logs each step - even for directories - to show the archive
     * file contents.
     * 
     * @param rootFile
     * @param fileList 
     * @param filenameFilter
     */
    private static void runNestedDirs(TFile rootFile, List<TFile> fileList, FilenameFilter filenameFilter) {
        TFile files[] = rootFile.listFiles(filenameFilter);
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                TFile tFile = files[i];
                LOG.info("extracting:" + tFile.getAbsolutePath());
                if (tFile.isDirectory()) {
                    runNestedDirs(tFile, fileList, filenameFilter);
                } else {
                    fileList.add(tFile);
                }
            }
        }
    }

    /**
     * Set archive files with normal Spring resources pattern, if not set, the
     * class will fallback to normal MultiResourceItemReader behaviour.
     *
     * @param archives 
     */
    public void setArchives(Resource[] archives) {
        this.archives = archives;
    }

    /**
     * Set a {@link FilenameFilter} to control used files from the archives.
     *
     * @param filenameFilter 
     */
    public void setFilenameFilter(FilenameFilter filenameFilter) {
        this.filenameFilter = filenameFilter;
    }
}
