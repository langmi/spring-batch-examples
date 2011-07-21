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
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * SimpleRenameFileTaskletStep.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class RenameFilesTaskletStep implements Tasklet {

    private ConcurrentHashMap<String, String> fileNamesMap;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private String outputPath;

    /** */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        for (Entry<String, String> entry : fileNamesMap.entrySet()) {
            LOG.debug("renamed:'" + entry.getKey() + "' to '" + entry.getValue() + "'");
            File oldFile = new File(outputPath + entry.getKey());
            LOG.debug("oldFile exists:" + Boolean.toString(oldFile.exists()));
            oldFile.renameTo(new File(outputPath + entry.getValue()));
            LOG.debug("newFile exists:" + Boolean.toString(oldFile.exists()));
        }

        return RepeatStatus.FINISHED;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public void setFileNamesMap(ConcurrentHashMap<String, String> fileNamesMap) {
        this.fileNamesMap = fileNamesMap;
    }
}
