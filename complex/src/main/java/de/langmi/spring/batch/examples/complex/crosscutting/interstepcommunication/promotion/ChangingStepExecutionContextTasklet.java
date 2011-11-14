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

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Tasklet which accesses the (Step){@link ExecutionContext} directly to 
 * set a value for a future step.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 * @see http://stackoverflow.com/questions/8117060/spring-batch-storing-in-jobexecutioncontext-from-tasklet-and-accessing-in-anot
 */
public class ChangingStepExecutionContextTasklet implements Tasklet {

    /** {@inheritDoc} */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // set variable in JobExecutionContext
        chunkContext.getStepContext().getStepExecution().getExecutionContext().put("value", "foo");

        // exit the step
        return RepeatStatus.FINISHED;
    }
}
