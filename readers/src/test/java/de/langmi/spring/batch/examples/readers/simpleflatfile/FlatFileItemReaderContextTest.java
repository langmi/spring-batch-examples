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
package de.langmi.spring.batch.examples.readers.simpleflatfile;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
@ContextConfiguration({
    "classpath*:spring/batch/job/simple-flatfile-job.xml",
    "classpath*:spring/batch/setup/**/*.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class FlatFileItemReaderContextTest {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    @Autowired
    private ItemStreamReader<String> itemReaderStream;
    private static final int EXPECTED_COUNT = 20;

    /**
     * Test should read succesfully.
     *
     * @throws Exception 
     */
    @Test
    public void testSuccessfulReading() throws Exception {
        // setup
        Map<String, JobParameter> jobParametersMap = new HashMap<String, JobParameter>();
        jobParametersMap.put("time", new JobParameter(System.currentTimeMillis()));
        jobParametersMap.put("input.file", new JobParameter("file:src/test/resources/input/input.txt"));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution(new JobParameters(jobParametersMap));

        int count = StepScopeTestUtils.doInStepScope(execution, new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {

                int count = 0;

                itemReaderStream.open(new ExecutionContext());

                try {
                    while (itemReaderStream.read() != null) {
                        count++;
                    }
                } finally {
                    itemReaderStream.close();
                }
                return count;

            }
        });
        assertEquals(EXPECTED_COUNT, count);
    }
}
