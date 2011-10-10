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
package de.langmi.spring.batch.examples.readers.file.tokenizers;

import java.util.Properties;
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
     * Standard tokenizer test with white space values.
     * Shows the differences for readString and readRawString, first one
     * trims whitespace.
     *
     * @throws Exception
     * @see http://forum.springsource.org/showthread.php?114765-Issue-FlatFileItemReader-automatically-Trimming-the-leading-and-trailing-spaces
     */
    @Test
    public void testWithSpaces() throws Exception {
        // set names only, standard configuration
        tokenizer.setNames(new String[]{"id", "value1", "value2"});
        // fire
        FieldSet fieldSet = tokenizer.tokenize("1,   foo   ,   bar");

        // assertions
        assertNotNull(fieldSet);
        assertTrue("field count", fieldSet.getFieldCount() == 3);
        assertEquals("1", fieldSet.readString("id"));
        // readString trims
        assertEquals("foo", fieldSet.readString("value1"));
        assertEquals("bar", fieldSet.readString("value2"));
        // readRawString does not trim
        assertEquals("   foo   ", fieldSet.readRawString("value1"));
        assertEquals("   bar", fieldSet.readRawString("value2"));

        // assertions with properties
        Properties props = fieldSet.getProperties();
        // the used fieldSet impl. trims whitespace in its getProperties method
        // see http://forum.springsource.org/showthread.php?114765-Issue-FlatFileItemReader-automatically-Trimming-the-leading-and-trailing-spaces
        // for follow up problems e.g. with BeanWrapperFieldSetMapper
        assertEquals("foo", props.getProperty("value1"));
        assertEquals("bar", props.getProperty("value2"));
    }

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
