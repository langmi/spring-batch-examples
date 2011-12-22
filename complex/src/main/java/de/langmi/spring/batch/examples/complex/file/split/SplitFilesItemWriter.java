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
package de.langmi.spring.batch.examples.complex.file.split;

import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.item.ItemWriter;

/**
 * A simple {@link ItemWriter} which splits all items in half and writes each
 * half to an ItemWriter.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class SplitFilesItemWriter implements ItemWriter<String> {

    private ItemWriter<String> firstWriter;
    private ItemWriter<String> secondWriter;
    private int inputLineCount;
    private int readCount = 0;

    @Override
    public void write(List<? extends String> items) throws Exception {
        List<String> itemsFirst = new ArrayList<String>();
        List<String> itemsSecond = new ArrayList<String>();

        for (String item : items) {
            // catch all items
            if (readCount < (inputLineCount / 2)) {
                itemsFirst.add(item);
            } else {
                itemsSecond.add(item);
            }
            readCount++;
        }
        if (itemsFirst.size() > 0) {
            firstWriter.write(items);
        }
        if (itemsSecond.size() > 0) {
            secondWriter.write(items);
        }
    }

    public void setInputLineCount(int inputLineCount) {
        this.inputLineCount = inputLineCount;
    }

    public void setFirstWriter(ItemWriter<String> firstWriter) {
        this.firstWriter = firstWriter;
    }

    public void setSecondWriter(ItemWriter<String> secondWriter) {
        this.secondWriter = secondWriter;
    }
}
