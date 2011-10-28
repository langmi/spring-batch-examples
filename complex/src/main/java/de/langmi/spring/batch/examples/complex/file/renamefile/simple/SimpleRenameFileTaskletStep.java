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

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.UrlResource;

/**
 * SimpleRenameFileTaskletStep.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class SimpleRenameFileTaskletStep implements Tasklet {

    
    /** */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        
        // get used output file
        String outputFilePath = (String) chunkContext.getStepContext().getJobParameters().get("output.file");
        UrlResource oldFile = new UrlResource(outputFilePath);
        // get desired output file name
        String desiredOutputFilePath = (String) chunkContext.getStepContext().getJobExecutionContext().get("desired.output.file");
        UrlResource newFile = new UrlResource(desiredOutputFilePath);
        
        // rename
        oldFile.getFile().renameTo(newFile.getFile());
        
        return RepeatStatus.FINISHED;
    }
    
}
