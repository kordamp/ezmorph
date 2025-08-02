/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2006-2025 Andres Almiray.
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
package org.kordamp.ezmorph.object;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.kordamp.ezmorph.MorphException;
import org.kordamp.ezmorph.Morpher;
import org.kordamp.ezmorph.test.ArrayAssertions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class ObjectListMorpherTest extends AbstractObjectMorpherTestCase {
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ObjectListMorpherTest.class);
        suite.setName("ObjectListMorpher Tests");
        return suite;
    }

    private ObjectListMorpher anotherMorpher;
    private ObjectListMorpher anotherMorpherWithDefaultValue;
    private ObjectListMorpher morpher;
    private ObjectListMorpher morpherWithDefaultValue;

    public ObjectListMorpherTest(String name) {
        super(name);
    }

    // -----------------------------------------------------------------------

    public void testMorph_illegalArgument() {
        try {
            // argument is not a list
            morpher.morph("");
        } catch (MorphException expected) {
            // ok
        }
    }

    public void testMorph_IntegerList() {
        List expected = new ArrayList();
        expected.add(Integer.valueOf(1));
        expected.add(Integer.valueOf(2));
        expected.add(Integer.valueOf(3));
        List actual = (List) morpher.morph(expected);
        ArrayAssertions.assertEquals(expected, actual);
    }

    public void testMorph_null() {
        assertNull(morpher.morph(null));
    }

    public void testMorph_NullList() {
        List expected = new ArrayList();
        expected.add(null);
        expected.add(null);
        expected.add(null);
        List actual = (List) morpher.morph(expected);
        ArrayAssertions.assertEquals(expected, actual);
    }

    public void testMorph_NullList_withDefaultValue() {
        List expected = new ArrayList();
        expected.add(Integer.valueOf(0));
        expected.add(Integer.valueOf(0));
        expected.add(Integer.valueOf(0));
        List input = new ArrayList();
        input.add(null);
        input.add(null);
        input.add(null);
        List actual = (List) morpherWithDefaultValue.morph(input);
        ArrayAssertions.assertEquals(expected, actual);
    }

    public void testMorph_StringList() {
        List expected = new ArrayList();
        expected.add(Integer.valueOf(1));
        expected.add(Integer.valueOf(2));
        expected.add(Integer.valueOf(3));
        List input = new ArrayList();
        input.add("1");
        input.add("2");
        input.add("3");
        List actual = (List) morpher.morph(input);
        ArrayAssertions.assertEquals(expected, actual);
    }

    public void testObjectListMorpher_illegalMorpher_noMorphMethod() {
        try {
            morpher = new ObjectListMorpher(new Morpher() {
                public Class morphsTo() {
                    return Object.class;
                }

                public boolean supports(Class clazz) {
                    return false;
                }
            });
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }

    public void testObjectListMorpher_illegalMorpher_nullMorpher() {
        try {
            morpher = new ObjectListMorpher(null);
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }

    public void testObjectListMorpher_illegalMorpher_supportsList() {
        try {
            morpher = new ObjectListMorpher(new Morpher() {
                public Class morphsTo() {
                    return List.class;
                }

                public boolean supports(Class clazz) {
                    return false;
                }
            });
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }

    protected Morpher getAnotherMorpher() {
        return anotherMorpher;
    }

    protected Morpher getAnotherMorpherWithDefaultValue() {
        return anotherMorpherWithDefaultValue;
    }

    protected Morpher getMorpher() {
        return morpher;
    }

    protected Morpher getMorpherWithDefaultValue() {
        return morpherWithDefaultValue;
    }

    protected void setUp() throws Exception {
        morpher = new ObjectListMorpher(new NumberMorpher(Integer.class));
        morpherWithDefaultValue = new ObjectListMorpher(new NumberMorpher(Integer.class,
            Integer.valueOf(0)), Integer.valueOf(0));
        anotherMorpher = new ObjectListMorpher(new NumberMorpher(Integer.class));
        anotherMorpherWithDefaultValue = new ObjectListMorpher(new NumberMorpher(Integer.class,
            Integer.valueOf(1)), Integer.valueOf(1));
    }
}