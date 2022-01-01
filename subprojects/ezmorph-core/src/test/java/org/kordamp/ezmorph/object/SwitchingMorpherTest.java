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
import org.kordamp.ezmorph.MorphUtils;
import org.kordamp.ezmorph.MorpherRegistry;
import org.kordamp.ezmorph.bean.BeanMorpher;
import org.kordamp.ezmorph.object.sample.BeanA;
import org.kordamp.ezmorph.object.sample.BeanB;
import org.kordamp.ezmorph.object.sample.WrapperA;
import org.kordamp.ezmorph.object.sample.WrapperB;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class SwitchingMorpherTest extends TestCase {
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SwitchingMorpherTest.class);
        suite.setName("SwitchingMorpher Tests");
        return suite;
    }

    private SwitchingMorpher morpher;

    public SwitchingMorpherTest(String name) {
        super(name);
    }

    // -----------------------------------------------------------------------

    public void testMorphWrapperAToBeanA() {
        WrapperA wrapper = new WrapperA();
        wrapper.setInteger("12");
        BeanA actual = (BeanA) morpher.morph(wrapper);
        BeanA expected = new BeanA();
        expected.setInteger(12);
        assertEquals(expected, actual);
    }

    public void testMorphWrapperBToBeanB() {
        WrapperB wrapper = new WrapperB();
        wrapper.setBool("false");
        BeanB actual = (BeanB) morpher.morph(wrapper);
        BeanB expected = new BeanB();
        expected.setBool(false);
        assertEquals(expected, actual);
    }

    public void testMorph_null() {
        assertNull(morpher.morph(null));
    }

    protected void setUp() throws Exception {
        Map<Class<?>, Class<?>> classMap = new HashMap<Class<?>, Class<?>>();
        classMap.put(WrapperA.class, BeanA.class);
        classMap.put(WrapperB.class, BeanB.class);
        MorpherRegistry morpherRegistry = new MorpherRegistry();
        MorphUtils.registerStandardMorphers(morpherRegistry);
        morpherRegistry.registerMorpher(new BeanMorpher(BeanA.class, morpherRegistry));
        morpherRegistry.registerMorpher(new BeanMorpher(BeanB.class, morpherRegistry));
        morpher = new SwitchingMorpher(classMap, morpherRegistry);
    }
}