/**
 * Copyright 2012 Michael R. Lange <michael.r.lange@langmi.de>.
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

/**
 * Simple Domain Class for Spring Batch Examples.
 * Has an own id and a shared id, which is meant as shared between many items,
 * which are somehow related.
 *
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class SimpleItem {

    private int id;
    private int sharedId;
    private String value;

    public SimpleItem() {
    }

    public SimpleItem(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public SimpleItem(int id, int sharedId, String value) {
        this.id = id;
        this.sharedId = sharedId;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSharedId() {
        return sharedId;
    }

    public void setSharedId(int sharedId) {
        this.sharedId = sharedId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SimpleItem{" + "id=" + id + ", sharedId=" + sharedId + ", value=" + value + '}';
    }
}
