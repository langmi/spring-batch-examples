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
package de.langmi.spring.batch.examples.readers.file.fieldcount;

import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * FieldCountItemReader, constructs line by reading from wrapped reader, until
 * field count is reached.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class FieldCountItemReader implements ItemReader<List<String>> {

    private ItemReader<FieldSet> delegate;
    private int count;

    /** {@inheritDoc} */
    @Override
    public List<String> read() throws Exception {
        List<String> fields = new ArrayList<String>();
        FieldSet field = null;
        // read until end of file
        while ((field = delegate.read()) != null) {
            String[] fieldSetValues = field.getValues();
            for (int i = 0; i < fieldSetValues.length; i++) {
                fields.add(field.getValues()[i]);
            }
            // field count reached ?
            if (fields.size() != count) {
                continue;
            } else {
                return fields;
            }
        }
        // reader returned nothing
        return null;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setDelegate(ItemReader<FieldSet> delegate) {
        this.delegate = delegate;
    }
}
