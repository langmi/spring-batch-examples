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
package de.langmi.spring.batch.examples.complex.file.split;

import org.springframework.batch.core.StepContribution;
import java.io.FileNotFoundException;
import org.junit.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.core.io.FileSystemResource;
import static org.junit.Assert.*;

/**
 * Tests for {@link GetLineCountTasklet}.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class GetLineCountTaskletTest {

    private static String INPUT = "src/test/resources/input/simple/input.txt";
    private static final int EXPECTED_COUNT = 20;
    private GetLineCountTasklet tasklet;

    @Test
    public void testExecute() throws Exception {
        // setup
        tasklet = new GetLineCountTasklet();
        tasklet.setResource(new FileSystemResource(INPUT));
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();

        // execute
        RepeatStatus status = tasklet.execute(new StepContribution(stepExecution), new ChunkContext(new StepContext(stepExecution)));
        // assertions
        assertEquals(RepeatStatus.FINISHED, status);
        assertEquals(EXPECTED_COUNT, stepExecution.getExecutionContext().get("line.count"));
    }

    @Test
    public void testExecuteWithNull() throws Exception {
        // setup
        tasklet = new GetLineCountTasklet();
        tasklet.setResource(null);

        // execute
        try {
            tasklet.execute(null, null);
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Resource was null"));
        }
    }

    @Test
    public void testExecuteFileDoesNotExist() throws Exception {
        // setup
        tasklet = new GetLineCountTasklet();
        tasklet.setResource(new FileSystemResource("foobar.txt"));

        // execute
        try {
            tasklet.execute(null, null);
        } catch (Exception e) {
            assertTrue(e instanceof FileNotFoundException);
        }
    }
}
