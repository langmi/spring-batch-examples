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
package de.langmi.spring.batch.examples.readers.zip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipInputStream;
import org.springframework.batch.item.file.BufferedReaderFactory;
import org.springframework.core.io.Resource;

/**
 * ZipBufferedReaderFactory provides Resourcehandling of zip Files.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class ZipBufferedReaderFactory implements BufferedReaderFactory {

    /**
     * Creates Bufferedreader for zip Resource.
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
        return new BufferedReader(new InputStreamReader(new ZipInputStream(resource.getInputStream()), encoding));
    }
}
