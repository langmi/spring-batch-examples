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

import java.io.File;
import java.io.FilenameFilter;

/**
 * ArchiveFileNameFilter decides which files inside the archives should be read.
 * Without setting suffixes, all files will be accepted.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class DefaultArchiveFileNameFilter implements FilenameFilter {

    /** Suffixes for filtering. */
    private String[] suffixes;

    /**
     * If suffixes are set, the filter accepts only files with
     * those suffixes, otherwise all is accepted.
     *
     * @param dir
     * @param name
     * @return 
     */
    @Override
    public boolean accept(File dir, String name) {
        if (name != null) {
            if (suffixes != null && suffixes.length > 0) {
                for (int i = 0; i < suffixes.length; i++) {
                    if (name.endsWith(suffixes[i])) {
                        return true;
                    }
                }
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Set the suffixes for filtering.
     *
     * @param suffixes 
     */
    public void setSuffixes(String[] suffixes) {
        this.suffixes = suffixes;
    }
}
