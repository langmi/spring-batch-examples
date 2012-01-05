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
package de.langmi.spring.batch.examples.playground.file.getcurrentresource;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.MultiResourceItemReader;

/**
 * Simple ChunkListener for logging the current resource from a <br />
 * injected {@link MultiResourceItemReader}. Acts as StepExeuctionListener too
 * to save value to the StepExecutionContext, value is taken later in Test to
 * see if it really worked to get the current resource.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class GetCurrentResourceChunkListener implements ChunkListener, StepExecutionListener {

    private MultiResourceItemReader mrir;
    private StepExecution stepExecution;

    public void setMrir(MultiResourceItemReader mrir) {
        this.mrir = mrir;
    }

    @Override
    public void beforeChunk() {
        if (mrir != null && mrir.getCurrentResource() != null) {
            stepExecution.getExecutionContext().put("current.resource", mrir.getCurrentResource().getFilename());
        }
    }

    @Override
    public void afterChunk() {
        // no-op
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        // no-op
        return stepExecution.getExitStatus();
    }
}
