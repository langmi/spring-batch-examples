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
package de.langmi.spring.batch.examples.readers.tokenizers;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * Some tests to grok the behaviour of the {@link DelimitedLineTokenizer}.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class DelimitedLineTokenizerTests {
    
    private DelimitedLineTokenizer tokenizer;
    
    /**
     * Standard tokenizer test with quoted values.
     *
     * @throws Exception 
     */
    @Test
    public void testWithQuotes() throws Exception {
        // set names only, standard configuration
        tokenizer.setNames(new String[]{"id", "value"});
        // fire
        FieldSet fieldSet = tokenizer.tokenize("1,\"foo,bar\"");
        
        // assertions
        assertNotNull(fieldSet);
        assertTrue(fieldSet.getFieldCount() == 2);
        assertEquals("1", fieldSet.readString("id"));
        assertEquals("foo,bar", fieldSet.readString("value"));        
    }
    
    /**
     * Use a standard configured tokenizer.
     *
     * @throws Exception 
     */
    @Test
    public void testStandardConfiguration() throws Exception {
        // set names only, standard configuration
        tokenizer.setNames(new String[]{"id", "value"});
        // fire
        FieldSet fieldSet = tokenizer.tokenize("1,foo");
        
        // assertions
        assertNotNull(fieldSet);
        assertTrue(fieldSet.getFieldCount() == 2);
        assertEquals("1", fieldSet.readString("id"));
        assertEquals("foo", fieldSet.readString("value"));
    }
    
    
    @Before
    public void before() throws Exception {
        // fresh setup for each method
        tokenizer = new DelimitedLineTokenizer();
    }
}
