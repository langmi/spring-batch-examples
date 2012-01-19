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
package de.langmi.spring.batch.examples.playground;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * JobConfigurationTest without fancy test util.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de> 
 */
public class SimpleJobConfigurationManualTest {

    /** Launch Test. */
    @Test
    public void launchJob() throws Exception {
        // create context
        ConfigurableApplicationContext context =
                new ClassPathXmlApplicationContext(
                "spring/batch/job/in-memory-job.xml",
                "spring/batch/setup/job-context.xml",
                "spring/batch/setup/job-database.xml");
        // get the job
        Job job = (Job) context.getBean("inMemoryJob");
        // get the jobLauncher
        JobLauncher launcher = (JobLauncher) context.getBean("jobLauncher");
        // Job parameters
        Map<String, JobParameter> jobParametersMap = new HashMap<String, JobParameter>();
        jobParametersMap.put("time", new JobParameter(System.currentTimeMillis()));
        jobParametersMap.put("input.file", new JobParameter("file:src/test/resources/input/input.txt"));
        jobParametersMap.put("output.file", new JobParameter("file:target/test-outputs/simple/output.txt"));

        // launch the job
        JobExecution jobExecution = launcher.run(job, new JobParameters(jobParametersMap));
        // assert job run status
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }
}
