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
package de.langmi.spring.batch.examples.complex.skip.simple;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * SkipJobListener.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class SkipJobListener implements
        JobExecutionListener,
        SkipListener<String, String>,
        StepExecutionListener,
        ChunkListener,
        ItemWriteListener<String>,
        ItemReadListener<String> {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void beforeJob(JobExecution jobExecution) {
        LOG.debug("before job");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        LOG.debug("after job");
        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            LOG.debug(stepExecution.getSummary());
        }
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        LOG.debug("before step");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOG.debug("after step");
        return stepExecution.getExitStatus();
    }

    @Override
    public void beforeChunk() {
        LOG.debug("before chunk");
    }

    @Override
    public void afterChunk() {
        LOG.debug("after chunk");
    }

    @Override
    public void beforeWrite(List<? extends String> items) {
        LOG.debug("before write:" + items.toString());
    }

    @Override
    public void afterWrite(List<? extends String> items) {
        LOG.debug("after write:" + items.toString());
    }

    @Override
    public void onWriteError(Exception exception, List<? extends String> items) {
        LOG.debug("on write error");
    }

    @Override
    public void onSkipInRead(Throwable t) {
        LOG.debug("on skip in read");
    }

    @Override
    public void onSkipInWrite(String item, Throwable t) {
        LOG.debug("on skip in write:" + item);
    }

    @Override
    public void onSkipInProcess(String item, Throwable t) {
        LOG.debug("on skip in process:" + item);
    }

    @Override
    public void beforeRead() {
        LOG.debug("before read");
    }

    @Override
    public void afterRead(String item) {
        LOG.debug("after read:" + item);
    }

    @Override
    public void onReadError(Exception ex) {
        LOG.debug("on read error");
    }
}
