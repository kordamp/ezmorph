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
package org.kordamp.ezmorph.bean;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.kordamp.ezmorph.MorphException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class MorphDynaBeanTest extends TestCase {
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(MorphDynaBeanTest.class);
        suite.setName("MorphDynaBean Tests");
        return suite;
    }

    private MorphDynaBean dynaBean;
    private MorphDynaBean primitiveDynaBean;

    public MorphDynaBeanTest(String name) {
        super(name);
    }

    public void testConstructor_fail_Exception() {
        try {
            dynaBean = new MorphDynaBean();
            dynaBean.setDynaBeanClass(new MorphDynaClass("J", String.class, null));
            fail("should have thrown a MorphException");
        } catch (MorphException expected) {
            // ok
        }
    }

    public void testConstructor_fail_invalidPropertyClass() {
        try {
            Map properties = new HashMap();
            properties.put("object", new Object());
            dynaBean = new MorphDynaBean();
            dynaBean.setDynaBeanClass(new MorphDynaClass(properties));
            fail("should have thrown a MorphException");
        } catch (MorphException expected) {
            // ok
        }
    }

    public void testConstructor_fail_unkownClass() {
        try {
            Map properties = new HashMap();
            properties.put("object", "java.lang.Unknown");
            dynaBean = new MorphDynaBean();
            dynaBean.setDynaBeanClass(new MorphDynaClass(properties));
            fail("should have thrown a MorphException");
        } catch (MorphException expected) {
            // ok
        }
    }

    public void testContains() {
        assertFalse(dynaBean.contains("map", "key"));
        dynaBean.set("map", "key", "value");
        assertTrue(dynaBean.contains("map", "key"));
    }

    public void testContains_fail_Exception() {
        try {
            dynaBean.contains("byte", "key");
            fail("should have thrown a MorphException");
        } catch (MorphException expected) {
            // ok
        }
    }

    public void testEquals() {
        assertTrue(dynaBean.equals(dynaBean));
        assertFalse(dynaBean.equals(null));
        assertFalse(dynaBean.equals(primitiveDynaBean));
        assertFalse(dynaBean.equals(new Object()));
    }

    public void testGet_simple_nullValue() throws Exception {
        Map properties = new HashMap();
        properties.put("int", Integer.class);
        MorphDynaClass dynaClass = new MorphDynaClass(properties);
        dynaBean = (MorphDynaBean) dynaClass.newInstance();
        assertNull(dynaBean.get("int"));

        properties.clear();
        properties.put("int", Integer.TYPE);
        dynaClass = new MorphDynaClass(properties);
        dynaBean = (MorphDynaBean) dynaClass.newInstance();
        assertEquals(0, ((Integer) dynaBean.get("int")).intValue());
    }

    public void testGet_mapped_nullValue() throws Exception {
        Map properties = new HashMap();
        properties.put("map", Map.class);
        MorphDynaClass dynaClass = new MorphDynaClass(properties);
        dynaBean = (MorphDynaBean) dynaClass.newInstance();
        assertNull(dynaBean.get("map", "key"));
    }

    public void testGet_unindexed() {
        try {
            dynaBean.get("byte", 0);
            fail("should have thrown a MorphException");
        } catch (MorphException expected) {
            // ok
        }
    }

    public void testGet_unknownProperty() {
        try {
            dynaBean.get("unknown");
            fail("should have thrown a MorphException");
        } catch (MorphException expected) {
            // ok
        }
    }

    public void testGet_unmapped() {
        try {
            dynaBean.get("byte", "key");
            fail("should have thrown a MorphException");
        } catch (MorphException expected) {
            // ok
        }
    }

    public void testGetSet() {
        dynaBean.set("byte", Byte.valueOf("1"));
        dynaBean.set("short", Short.valueOf("1"));
        dynaBean.set("int", Integer.valueOf("1"));
        dynaBean.set("long", Long.valueOf("1"));
        dynaBean.set("float", Float.valueOf("1"));
        dynaBean.set("double", Double.valueOf("1"));
        dynaBean.set("bi", new BigInteger("1"));
        dynaBean.set("bd", new BigDecimal("1"));
        dynaBean.set("boolean", Boolean.TRUE);
        dynaBean.set("char", new Character('a'));

        assertEquals(Byte.valueOf("1"), dynaBean.get("byte"));
        assertEquals(Short.valueOf("1"), dynaBean.get("short"));
        assertEquals(Integer.valueOf("1"), dynaBean.get("int"));
        assertEquals(Long.valueOf("1"), dynaBean.get("long"));
        assertEquals(Float.valueOf("1"), dynaBean.get("float"));
        assertEquals(Double.valueOf("1"), dynaBean.get("double"));
        assertEquals(new BigInteger("1"), dynaBean.get("bi"));
        assertEquals(new BigDecimal("1"), dynaBean.get("bd"));
        assertEquals(Boolean.TRUE, dynaBean.get("boolean"));
        assertEquals(new Character('a'), dynaBean.get("char"));
    }

    public void testGetSet_primitives() {
        primitiveDynaBean.set("byte", Byte.valueOf("1"));
        primitiveDynaBean.set("short", Short.valueOf("1"));
        primitiveDynaBean.set("int", Integer.valueOf("1"));
        primitiveDynaBean.set("long", Long.valueOf("1"));
        primitiveDynaBean.set("float", Float.valueOf("1"));
        primitiveDynaBean.set("double", Double.valueOf("1"));
        primitiveDynaBean.set("boolean", Boolean.TRUE);
        primitiveDynaBean.set("char", new Character('a'));

        assertEquals(Byte.valueOf("1"), primitiveDynaBean.get("byte"));
        assertEquals(Short.valueOf("1"), primitiveDynaBean.get("short"));
        assertEquals(Integer.valueOf("1"), primitiveDynaBean.get("int"));
        assertEquals(Long.valueOf("1"), primitiveDynaBean.get("long"));
        assertEquals(Float.valueOf("1"), primitiveDynaBean.get("float"));
        assertEquals(Double.valueOf("1"), primitiveDynaBean.get("double"));
        assertEquals(Boolean.TRUE, primitiveDynaBean.get("boolean"));
        assertEquals(new Character('a'), primitiveDynaBean.get("char"));
    }

    public void testGetSetIndexed_Array() {
        dynaBean.set("strs", 0, "hello");
        dynaBean.set("strs", 1, "world");

        assertEquals("hello", dynaBean.get("strs", 0));
        assertEquals("world", dynaBean.get("strs", 1));
    }

    public void testGetSetIndexed_List() {
        dynaBean.set("list", 0, "hello");
        dynaBean.set("list", 1, "world");

        assertEquals("hello", dynaBean.get("list", 0));
        assertEquals("world", dynaBean.get("list", 1));
    }

    public void testGetSetMapped() {
        dynaBean.set("map", "key", "value");
        assertEquals("value", dynaBean.get("map", "key"));
    }

    public void testHashcode() {
        assertEquals(dynaBean.hashCode(), dynaBean.hashCode());
        assertTrue(dynaBean.hashCode() != primitiveDynaBean.hashCode());
    }

    public void testRemove() {
        dynaBean.set("map", "key", "value");
        assertTrue(dynaBean.contains("map", "key"));
        dynaBean.remove("map", "key");
        assertFalse(dynaBean.contains("map", "key"));
    }

    public void testRemove_fail_Exception() {
        try {
            dynaBean.remove("byte", "key");
            fail("should have thrown a MorphException");
        } catch (MorphException expected) {
            // ok
        }
    }

    public void testSet_unindexed() {
        try {
            dynaBean.set("byte", 0, null);
            fail("should have thrown a MorphException");
        } catch (MorphException expected) {
            // ok
        }
    }

    public void testSet_unmapped() {
        try {
            dynaBean.set("byte", "key", null);
            fail("should have thrown a MorphException");
        } catch (MorphException expected) {
            // ok
        }
    }

    protected void setUp() throws Exception {
        Map properties = new HashMap();
        properties.put("byte", Byte.class);
        properties.put("short", Short.class);
        properties.put("int", Integer.class);
        properties.put("long", Long.class);
        properties.put("float", Float.class);
        properties.put("double", Double.class);
        properties.put("bi", BigInteger.class);
        properties.put("bd", BigDecimal.class);
        properties.put("boolean", Boolean.class);
        properties.put("char", Character.class);
        properties.put("map", Map.class);
        properties.put("list", List.class);
        properties.put("strs", String[].class);
        MorphDynaClass dynaClass = new MorphDynaClass(properties);
        dynaBean = (MorphDynaBean) dynaClass.newInstance();

        properties = new HashMap();
        properties.put("byte", byte.class);
        properties.put("short", short.class);
        properties.put("int", int.class);
        properties.put("long", long.class);
        properties.put("float", float.class);
        properties.put("double", double.class);
        properties.put("boolean", boolean.class);
        properties.put("char", char.class);
        dynaClass = new MorphDynaClass(properties);
        primitiveDynaBean = (MorphDynaBean) dynaClass.newInstance();
    }
}