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

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.kordamp.ezmorph.MorphException;
import org.kordamp.ezmorph.Morpher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Morphs a List to another List using a Morpher.
 *
 * @author Andres Almiray
 */
public final class ObjectListMorpher extends AbstractObjectMorpher {
    private Object defaultValue;
    private Morpher morpher;
    private Method morphMethod;

    /**
     * Creates a new ArrayMorpher which will use another Morpher for its inner
     * type.<br>
     * The inner morpher can not morph to an array. Multiple dimension arrays are
     * already handled by this class.
     *
     * @param morpher the Morpher that will handle the array's inner type.
     */
    public ObjectListMorpher(Morpher morpher) {
        setMorpher(morpher);
    }

    public ObjectListMorpher(Morpher morpher, Object defaultValue) {
        super(true);
        this.defaultValue = defaultValue;
        setMorpher(morpher);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof ObjectListMorpher)) {
            return false;
        }

        ObjectListMorpher other = (ObjectListMorpher) obj;
        return morpher.equals(other.morpher);
    }

    public int hashCode() {
        return new HashCodeBuilder().append(morpher)
            .toHashCode();
    }

    public Object morph(Object value) {
        if (value == null) {
            return null;
        }

        if (!supports(value.getClass())) {
            throw new MorphException(value.getClass() + " is not supported");
        }

        List<Object> list = new ArrayList<Object>();
        for (Object object : ((List) value)) {
            if (object == null) {
                if (isUseDefault()) {
                    list.add(defaultValue);
                } else {
                    list.add(object);
                }
            } else {
                if (!morpher.supports(object.getClass())) {
                    throw new MorphException(object.getClass() + " is not supported");
                }
                try {
                    list.add(morphMethod.invoke(morpher, object));
                } catch (MorphException me) {
                    throw me;
                } catch (Exception e) {
                    throw new MorphException(e);
                }
            }
        }

        return list;
    }

    public Class<?> morphsTo() {
        return List.class;
    }

    public boolean supports(Class<?> clazz) {
        return clazz != null && List.class.isAssignableFrom(clazz);
    }

    private void setMorpher(Morpher morpher) {
        if (morpher == null) {
            throw new IllegalArgumentException("morpher can not be null");
        }
        this.morpher = morpher;

        // cache the morph method
        try {
            morphMethod = morpher.getClass()
                .getDeclaredMethod("morph", new Class[]{Object.class});
        } catch (NoSuchMethodException nsme) {
            throw new IllegalArgumentException(nsme.getMessage());
        }
    }
}