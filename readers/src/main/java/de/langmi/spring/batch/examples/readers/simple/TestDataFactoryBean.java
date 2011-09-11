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
package de.langmi.spring.batch.examples.readers.simple;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.FactoryBean;

/**
 * TestDataFactoryBean provides simple List Testdata, configured as prototype, <br />
 * because one of the readers exhausts the list while reading.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class TestDataFactoryBean implements FactoryBean<List<String>> {

    public static final int COUNT = 20;

    @Override
    public List<String> getObject() throws Exception {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < COUNT; i++) {
            data.add(String.valueOf(i));
        }
        return data;
    }

    @Override
    public Class<?> getObjectType() {
        return List.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
