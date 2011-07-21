/*
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
package de.langmi.spring.batch.examples.renamefiles.generic;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
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
        String businessKey = (String) stepExecution.getExecutionContext().get("business.key");
        try {
            String path = this.outputFileResource.getFile().getParent();
            String newFileName = path + File.separator + businessKey + ".txt";
            if (outputFileResource.getFile().renameTo(new File(newFileName))) {
                LOG.debug("renamed:" + this.outputFileResource.getFilename() + " to:" + newFileName);
            } else {
                throw new RuntimeException("renaming failed");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return stepExecution.getExitStatus();
    }

    public void setOutputFileResource(Resource outputFileResource) {
        this.outputFileResource = outputFileResource;
    }
}
