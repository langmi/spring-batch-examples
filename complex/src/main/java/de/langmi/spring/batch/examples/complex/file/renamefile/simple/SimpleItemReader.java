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
package de.langmi.spring.batch.examples.complex.file.renamefile.simple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * SimpleItemReader - is not threadsafe, uses testdata.
 * Takes the first item to create the file name to be used
 * for renaming the original output file.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class SimpleItemReader implements ItemStream, ItemReader<String> {

    private List<String> testData;
    private Iterator<String> iterator;
    private boolean desiredOutputFilePathCreated = false;
    private StepExecution stepExecution;

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (iterator.hasNext()) {
            String item = iterator.next();
            if (!desiredOutputFilePathCreated) {
                stepExecution.getExecutionContext().put("desired.output.file", "file:target/test-outputs/rename-files-simple/output-" + item + ".txt");
                desiredOutputFilePathCreated = true;
            }
            return item;
        } else {
            return null;
        }
    }

    /** Opens the Iterator for the testdata. */
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.testData = createTestData(20);
        this.iterator = testData.iterator();
    }

    /** Method without function */
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        // no-op
    }

    /** Method without function */
    @Override
    public void close() throws ItemStreamException {
        // no-op
    }

    /**
     * Creates some testdata.
     *
     * @param count
     * @return 
     */
    private List<String> createTestData(int count) {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            data.add(String.valueOf(i));
        }
        return data;
    }

    /**
     * Sets the stepExecution.
     * 
     * @param stepExecution 
     */
    @BeforeStep
    public void setStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
}
