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
package de.langmi.spring.batch.examples.renamefiles.partition;

import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * A 99% Copy of the the Spring MultiResourcePartitioner, file name propagation
 * is outsourced to a callback handler.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class CustomMultiResourcePartitioner implements Partitioner {

    private static final String PARTITION_KEY = "partition";
    private Resource[] resources = new Resource[0];
    private MultiResourceCallbackHandler callbackHandler = new DefaultMultiResourceCallbackHandler();

    /**
     * The resources to assign to each partition. In Spring configuration you
     * can use a pattern to select multiple resources.
     * @param resources the resources to use
     */
    public void setResources(Resource[] resources) {
        this.resources = resources;
    }

    public void setCallbackHandler(MultiResourceCallbackHandler callbackHandler) {
        this.callbackHandler = callbackHandler;
    }

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
            // callback
            callbackHandler.handleContextParametersSetup(i, context, resource);
            map.put(PARTITION_KEY + i, context);
            i++;
        }
        return map;
    }
}
