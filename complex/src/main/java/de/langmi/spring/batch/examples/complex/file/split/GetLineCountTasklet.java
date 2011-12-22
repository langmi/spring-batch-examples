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
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

/**
 * Simple {@link Tasklet} which gets the line count from a file.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class GetLineCountTasklet implements Tasklet {

    private Resource resource;

    /**
     * Check if resouce is a file and get the line count from the file.
     * Line count is 
     *
     * @param contribution
     * @param chunkContext
     * @return
     * @throws Exception 
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        checkResource();
        int lineCount = getLineCount(resource.getFile());
        
        chunkContext.getStepContext().getStepExecution().getExecutionContext().put("line.count", lineCount);

        return RepeatStatus.FINISHED;
    }

    /**
     * Short variant to get the line count of the file, there can be problems with files where
     * the last line has no content.
     *
     * @param file
     * @param chunkContext
     * @return the line count
     * @throws IOException 
     */
    private int getLineCount(File file) throws IOException {
        // get line count
        LineNumberReader lnr = null;
        try {
            lnr = new LineNumberReader(new FileReader(file));
            lnr.skip(Long.MAX_VALUE);
            return lnr.getLineNumber();
        } finally {
            if (lnr != null) {
                lnr.close();
            }
        }
    }

    /**
     * Checks if the resource is a file.
     *
     * @return
     * @throws RuntimeException 
     */
    private File checkResource() throws RuntimeException {
        File file;
        // resource check
        try {
            file = resource.getFile();
        } catch (IOException e) {
            throw new RuntimeException("Could not convert resource to file: [" + resource + "]", e);
        }
        return file;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
