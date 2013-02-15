/*
 * Copyright 2013 Michael R. Lange <michael.r.lange@langmi.de>.
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
package de.langmi.spring.batch.examples.complex.support;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class InitializeDatabase implements InitializingBean {

    private DataSource dataSource;
    private Resource scriptLocation;

    @Override
    public void afterPropertiesSet() throws Exception {
        // i tried java.util.Scanner, but it does not work correctly with all line feed versions
        String sql = stripComments(IOUtils.readLines(scriptLocation.getInputStream()));
        new JdbcTemplate(dataSource).execute(sql);
    }

    /**
     * Strip Strings in List from SQL comments.
     */
    private String stripComments(List<String> list) {
        StringBuilder buffer = new StringBuilder();
        for (String line : list) {
            if (!line.startsWith("//") && !line.startsWith("--")) {
                buffer.append(line).append("\n");
            }
        }
        return buffer.toString();
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setScriptLocation(Resource scriptLocation) {
        this.scriptLocation = scriptLocation;
    }
}
