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
package de.langmi.spring.batch.examples.complex.aggregating;

/**
 * AggregatedItem.
 * 
 * @author Michael R. Lange <michael.r.lange@langmi.de>
 */
public class AggregatedItem {

    private int id;
    private int sum = 0;

    public AggregatedItem(int id) {
        this.id = id;
    }

    
    public AggregatedItem(int id, int sum) {
        this.id = id;
        this.sum = sum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public void add(int value) {
        this.sum = this.sum + value;
    }

    @Override
    public String toString() {
        return "AggregatedItem{" + "id=" + id + ", sum=" + sum + '}';
    }
}
