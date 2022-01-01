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
package org.kordamp.ezmorph.object;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.kordamp.ezmorph.MorphException;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class StringMorpherTest extends TestCase {
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(StringMorpherTest.class);
        suite.setName("StringMorpher Tests");
        return suite;
    }

    private StringMorpher morpher = StringMorpher.getInstance();

    public StringMorpherTest(String name) {
        super(name);
    }

    // -----------------------------------------------------------------------

    public void testMorph_array() {
        try {
            morpher.morph(new boolean[]{true, false});
            fail("Expected a MorphException");
        } catch (MorphException expected) {
            // ok
        }

    }

    public void testMorph_boolean() {
        String expected = "true";
        String actual = (String) morpher.morph(Boolean.TRUE);
        assertEquals(expected, actual);
    }

    public void testMorph_noConversion() {
        String expected = "true";
        String actual = (String) morpher.morph(expected);
        assertEquals(expected, actual);
    }

    public void testMorph_null() {
        assertNull(morpher.morph(null));
    }
}