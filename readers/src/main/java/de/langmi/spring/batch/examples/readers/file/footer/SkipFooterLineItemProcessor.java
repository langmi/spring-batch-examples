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
package de.langmi.spring.batch.examples.readers.file.footer;

import org.springframework.batch.item.ItemProcessor;

/**
 * Simple Itemprocessor which ignores a specific footer line.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 * @see http://forum.springsource.org/showthread.php?120099-Any-way-to-skip-the-first-and-last-line-of-the-input-file-when-reading-a-txt-file
 */
public class SkipFooterLineItemProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String item) throws Exception {
        if (item != null && item.contains("footer")) {
            return null;
        } else {
            return item;
        }
    }
}
