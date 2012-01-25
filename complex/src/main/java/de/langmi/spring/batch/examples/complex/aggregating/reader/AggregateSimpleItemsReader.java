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
package de.langmi.spring.batch.examples.complex.aggregating.reader;

import de.langmi.spring.batch.examples.complex.aggregating.AggregatedItem;
import de.langmi.spring.batch.examples.complex.support.SimpleItem;
import org.springframework.batch.item.ItemReader;

/**
 * AggregateSimpleItemsReader wraps another reader, aggregates the data and creates
 * an AggregatedItem.
 * 
 * Is stateful.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 * @see http://stackoverflow.com/questions/8837487/how-to-process-logically-related-rows-after-itemreader-in-springbatch
 */
public class AggregateSimpleItemsReader implements ItemReader<AggregatedItem> {

    /** Wrapped reaer. */
    private ItemReader<SimpleItem> delegate;
    private AggregatedItem tempData = null;
    private boolean isReaderExhausted = false;

    @Override
    public AggregatedItem read() throws Exception {
        AggregatedItem returnValue = null;
        // read delegate until one AggregatedItem is complete or reader is exhausted
        while (!isReaderExhausted) {
            SimpleItem sItem = delegate.read();

            if (sItem != null) {
                // first run?
                if (tempData == null) {
                    tempData = new AggregatedItem(sItem.getSharedId());
                }
                // same shared id ? add value 
                if (tempData.getId() == sItem.getSharedId()) {
                    tempData.add(sItem.getValue());
                } else {
                    // set returnvalue
                    returnValue = new AggregatedItem(tempData.getId(), tempData.getSum());
                    // create new tempData
                    tempData = new AggregatedItem(sItem.getSharedId(), sItem.getValue());
                    // break
                    break;
                }
            } else {
                returnValue = tempData;
                isReaderExhausted = true;
            }
        }
        return returnValue;
    }

    public void setDelegate(ItemReader<SimpleItem> delegate) {
        this.delegate = delegate;
    }
}
