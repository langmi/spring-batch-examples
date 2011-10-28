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
package de.langmi.spring.batch.examples.complex.file.renamefile.partition.extrastep;

import de.langmi.spring.batch.examples.complex.file.renamefile.partition.BatchConstants;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * PropagateFileNamesListener puts the current business key for the future
 * desired output file and the current output file name in a HashMap bean.
 *
 * Is not threadsafe ! Use with scope="step". 
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class PropagateFileNamesListener implements StepExecutionListener {

    private ConcurrentHashMap<String, String> fileNames;
    private String outputFile;

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        // get business key
        String businessKey = (String) stepExecution.getExecutionContext().get(BatchConstants.CONTEXT_NAME_BUSINESS_KEY);
        this.fileNames.put(outputFile, businessKey);

        return stepExecution.getExitStatus();
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // no-op
    }

    public void setFileNames(ConcurrentHashMap<String, String> fileNames) {
        this.fileNames = fileNames;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }
}
