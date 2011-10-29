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
package de.langmi.spring.batch.examples.readers.file.multiresourcepartitioner;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
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
    "classpath*:spring/batch/job/readers/file/file-multiresourcepartitioner-simple-job.xml",
    "classpath*:spring/batch/setup/**/*.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleMultiResourcePartitionerJobConfigurationTest {

    private static final int READ_COUNT_PER_FILE = 20;
    private static final int READ_COUNT_OVERALL = 40;
    private static final int STEP_COUNT = 3;
    /** JobLauncherTestUtils Bean. */
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    /** Launch Test. */
    @Test
    public void launchJob() throws Exception {
        // Job parameters
        Map<String, JobParameter> jobParametersMap = new HashMap<String, JobParameter>();
        jobParametersMap.put("time", new JobParameter(System.currentTimeMillis()));
        jobParametersMap.put("input.file.pattern", new JobParameter("file:src/test/resources/input/file/multiresource/*.txt"));
        jobParametersMap.put("output.dir", new JobParameter("file:target/test-outputs/readers/file/multiresourcepartitioner-simple/"));

        // launch the job
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters(jobParametersMap));

        // assert job run status
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

        assertEquals(STEP_COUNT, jobExecution.getStepExecutions().size());

        // assert step meta data
        boolean partitionStepFound = false;
        boolean childrenStepFound = false;
        for (StepExecution step : jobExecution.getStepExecutions()) {
            // spring batch works with 3 "steps" here, the PartitionStep itself 
            // and the created children
            if ("businessStep".equals(step.getStepName())) {
                assertEquals("Read Count mismatch, changed input?",
                        READ_COUNT_OVERALL, step.getReadCount());
                assertEquals("Write count mismatch.",
                        READ_COUNT_OVERALL, step.getWriteCount());
                partitionStepFound = true;
            }
            // the children steps follow the pattern 
            // "<stepName>:partition:<partition-id>"
            if (step.getStepName().contains("concreteBusinessStep:partition")) {
                assertEquals("Read Count mismatch, changed input?",
                        READ_COUNT_PER_FILE, step.getReadCount());
                assertEquals("Write count mismatch.",
                        READ_COUNT_PER_FILE, step.getWriteCount());
                childrenStepFound = true;
            }
        }
        assertTrue("Changed step names?", partitionStepFound && childrenStepFound);
    }
}
