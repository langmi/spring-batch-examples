/**
 * Copyright 2012 Michael R. Lange <michael.r.lange@langmi.de>.
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
package de.langmi.spring.batch.examples.basics.taskletstep;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Simple test for {@link SimpleTaskletStep}.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
* @see <a href="http://stackoverflow.com/questions/1119385/junit-test-for-system-out-println">JUnit test for system.out.println</a>
 */
public class SimpleTaskletStepTest {

    /** Stream for catching System.out. */
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final Tasklet tasklet = new SimpleTaskletStep();

    @Test
    public void testExecute() throws Exception {
        // run the taskletStep, no need for contexts here
        assertEquals(RepeatStatus.FINISHED, tasklet.execute(null, null));
        // assert sysoutput
        assertEquals("Hello World!", outContent.toString());
    }

    @Before
    public void setUpStreams() {
        // catch system out
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void cleanUpStreams() {
        // reset JVM standard
        System.setOut(null);
    }
}
