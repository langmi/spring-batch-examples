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
package de.langmi.spring.batch.examples.readers.support;

import java.util.List;

/**
 * Default implementation which just appends each object.toString() together,
 * thus unifying all strings.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class DefaultUnifyingStringItemsMapper implements UnifyingItemsMapper<String> {

    /** {@inheritDoc} */
    @Override
    public String mapItems(List<?> items) throws Exception {
        if (items != null && items.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Object item : items) {
                if (item != null) {
                    sb.append(item);
                }
            }
            if (sb.length() > 0) {
                return sb.toString();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
