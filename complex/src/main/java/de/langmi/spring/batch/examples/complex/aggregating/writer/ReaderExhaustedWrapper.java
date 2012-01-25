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
package de.langmi.spring.batch.examples.complex.aggregating.writer;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;

/**
 * A Reader Implementation for 
 * http://stackoverflow.com/questions/8837487/how-to-process-logically-related-rows-after-itemreader-in-springbatch.
 * 
 * Wraps another reader and peeks ahead for each read to know beforehand if the 
 * delegate reader is exhausted. Propagates the information to the StepExecutionContext.
 * 
 * Is stateful and not threadsafe.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class ReaderExhaustedWrapper<T> implements ItemReader<T>, StepExecutionListener {

    private ItemReader<T> delegate;
    private StepExecution stepExecution;
    private T nextItem = null;
    private boolean delegateExhausted = false;

    @Override
    public T read() throws Exception {
        // check if delegate already exhausted to avoid an unnecessary delegate.read()
        if (!delegateExhausted) {
            T returnItem;
            // next filled ? 
            if (nextItem != null) {
                returnItem = nextItem;
                nextItem = null;
            } else {
                // standard read
                returnItem = delegate.read();
            }

            // try to peek one item ahead
            nextItem = delegate.read();
            // last item reached?
            if (nextItem == null) {
                stepExecution.getExecutionContext().put("readerExhausted", Boolean.TRUE);
                delegateExhausted = true;
            }

            return returnItem;
        } else {
            return null;
        }
    }

    public void setDelegate(ItemReader<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return stepExecution.getExitStatus();
    }
}
