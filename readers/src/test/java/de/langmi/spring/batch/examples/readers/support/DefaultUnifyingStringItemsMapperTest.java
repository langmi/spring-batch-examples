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
package de.langmi.spring.batch.examples.readers.support;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * DefaultStringListMapperTest.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class DefaultUnifyingStringItemsMapperTest {

    private UnifyingItemsMapper<String> mapper = new DefaultUnifyingStringItemsMapper();

    @Test
    public void testMapItems() throws Exception {

        List<String> items = new ArrayList<String>() {

            {
                add("foo");
                add("bar");
                add("foobar");
            }
        };

        assertEquals("foobarfoobar", mapper.mapItems(items));

    }

    @Test
    public void testMapItemsWithNull() throws Exception {
        assertNull(mapper.mapItems(null));
    }

    @Test
    public void testMapItemsWithNullObject() throws Exception {

        List<String> items = new ArrayList<String>() {

            {
                add(null);
            }
        };
        String result = mapper.mapItems(items);
        assertNull(result);

    }
}
