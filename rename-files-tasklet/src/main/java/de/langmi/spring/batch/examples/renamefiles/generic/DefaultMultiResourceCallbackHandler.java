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

import java.io.IOException;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.Resource;

/**
 * Default callback implementation.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class DefaultMultiResourceCallbackHandler implements MultiResourceCallbackHandler {

    private static final String DEFAULT_KEY_NAME = "fileName";
    private String keyName = DEFAULT_KEY_NAME;
    
    @Override
    public void handleContextParametersSetup(int partition, ExecutionContext context, Resource resource) {
        try {
            context.putString(keyName, resource.getURL().toExternalForm());
        } catch (IOException e) {
            throw new IllegalArgumentException("File could not be located for: " + resource, e);
        }
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
}
