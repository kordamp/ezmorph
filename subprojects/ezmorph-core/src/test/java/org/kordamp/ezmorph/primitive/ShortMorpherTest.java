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
package org.kordamp.ezmorph.primitive;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.kordamp.ezmorph.MorphException;
import org.kordamp.ezmorph.Morpher;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class ShortMorpherTest extends AbstractMorpherTestCase {
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ShortMorpherTest.class);
        suite.setName("ShortMorpher Tests");
        return suite;
    }

    private Morpher anotherMorpher;
    private Morpher anotherMorpherWithDefaultValue;
    private Morpher morpher;
    private Morpher morpherWithDefaultValue;

    public ShortMorpherTest(String name) {
        super(name);
    }

    // -----------------------------------------------------------------------

    public void testShortMorph_throwException() {
        try {
            ((ShortMorpher) getMorpher()).morph(String.valueOf("A"));
            fail("Should have thrown an Exception");
        } catch (MorphException expected) {
            // ok
        }
    }

    public void testShortMorph_throwException_null() {
        try {
            ((ShortMorpher) getMorpher()).morph(null);
            fail("Should have thrown an Exception");
        } catch (MorphException expected) {
            // ok
        }
    }

    public void testShortMorph_useDefault() {
        String expected = String.valueOf("A");
        short actual = ((ShortMorpher) getMorpherWithDefaultValue()).morph(expected);
        assertEquals(0, actual);
    }

    public void testShortMorph_useDefault_null() {
        short actual = ((ShortMorpher) getMorpherWithDefaultValue()).morph(null);
        assertEquals(0, actual);
    }

    public void testShortMorphDecimalValue_Number() {
        Double expected = Double.valueOf(3.1416d);
        short actual = ((ShortMorpher) getMorpher()).morph(expected);
        assertEquals(3, actual);
    }

    public void testShortMorphDecimalValue_Number_outOfRange() {
        short actual = ((ShortMorpher) getMorpher()).morph(Double.valueOf(Double.MAX_VALUE));
        assertEquals(-1, actual);
    }

    public void testShortMorphDecimalValue_String() {
        String expected = "3.1416";
        short actual = ((ShortMorpher) getMorpher()).morph(expected);
        assertEquals(3, actual);
    }

    public void testShortMorphMaxValue_Number() {
        Short expected = Short.valueOf(Short.MAX_VALUE);
        short actual = ((ShortMorpher) getMorpher()).morph(expected);
        assertEquals(expected.shortValue(), actual);
    }

    public void testShortMorphMaxValue_String() {
        String expected = String.valueOf(Short.valueOf(Short.MAX_VALUE));
        short actual = ((ShortMorpher) getMorpher()).morph(expected);
        assertEquals(expected, String.valueOf(actual));
    }

    public void testShortMorphMinValue_Number() {
        Short expected = Short.valueOf(Short.MIN_VALUE);
        short actual = ((ShortMorpher) getMorpher()).morph(expected);
        assertEquals(expected.shortValue(), actual);
    }

    public void testShortMorphMinValue_String() {
        String expected = String.valueOf(Short.valueOf(Short.MIN_VALUE));
        short actual = ((ShortMorpher) getMorpher()).morph(expected);
        assertEquals(expected, String.valueOf(actual));
    }

    protected Morpher getMorpher() {
        return morpher;
    }

    protected Morpher getMorpherWithDefaultValue() {
        return morpherWithDefaultValue;
    }

    protected Class getMorphsToClass() {
        return Short.TYPE;
    }

    protected Morpher getAnotherMorpher() {
        return anotherMorpher;
    }

    protected Morpher getAnotherMorpherWithDefaultValue() {
        return anotherMorpherWithDefaultValue;
    }

    protected void setUp() throws Exception {
        morpher = new ShortMorpher();
        morpherWithDefaultValue = new ShortMorpher((short) 0);
        anotherMorpher = new ShortMorpher();
        anotherMorpherWithDefaultValue = new ShortMorpher((short) 1);
    }
}