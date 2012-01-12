/**
 * Copyright 2012 Michael R. Lange <michael.r.lange@langmi.de>.
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
package de.langmi.spring.batch.examples.playground.file.footer.callback;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileFooterCallback;

/**
 * Writer for Groking http://stackoverflow.com/questions/8815958/writing-header-and-footer-to-the-output-file-for-a-database-to-file-job.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class CustomWriter implements ItemWriter<String>, FlatFileFooterCallback {
    
    private ItemWriter<String> delegate;
    private int count = 0;

    @Override
    public void write(List<? extends String> items) throws Exception {
        count = count + items.size();
        System.out.println(count);
        delegate.write(items);
    }

    @Override
    public void writeFooter(Writer writer) throws IOException {
        writer.write("count:" + count);
    }

    public void setDelegate(ItemWriter<String> delegate) {
        this.delegate = delegate;
    }
    
}
