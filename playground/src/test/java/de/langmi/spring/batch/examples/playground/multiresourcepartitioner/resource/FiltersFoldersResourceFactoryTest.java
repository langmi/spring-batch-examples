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
package de.langmi.spring.batch.examples.playground.multiresourcepartitioner.resource;

import de.langmi.spring.batch.examples.playground.resource.FiltersFoldersResourceFactory;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.core.io.Resource;

/**
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class FiltersFoldersResourceFactoryTest {

    @Test
    public void testSimple() throws Exception {
        Resource[] resources = FiltersFoldersResourceFactory.getInstance("src/test/resources", new ArrayList<String>() {

            {
                add("input");
            }
        });
        assertNotNull(resources);
        assertTrue(resources.length > 0);
    }
}
