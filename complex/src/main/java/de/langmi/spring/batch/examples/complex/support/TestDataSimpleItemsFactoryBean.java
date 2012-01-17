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
package de.langmi.spring.batch.examples.complex.support;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.FactoryBean;

/**
 * Provides List with test data.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class TestDataSimpleItemsFactoryBean implements FactoryBean<List<SimpleItem>> {

    /** Public to make it usable for test assertions. */
    public static final int COUNT = 20;

    @Override
    public List<SimpleItem> getObject() throws Exception {
        List<SimpleItem> data = new ArrayList<SimpleItem>();
        for (int i = 0; i < COUNT; i++) {
            data.add(new SimpleItem(i, String.valueOf(i)));
        }
        return data;
    }

    @Override
    public Class<?> getObjectType() {
        return List.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
