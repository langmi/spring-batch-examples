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
package de.langmi.spring.batch.examples.readers.file.multiresourcepartitioner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Slightly changed MultiResourcePartitioner, does create output file name too.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class CustomMultiResourcePartitioner implements Partitioner {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private static final String PARTITION_KEY = "partition";
    private Resource[] resources = new Resource[0];
    private String inputKeyName = "inputFilePath";
    private String outputKeyName = "outputFileName";

    /**
     * Assign the filename of each of the injected resources to an
     * {@link ExecutionContext}.
     * 
     * @see Partitioner#partition(int)
     */
    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> map = new HashMap<String, ExecutionContext>(gridSize);
        int i = 0;
        for (Resource resource : resources) {
            ExecutionContext context = new ExecutionContext();
            Assert.state(resource.exists(), "Resource does not exist: " + resource);
            try {
                context.putString(inputKeyName, resource.getURL().toExternalForm());
                context.put(outputKeyName, createOutputFilename(i, resource));
            } catch (IOException e) {
                throw new IllegalArgumentException("File could not be located for: " + resource, e);
            }
            map.put(PARTITION_KEY + i, context);
            i++;
        }
        return map;
    }

    /**
     * Creates distinct output file name per partition.
     *
     * @param partitionId
     * @param context
     * @param resource
     * @return 
     */
    private String createOutputFilename(int partitionId, Resource resource) {
        String outputFileName = "output-" + String.valueOf(partitionId) + ".txt";
        LOG.info(
                "for inputfile:'"
                + resource.getFilename()
                + "' outputfilename:'"
                + outputFileName
                + "' was created");

        return outputFileName;
    }

    /**
     * The resources to assign to each partition. In Spring configuration you
     * can use a pattern to select multiple resources.
     *
     * @param resources the resources to use
     */
    public void setResources(Resource[] resources) {
        this.resources = resources;
    }
}
