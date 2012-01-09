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

import java.util.ArrayList;
import java.util.List;
import org.springframework.aop.framework.Advised;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.MultiResourceItemReader;

/**
 * Simple Listener for logging the current resource from a <br />
 * injected {@link MultiResourceItemReader}. Saves the value to the StepExecutionContext, 
 * value is taken later in Test to see if it really worked to get the current resource.
 * <br />
 * To get it working with a step scoped MultiResourceItemReader i access the 
 * proxy directly, see http://forum.springsource.org/showthread.php?120775-Accessing-the-currently-processing-filename,
 * https://gist.github.com/1582202 and https://jira.springsource.org/browse/BATCH-1831.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class GetCurrentResourceChunkListener implements ChunkListener, StepExecutionListener {

    private StepExecution stepExecution;
    private Object proxy;
    private List<String> fileNames = new ArrayList<String>();
    private MultiResourceItemReader test;

    public void setTest(MultiResourceItemReader test) {
        this.test = test;
    }

    public void setProxy(Object mrir) {
        this.proxy = mrir;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return stepExecution.getExitStatus();
    }

    @Override
    public void beforeChunk() {
        if (proxy instanceof Advised) {
            try {
                Advised advised = (Advised) proxy;
                Object obj = advised.getTargetSource().getTarget();
                MultiResourceItemReader mrirTarget = (MultiResourceItemReader) obj;
                if (mrirTarget != null
                        && mrirTarget.getCurrentResource() != null
                        && !fileNames.contains(mrirTarget.getCurrentResource().getFilename())) {
                    String fileName = mrirTarget.getCurrentResource().getFilename();
                    fileNames.add(fileName);
                    String index = String.valueOf(fileNames.indexOf(fileName));
                    stepExecution.getExecutionContext().put("current.resource" + index, fileName);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void afterChunk() {
        // no-op
    }
}
