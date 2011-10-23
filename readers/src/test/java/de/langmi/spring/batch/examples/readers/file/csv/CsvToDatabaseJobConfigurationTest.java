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
package de.langmi.spring.batch.examples.readers.file.csv;

import org.junit.After;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import static org.junit.Assert.*;
import org.junit.Before;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * JobConfigurationTest.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de> 
 */
@ContextConfiguration({
    "classpath*:spring/batch/job/readers/file/file-csv-to-database-job.xml",
    "classpath*:spring/batch/setup/**/*.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class CsvToDatabaseJobConfigurationTest {

    /** Logger. */
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    /** CREATE statement for BUSINESS_OBJECTS table. */
    private static final String CREATE_TABLE_SQL = "CREATE TABLE BUSINESS_OBJECTS (ID INTEGER IDENTITY, ATTRIBUTE VARCHAR (100))";
    private static final String DELETE_TABLE_SQL = "DROP TABLE BUSINESS_OBJECTS";
    private static final String COUNT_SQL = "SELECT COUNT(*) FROM BUSINESS_OBJECTS";
    /** Lines count from input file. */
    private static final int EXPECTED_COUNT = 20;
    /** JobLauncherTestUtils Bean. */
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    /** Launch Test. */
    @Test
    public void launchJob() throws Exception {
        // Job parameters
        Map<String, JobParameter> jobParametersMap = new HashMap<String, JobParameter>();
        jobParametersMap.put("time", new JobParameter(System.currentTimeMillis()));
        jobParametersMap.put("input.file", new JobParameter("file:src/test/resources/input/file/csv/input.csv"));
        jobParametersMap.put("output.file", new JobParameter("file:target/test-outputs/readers/file/csv-to-database/output.txt"));

        // launch the job
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters(jobParametersMap));

        // assert job run status
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

        // output step summaries
        for (StepExecution step : jobExecution.getStepExecutions()) {
            LOG.debug(step.getSummary());
            assertEquals("Read Count mismatch, changed input?",
                         EXPECTED_COUNT, step.getReadCount());
        }
        
        // assert items are written successfully to database
        assertEquals(EXPECTED_COUNT, jdbcTemplate.queryForInt(COUNT_SQL));        
    }

    @Before
    public void before() throws Exception {
        // provide jdbc template for setup and later assertions
        jdbcTemplate = new JdbcTemplate(dataSource);
        // setup business data table
        jdbcTemplate.execute(CREATE_TABLE_SQL);
    }

    @After
    public void after() throws Exception {
        // clear table
        jdbcTemplate.execute(DELETE_TABLE_SQL);
    }
}
