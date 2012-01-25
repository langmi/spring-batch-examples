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
package de.langmi.spring.batch.examples.complex.aggregating;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;

/**
 * ItemWriter for AggregatedItems.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class AggregatedItemWriter implements ItemWriter<AggregatedItem>, StepExecutionListener {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private int count = 0;

    @Override
    public void write(List<? extends AggregatedItem> items) throws Exception {        
        LOG.debug("writing:");
        for (AggregatedItem item : items) {
            LOG.debug(item.toString());
            count++;
        }
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        LOG.debug("beforeStep");
        // no-op
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOG.debug("afterStep");
        stepExecution.getExecutionContext().put("real.write.count", count);
        return stepExecution.getExitStatus();
    }
}
