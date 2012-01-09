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
package de.langmi.spring.batch.examples.playground.file.getcurrentresource;

import java.util.HashMap;
import java.util.Map;
import org.junit.Ignore;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * JobConfigurationTest.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de> 
 */
@ContextConfiguration({
    "classpath*:spring/batch/job/file-getcurrentresource-multiresource-simple-job.xml",
    "classpath*:spring/batch/setup/**/*.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Ignore
public class GetCurrentResourceMultiResourceJobConfigurationTest {

    /** Logger. */
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    /** Lines count from all input files. */
    private static final int READ_COUNT_OVERALL = 40;
    private static final String KEY_CURRENT_RESOURCE = "current.resource";
    /** JobLauncherTestUtils Bean. */
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    /** Launch Test. */
    @Test
    public void launchJob() throws Exception {
        // Job parameters
        Map<String, JobParameter> jobParametersMap = new HashMap<String, JobParameter>();
        jobParametersMap.put("time", new JobParameter(System.currentTimeMillis()));
        jobParametersMap.put("input.file.pattern", new JobParameter("file:src/test/resources/input/getcurrentresource/*.txt"));
        jobParametersMap.put("output.file", new JobParameter("file:target/test-outputs/getcurrentresource/output.txt"));

        // launch the job
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters(jobParametersMap));

        // assert job run status
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

        // output step summaries and check
        for (StepExecution step : jobExecution.getStepExecutions()) {
            LOG.debug(step.getSummary());
            assertEquals("Read Count mismatch, changed input?",
                    READ_COUNT_OVERALL, step.getReadCount());
            assertEquals("Write count mismatch.",
                    READ_COUNT_OVERALL, step.getWriteCount());
            checkCurrentResources(step, 0);
            checkCurrentResources(step, 1);
        }
    }

    /**
     * Check the context for existence of a key/value for current.resource/filename.
     *
     * @param step
     * @param index 
     */
    private void checkCurrentResources(StepExecution step, int index) {
        // check for current resource
        String keyName = KEY_CURRENT_RESOURCE + index;
        assertTrue(step.getExecutionContext().containsKey(keyName));
        assertNotNull(step.getExecutionContext().get(keyName));
        // files follow pattern input0.txt, input1.txt, and so on
        assertTrue(((String) step.getExecutionContext().get(keyName)).contains(index + ".txt"));
    }
}
