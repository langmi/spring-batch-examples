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
package de.langmi.spring.batch.examples.complex.file.renamefile.partition.extrastep;

import java.io.File;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Works with value map to rename files according to business keys.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class RenameFilesTasklet implements Tasklet {

    private ConcurrentHashMap<String, String> fileNames;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private static final String FILE_PREFIX = "file:";

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        for (Entry<String, String> entry : fileNames.entrySet()) {
            String oldFileName = entry.getKey();
            // remove "file:" part, sometimes there due to spring resource patterns
            if (oldFileName.contains(FILE_PREFIX)) {
                oldFileName = oldFileName.replace(FILE_PREFIX, "");
            }
            // old file name
            File oldFile = new File(oldFileName);
            String path = oldFile.getParent();
            String newFilePathAndName = createOutputFileName(path, entry);

            // rename file
            File newFile = new File(newFilePathAndName);
            oldFile.renameTo(newFile);

            LOG.info("renamed:" + oldFile.getPath() + " to:" + newFilePathAndName);
        }

        return RepeatStatus.FINISHED;
    }

    /**
     * Takes the old name and the business key to create a new output file name.
     *
     * @param path
     * @param entry
     * @return 
     */
    private String createOutputFileName(String path, Entry<String, String> entry) {

        StringBuilder sb = new StringBuilder(path);
        sb.append(File.separator);
        sb.append(entry.getValue());
        sb.append(".txt");
        
        return sb.toString();
    }

    public void setFileNames(ConcurrentHashMap<String, String> fileNames) {
        this.fileNames = fileNames;
    }
}
