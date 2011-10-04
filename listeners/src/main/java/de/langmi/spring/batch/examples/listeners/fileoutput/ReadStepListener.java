/**
 * Copyright 2011 Micha.
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
package de.langmi.spring.batch.examples.listeners.fileoutput;

import java.util.ArrayList;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.item.ItemWriter;

/**
 * Step Listener which writes skipped items to a file.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class ReadStepListener implements ItemReadListener<String> {
    
    private ItemWriter<String> readItemWriter;
    
    @Override
    public void beforeRead() {
        // no-op
    }

    @Override
    public void afterRead(final String t) {
        try {
            this.readItemWriter.write(new ArrayList<String>(){{
                add(t);
            }});
        } catch (Exception ex) {
           // handle exception
        }
    }

    @Override
    public void onReadError(Exception excptn) {
        // no-op
    }

    public void setReadItemWriter(ItemWriter<String> readItemWriter) {
        this.readItemWriter = readItemWriter;
    }
}
