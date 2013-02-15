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

import java.util.Scanner;
import javax.sql.DataSource;
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
        Scanner sqlScanner = new Scanner(scriptLocation.getInputStream(),"UTF-8").useDelimiter(";");
        while (sqlScanner.hasNext()) {
            String sql = sqlScanner.next();
            System.out.println(sql);
            new JdbcTemplate(dataSource).execute(sql);
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setScriptLocation(Resource scriptLocation) {
        this.scriptLocation = scriptLocation;
    }

}
