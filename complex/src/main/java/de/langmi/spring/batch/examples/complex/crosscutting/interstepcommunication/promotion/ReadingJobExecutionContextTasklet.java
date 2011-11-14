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
package de.langmi.spring.batch.examples.complex.crosscutting.interstepcommunication.promotion;

import de.langmi.spring.batch.examples.complex.crosscutting.interstepcommunication.jobcontext.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Tasklet which accesses the (Job){@link ExecutionContext} directly to 
 * read a value.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 * @see http://stackoverflow.com/questions/8117060/spring-batch-storing-in-jobexecutioncontext-from-tasklet-and-accessing-in-anot
 */
public class ReadingJobExecutionContextTasklet implements Tasklet {

    private static final Logger LOG = LoggerFactory.getLogger(ChangingJobExecutionContextTasklet.class);

    /** {@inheritDoc} */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // pull variable from JobExecutionContext, accesses a unmodifiable map
        String value = (String) chunkContext.getStepContext().getJobExecutionContext().get("value");

        // check for the value
        if (StringUtils.trimToNull(value) != null) {
            LOG.debug("Found value in JobExecutionContext:" + value);
        } else {
            throw new Exception("Did not found value in JobExecutionContext");
        }

        // exit the step
        return RepeatStatus.FINISHED;
    }
}
