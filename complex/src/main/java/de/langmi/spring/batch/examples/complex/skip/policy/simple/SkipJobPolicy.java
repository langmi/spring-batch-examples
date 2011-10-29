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
package de.langmi.spring.batch.examples.complex.skip.policy.simple;

import de.langmi.spring.batch.examples.complex.skip.simple.SkipJobCustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

/**
 * SkipJobPolicy.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class SkipJobPolicy implements SkipPolicy {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private int skipLimit;

    @Override
    public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
        LOG.debug("shouldSkip:" + t.toString());
        if (t instanceof SkipJobCustomException && skipCount <= skipLimit) {
            return true;
        } else {
            return false;
        }
    }

    public void setSkipLimit(int skipLimit) {
        this.skipLimit = skipLimit;
    }
}
