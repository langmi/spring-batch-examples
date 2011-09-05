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
package de.langmi.spring.batch.examples.readers.gzip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import org.springframework.batch.item.file.BufferedReaderFactory;
import org.springframework.core.io.Resource;

/**
 * GZipBufferedReaderFactory provides Resourcehandling of gzip Files.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 * @see <a href="http://php.sabscape.com/blog/?p=281">sabscape.com: Customizing Spring Batch to process zipped files</a>
 */
public class GZipBufferedReaderFactory implements BufferedReaderFactory {

    /**
     * Creates Bufferedreader for gzip Resource.
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
        return new BufferedReader(new InputStreamReader(new GZIPInputStream(resource.getInputStream()), encoding));
    }
}
