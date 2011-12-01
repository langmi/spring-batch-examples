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
package de.langmi.spring.batch.examples.complex.jdbc.generic.support;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

/**
 * MapPreparedStatementSetter.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class MapPreparedStatementSetter implements ItemPreparedStatementSetter<Map<String, Object>> {

    @Override
    public void setValues(Map<String, Object> item, PreparedStatement ps) throws SQLException {
        for (int i = 0; i < item.size(); i++) {
            // PreparedStatements start with 1
            ps.setObject(i + 1, item.get(String.valueOf(i)));
        }
    }
}
