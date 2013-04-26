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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.springframework.batch.item.file.BufferedReaderFactory;
import org.springframework.core.io.Resource;

/**
 * GZipBufferedReaderFactory provides Resourcehandling of gzip Files and still
 * works with normal flat files.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 * @see <a href="http://php.sabscape.com/blog/?p=281">sabscape.com: Customizing Spring Batch to process zipped files</a>
 */
public class GZipBufferedReaderFactory implements BufferedReaderFactory {

    /** Default value for gzip suffixes. */
    private List<String> gzipSuffixes = new ArrayList<String>() {

        {
            add(".gz");
            add(".gzip");
        }
    };

    /**
     * Creates Bufferedreader for gzip Resource, handles normal resources
     * too.
     * 
     * @param resource
     * @param encoding
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    @Override
    public BufferedReader create(Resource resource, String encoding)
            throws UnsupportedEncodingException, IOException {
        for (String suffix : gzipSuffixes) {
            // test for filename and description, description is used when 
            // handling itemStreamResources
            if (resource.getFilename().endsWith(suffix)
                    || resource.getDescription().endsWith(suffix)) {
                return new BufferedReader(new InputStreamReader(new GZIPInputStream(resource.getInputStream()), encoding));
            }
        }
        return new BufferedReader(new InputStreamReader(resource.getInputStream(), encoding));
    }

    public List<String> getGzipSuffixes() {
        return gzipSuffixes;
    }

    public void setGzipSuffixes(List<String> gzipSuffixes) {
        this.gzipSuffixes = gzipSuffixes;
    }
}
