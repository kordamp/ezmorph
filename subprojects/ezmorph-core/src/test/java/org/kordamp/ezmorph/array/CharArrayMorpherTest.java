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
package org.kordamp.ezmorph.array;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.kordamp.ezmorph.MorphException;
import org.kordamp.ezmorph.test.ArrayAssertions;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class CharArrayMorpherTest extends AbstractArrayMorpherTestCase {
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(CharArrayMorpherTest.class);
        suite.setName("CharArrayMorpher Tests");
        return suite;
    }

    private CharArrayMorpher anotherMorpher;
    private CharArrayMorpher anotherMorpherWithDefaultValue;
    private CharArrayMorpher morpher;
    private CharArrayMorpher morpherWithDefaultValue;

    public CharArrayMorpherTest(String name) {
        super(name);
    }

    // -----------------------------------------------------------------------

    public void testMorph_charArray() {
        char[] expected = {'1', '2', '3'};
        char[] actual = (char[]) morpher.morph(expected);
        ArrayAssertions.assertEquals(expected, actual);
    }

    public void testMorph_charArray_threedims() {
        char[][][] expected = {{{'1'}, {'2'}}, {{'3'}, {'4'}}};
        char[][][] actual = (char[][][]) morpher.morph(expected);
        ArrayAssertions.assertEquals(expected, actual);
    }

    public void testMorph_charArray_twodims() {
        char[][] expected = {{'1', '2', '3'}, {'4', '5', '6'}};
        char[][] actual = (char[][]) morpher.morph(expected);
        ArrayAssertions.assertEquals(expected, actual);
    }

    public void testMorph_illegalArgument() {
        try {
            // argument is not an array
            morpher.morph("");
        } catch (MorphException expected) {
            // ok
        }
    }

    public void testMorph_null() {
        assertNull(morpher.morph(null));
    }

    public void testMorph_strings() {
        String[] expected = {"1", "2", "3.3"};
        char[] actual = (char[]) morpher.morph(expected);
        ArrayAssertions.assertEquals(new char[]{'1', '2', '3'}, actual);
    }

    public void testMorph_strings_twodims() {
        String[][] expected = {{"1", "2", "3.3"}, {"4", "5", "6.6"}};
        char[][] actual = (char[][]) morpher.morph(expected);
        ArrayAssertions.assertEquals(new char[][]{{'1', '2', '3'}, {'4', '5', '6'}}, actual);
    }

    public void testMorph_throwException() {
        try {
            new CharArrayMorpher().morph(new String[]{null});
            fail("Should have thrown an Exception");
        } catch (MorphException expected) {
            // ok
        }
    }

    protected AbstractArrayMorpher getAnotherMorpher() {
        return anotherMorpher;
    }

    protected AbstractArrayMorpher getAnotherMorpherWithDefaultValue() {
        return anotherMorpherWithDefaultValue;
    }

    protected AbstractArrayMorpher getMorpher() {
        return morpher;
    }

    protected AbstractArrayMorpher getMorpherWithDefaultValue() {
        return morpherWithDefaultValue;
    }

    protected Class getMorphsToClass() {
        return char[].class;
    }

    protected void setUp() throws Exception {
        morpher = new CharArrayMorpher();
        morpherWithDefaultValue = new CharArrayMorpher('A');
        anotherMorpher = new CharArrayMorpher();
        anotherMorpherWithDefaultValue = new CharArrayMorpher('B');
    }
}