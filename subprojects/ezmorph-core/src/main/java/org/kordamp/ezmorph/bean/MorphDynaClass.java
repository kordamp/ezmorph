/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2006-2024 Andres Almiray.
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

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.kordamp.ezmorph.MorphException;
import org.kordamp.ezmorph.MorphUtils;
import org.kordamp.ezmorph.MorpherRegistry;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andres Almiray
 */
public final class MorphDynaClass implements DynaClass, Serializable {
    private static final Comparator dynaPropertyComparator = new Comparator() {
        public int compare(Object a, Object b) {
            if (a instanceof DynaProperty && b instanceof DynaProperty) {
                DynaProperty p1 = (DynaProperty) a;
                DynaProperty p2 = (DynaProperty) b;
                return p1.getName()
                    .compareTo(p2.getName());
            }
            return -1;
        }
    };

    private static final long serialVersionUID = -613214016860871560L;

    private Map<String, Object> attributes;
    private Class beanClass;
    private DynaProperty dynaProperties[];
    private String name;
    private Map<String, DynaProperty> properties = new HashMap<String, DynaProperty>();
    private Class type;

    public MorphDynaClass(Map<String, Object> attributes) {
        this(null, null, attributes);
    }

    public MorphDynaClass(Map<String, Object> attributes, boolean exceptionOnEmptyAttributes) {
        this(null, null, attributes, exceptionOnEmptyAttributes);
    }

    public MorphDynaClass(String name, Class type, Map<String, Object> attributes) {
        this(name, type, attributes, false);
    }

    public MorphDynaClass(String name, Class type, Map<String, Object> attributes,
                          boolean exceptionOnEmptyAttributes) {
        if (name == null) {
            name = "MorphDynaClass";
        }
        if (type == null) {
            type = MorphDynaBean.class;
        }
        if (!MorphDynaBean.class.isAssignableFrom(type)) {
            throw new MorphException("MorphDynaBean is not assignable from " + type.getName());
        }
        if (attributes == null || attributes.isEmpty()) {
            if (exceptionOnEmptyAttributes) {
                throw new MorphException("Attributes map is null or empty.");
            } else {
                attributes = new HashMap<String, Object>();
            }
        }
        this.name = name;
        this.type = type;
        this.attributes = attributes;
        process();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof MorphDynaClass)) {
            return false;
        }

        MorphDynaClass other = (MorphDynaClass) obj;
        EqualsBuilder builder = new EqualsBuilder().append(this.name, other.name)
            .append(this.type, other.type);
        if (dynaProperties.length != other.dynaProperties.length) {
            return false;
        }
        for (int i = 0; i < dynaProperties.length; i++) {
            DynaProperty a = this.dynaProperties[i];
            DynaProperty b = other.dynaProperties[i];
            builder.append(a.getName(), b.getName());
            builder.append(a.getType(), b.getType());
        }
        return builder.isEquals();
    }

    public DynaProperty[] getDynaProperties() {
        return dynaProperties;
    }

    public DynaProperty getDynaProperty(String propertyName) {
        if (propertyName == null) {
            throw new MorphException("Unnespecified bean property name");

        }
        return properties.get(propertyName);
    }

    public String getName() {
        return this.name;
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder().append(name)
            .append(type);
        for (DynaProperty dynaProperty : dynaProperties) {
            builder.append(dynaProperty.getName());
            builder.append(dynaProperty.getType());
        }
        return builder.toHashCode();
    }

    public DynaBean newInstance() throws IllegalAccessException, InstantiationException {
        return newInstance(null);
    }

    public DynaBean newInstance(MorpherRegistry morpherRegistry) throws IllegalAccessException,
        InstantiationException {
        if (morpherRegistry == null) {
            morpherRegistry = new MorpherRegistry();
            MorphUtils.registerStandardMorphers(morpherRegistry);
        }
        MorphDynaBean dynaBean = (MorphDynaBean) getBeanClass().newInstance();
        dynaBean.setDynaBeanClass(this);
        dynaBean.setMorpherRegistry(morpherRegistry);
        for (String key : attributes.keySet()) {
            dynaBean.set(key, null);
        }
        return dynaBean;
    }

    public String toString() {
        return new ToStringBuilder(this).append("name", this.name)
            .append("type", this.type)
            .append("attributes", this.attributes)
            .toString();
    }

    protected Class getBeanClass() {
        if (this.beanClass == null) {
            process();
        }
        return this.beanClass;
    }

    private void process() {
        this.beanClass = this.type;

        try {

            dynaProperties = new DynaProperty[attributes.size()];
            int i = 0;
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                String pname = entry.getKey();
                Object pclass = entry.getValue();
                DynaProperty dynaProperty = null;
                if (pclass instanceof String) {
                    Class klass = (Class) Class.forName((String) pclass);
                    if (klass.isArray() && klass.getComponentType()
                        .isArray()) {
                        throw new MorphException("Multidimensional arrays are not supported");
                    }
                    dynaProperty = new DynaProperty(pname, klass);
                } else if (pclass instanceof Class) {
                    Class klass = (Class) pclass;
                    if (klass.isArray() && klass.getComponentType()
                        .isArray()) {
                        throw new MorphException("Multidimensional arrays are not supported");
                    }
                    dynaProperty = new DynaProperty(pname, klass);
                } else {
                    throw new MorphException("Type must be String or Class");
                }
                properties.put(dynaProperty.getName(), dynaProperty);
                dynaProperties[i++] = dynaProperty;
            }
        } catch (ClassNotFoundException cnfe) {
            throw new MorphException(cnfe);
        }

        // keep properties sorted by name
        Arrays.sort(dynaProperties, 0, dynaProperties.length, dynaPropertyComparator);
    }
}