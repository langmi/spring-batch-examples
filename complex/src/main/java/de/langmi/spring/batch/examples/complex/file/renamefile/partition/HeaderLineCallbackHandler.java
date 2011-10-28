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
package de.langmi.spring.batch.examples.complex.file.renamefile.partition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.LineCallbackHandler;

/**
 * HeaderLineCallbackHandler handles header line from file.
 * Is not threadsafe, use with scope="step".
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class HeaderLineCallbackHandler implements LineCallbackHandler, StepExecutionListener {

    private StepExecution stepExecution;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    /**
     * Handles header line and saves business key to step execution context.
     * 
     * @param line 
     */
    @Override
    public void handleLine(String line) {
        // promote line as business key for output file
        stepExecution.getExecutionContext().put(BatchConstants.CONTEXT_NAME_BUSINESS_KEY, line);
        LOG.info("business key created:'" + line + "'");        
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
