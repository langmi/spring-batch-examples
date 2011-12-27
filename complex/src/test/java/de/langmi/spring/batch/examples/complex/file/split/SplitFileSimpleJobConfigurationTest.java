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

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
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
@ContextConfiguration(locations = {
    "classpath*:spring/batch/job/complex/file/split/split-file-simple-job.xml",
    "classpath*:spring/batch/setup/**/*.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SplitFileSimpleJobConfigurationTest {

    /** JobLauncherTestUtils Bean. */
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    private static final int EXPECTED_COUNT = 20;
    private static final String ENCODING_UTF_8 = "UTF-8";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String OUTPUT_FIRST = "target/test-outputs/split-file-simple/output-first.txt";
    private static final String OUTPUT_SECOND = "target/test-outputs/split-file-simple/output-second.txt";

    /** Launch Test. */
    @Test
    public void launchJob() throws Exception {
        // Job parameters
        Map<String, JobParameter> jobParametersMap = new HashMap<String, JobParameter>();
        jobParametersMap.put("time", new JobParameter(System.currentTimeMillis()));
        jobParametersMap.put("input.file", new JobParameter("file:src/test/resources/input/simple/input.txt"));
        jobParametersMap.put("output.file.first", new JobParameter("file:" + OUTPUT_FIRST));
        jobParametersMap.put("output.file.second", new JobParameter("file:" + OUTPUT_SECOND));

        // launch the job
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters(jobParametersMap));

        // assert job run status
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

        // assert read/write counts
        for (StepExecution step : jobExecution.getStepExecutions()) {
            // the first tasklet step logs nothing
            if ("getLineCountStep".equals(step.getStepName())) {
                assertEquals(0, step.getReadCount());
                assertEquals(0, step.getWriteCount());
                // its "one" execution and thus one commit
                assertEquals(1, step.getCommitCount());
            }
            if ("splitFilesStep".equals(step.getStepName())) {
                assertEquals(EXPECTED_COUNT, step.getReadCount());
                assertEquals(EXPECTED_COUNT, step.getWriteCount());
                // commit interval is 5 (see job.xml)
                // commit-count: 20 / 5 = 4
                // but there will be an additional commit for the 
                // last "read but end of data" cycle
                assertEquals(5, step.getCommitCount());
            }
        }

        // check output contains right amount of lines
        assertEquals(EXPECTED_COUNT / 2, getLineCount(OUTPUT_FIRST));
        assertEquals(EXPECTED_COUNT / 2, getLineCount(OUTPUT_SECOND));
    }

    /**
     * Get the line count for a file using Java 1.5 possibilities.
     *
     * @param file
     * @return
     * @throws Exception 
     */
    private static int getLineCount(String file) throws Exception {

        Scanner scanner = null;

        try {
            scanner = new Scanner(new File(file), ENCODING_UTF_8);
            // read lines, not tokens for line-content
            scanner.useDelimiter(LINE_SEPARATOR);
            int count = 0;
            while (scanner.hasNextLine()) {
                scanner.nextLine();
                count++;
            }
            return count;
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }
}
