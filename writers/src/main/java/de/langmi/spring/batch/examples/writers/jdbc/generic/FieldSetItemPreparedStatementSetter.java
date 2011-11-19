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
package de.langmi.spring.batch.examples.writers.jdbc.generic;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * Implementation for {@link ItemPreparedStatementSetter}, 
 * sets the values from {@link FieldSet}.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class FieldSetItemPreparedStatementSetter implements ItemPreparedStatementSetter<FieldSet> {

    /** {@inheritDoc} */
    @Override
    public void setValues(FieldSet item, PreparedStatement ps) throws SQLException {
        for (int i = 0; i < item.getValues().length; i++) {
            // PreparedStatements start with 1
            ps.setObject(i+1, item.getValues()[i]);

        }
    }
}
