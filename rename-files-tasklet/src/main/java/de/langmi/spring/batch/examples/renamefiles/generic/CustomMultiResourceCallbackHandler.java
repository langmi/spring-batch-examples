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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.Resource;

/**
 * Default callback implementation.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class CustomMultiResourceCallbackHandler extends DefaultMultiResourceCallbackHandler {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handleContextParametersSetup(int partition, ExecutionContext context, Resource resource) {
        // i want the standard way for propagating the complete file path, but
        // with a different name
        super.setKeyName("inputFilePath");
        super.handleContextParametersSetup(partition, context, resource);
        // and the desired output file name
        String outputFileName = "output-" + String.valueOf(partition) + ".txt";
        context.put("outputFileName", outputFileName);
        LOG.info("for inputfile:'" + resource.getFilename() + "' outputfilename:'" + outputFileName + "' was taken");
    }
}
