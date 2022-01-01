/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2006-2022 Andres Almiray.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kordamp.ezmorph.array;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.kordamp.ezmorph.Morpher;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public abstract class AbstractArrayMorpherTestCase extends TestCase {
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(AbstractArrayMorpherTestCase.class);
        suite.setName("AbstractArrayMorpher Tests");
        return suite;
    }

    public AbstractArrayMorpherTestCase(String name) {
        super(name);
    }

    // -----------------------------------------------------------------------

    public void testEquals_another_Morpher() {
        assertFalse(getMorpherWithDefaultValue().equals(getAnotherMorpherWithDefaultValue()));
        assertTrue(getMorpher().equals(getAnotherMorpher()));
    }

    public void testEquals_different_morpher() {
        assertFalse(getMorpher().equals(new Morpher() {
            public Class morphsTo() {
                return null;
            }

            public boolean supports(Class clazz) {
                return false;
            }
        }));
    }

    public void testEquals_morpher_withDefaultValue() {
        assertFalse(getMorpher().equals(getMorpherWithDefaultValue()));
    }

    public void testEquals_null() {
        assertFalse(getMorpher().equals(null));
    }

    public void testEquals_same_morpher() {
        assertTrue(getMorpher().equals(getMorpher()));
        assertTrue(getMorpherWithDefaultValue().equals(getMorpherWithDefaultValue()));
    }

    public void testHashCode_morpher_withDefaultValue() {
        assertTrue(getMorpher().hashCode() != getMorpherWithDefaultValue().hashCode());
    }

    public void testHashCode_same_morpher() {
        assertEquals(getMorpher().hashCode(), getMorpher().hashCode());
        assertEquals(getMorpherWithDefaultValue().hashCode(),
            getMorpherWithDefaultValue().hashCode());
    }

    public void testMorphsTo() {
        assertEquals(getMorphsToClass(), getMorpher().morphsTo());
    }

    // -----------------------------------------------------------------------

    protected abstract AbstractArrayMorpher getAnotherMorpher();

    protected abstract AbstractArrayMorpher getAnotherMorpherWithDefaultValue();

    protected abstract AbstractArrayMorpher getMorpher();

    protected abstract AbstractArrayMorpher getMorpherWithDefaultValue();

    protected abstract Class getMorphsToClass();
}