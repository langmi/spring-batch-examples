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
package de.langmi.spring.batch.examples.readers.file.flatfileitemreader;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * Tests the FlatFileItemReader from Spring Batch with Application Context with
 * StepScopeTestExecutionListener.
 * <br />
 * By using StepScopeTestExecutionListener we need to provide the other "default"
 * listeners too.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 * @see <a href="http://static.springsource.org/spring-batch/reference/html/testing.html#d0e7538">Spring Batch Testing</a>
 */
@ContextConfiguration({
    "classpath*:spring/batch/job/readers/file/file-flatfileitemreader-job.xml",
    "classpath*:spring/batch/setup/**/*.xml"})
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    StepScopeTestExecutionListener.class,
    TransactionalTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class FlatFileItemReaderContextStepScopeTest {

    /** Reader under test. */
    @Autowired
    private ItemStreamReader<String> itemReaderStream;
    private StepExecution execution;
    private static final int EXPECTED_COUNT = 20;

    /**
     * Test should read succesfully.
     *
     * @throws Exception 
     */
    @Test
    public void testSuccessfulReading() throws Exception {
        // open manually
        itemReaderStream.open(execution.getExecutionContext());
        // read
        try {
            int count = 0;
            while (itemReaderStream.read() != null) {
                count++;
            }
            assertEquals(EXPECTED_COUNT, count);
        } catch (Exception e) {
            throw e;
        } finally {
            itemReaderStream.close();
        }
    }

    /**
     * Mandatory method for stepScope Test.
     *
     * @return
     * @see StepScopeTestExecutionListener
     */
    public StepExecution getStepExection() {
        Map<String, JobParameter> jobParametersMap = new HashMap<String, JobParameter>();
        jobParametersMap.put("time", new JobParameter(System.currentTimeMillis()));
        jobParametersMap.put("input.file", new JobParameter("file:src/test/resources/input/file/simple/input.txt"));
        execution = MetaDataInstanceFactory.createStepExecution(new JobParameters(jobParametersMap));
        return execution;
    }
}
