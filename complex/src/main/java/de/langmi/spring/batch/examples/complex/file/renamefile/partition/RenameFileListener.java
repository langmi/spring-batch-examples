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
package de.langmi.spring.batch.examples.complex.file.renamefile.partition;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.core.io.Resource;

/**
 * RenameFileListener - renames outputFileResource to a new name with relation
 * to a stepcontext-provided business key. Is not threadsafe! Use with scope="step".
 * 
 * The renaming is actually a "copy and delete old file later" implementation.
 * Real renaming does not work inside one step, due to spring batch calling 
 * stream.close after 'afterStep'. It would work with unix/linux operation systems,
 * but not with windows.
 * So i use file.deleteOnExit which works on all operation systems.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class RenameFileListener implements StepExecutionListener {

    private Resource outputFileResource;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // no-op
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        // get business key
        String businessKey = (String) stepExecution.getExecutionContext().get(BatchConstants.CONTEXT_NAME_BUSINESS_KEY);
        try {
            String path = this.outputFileResource.getFile().getParent();
            String newFilePathAndName = path + File.separator + businessKey + ".txt";
            FileUtils.copyFile(outputFileResource.getFile(), new File(newFilePathAndName));
            LOG.info("copied:" + this.outputFileResource.getFile().getPath() + " to:" + newFilePathAndName);
            // deletion here is not good, the itemstream will be closed after 
            // this afterStep method is called
            // so get it deleted on jvm exit
            this.outputFileResource.getFile().deleteOnExit();
            LOG.info("deleteOnExit for:" + this.outputFileResource.getFile().getPath());
        } catch (Exception ex) {
            return new ExitStatus(ExitStatus.FAILED.getExitCode(), ex.getMessage());
        }

        return stepExecution.getExitStatus();
    }

    public void setOutputFileResource(Resource outputFileResource) {
        this.outputFileResource = outputFileResource;
    }
}
