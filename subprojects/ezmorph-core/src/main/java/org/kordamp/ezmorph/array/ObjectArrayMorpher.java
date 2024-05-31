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
package org.kordamp.ezmorph.array;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.kordamp.ezmorph.MorphException;
import org.kordamp.ezmorph.Morpher;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * Morphs an array to another array using a Morpher.
 *
 * @author Andres Almiray
 */
public final class ObjectArrayMorpher extends AbstractArrayMorpher {
    private Morpher morpher;
    private Method morphMethod;
    private Class target;
    private Class targetArrayClass;

    /**
     * Creates a new ArrayMorpher which will use another Morpher for its inner
     * type.<br>
     * The inner morpher can not morph to an array. Multiple dimension arrays are
     * already handled by this class.
     *
     * @param morpher the Morpher that will handle the array's inner type.
     */
    public ObjectArrayMorpher(Morpher morpher) {
        super(false);
        setMorpher(morpher);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof ObjectArrayMorpher)) {
            return false;
        }

        ObjectArrayMorpher other = (ObjectArrayMorpher) obj;
        return morpher.equals(other.morpher);
    }

    public int hashCode() {
        return new HashCodeBuilder().append(morpher)
            .toHashCode();
    }

    public Object morph(Object array) {
        if (array == null) {
            return null;
        }

        if (array.getClass()
            .isArray()) {
            int length = Array.getLength(array);
            int dims = getDimensions(array.getClass());
            int[] dimensions = createDimensions(dims, length);
            Object result = Array.newInstance(this.target, dimensions);

            if (dims == 1) {
                for (int index = 0; index < length; index++) {
                    try {
                        Object value = Array.get(array, index);
                        if (value != null && !morpher.supports(value.getClass())) {
                            throw new MorphException(value.getClass() + " is not supported");
                        }
                        Object morphed = morphMethod.invoke(morpher, value);
                        Array.set(result, index, morphed);
                    } catch (MorphException me) {
                        throw me;
                    } catch (Exception e) {
                        throw new MorphException(e);
                    }
                }
            } else {
                for (int index = 0; index < length; index++) {
                    Array.set(result, index, morph(Array.get(array, index)));
                }
            }

            return result;
        } else {
            throw new MorphException("argument is not an array: " + array.getClass());
        }
    }

    public Class<?> morphsTo() {
        return targetArrayClass;
    }

    public boolean supports(Class<?> clazz) {
        if (clazz == null || !clazz.isArray()) {
            return false;
        }
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        return morpher.supports(clazz);
    }

    private void setMorpher(Morpher morpher) {
        if (morpher == null) {
            throw new IllegalArgumentException("morpher can not be null");
        }
        if (morpher.morphsTo()
            .isArray()) {
            throw new IllegalArgumentException("morpher target class can not be an array");
        }
        this.morpher = morpher;
        this.targetArrayClass = Array.newInstance(morpher.morphsTo(), 1)
            .getClass();
        this.target = morpher.morphsTo();

        // cache the morph method
        try {
            morphMethod = morpher.getClass()
                .getDeclaredMethod("morph", new Class[]{Object.class});
        } catch (NoSuchMethodException nsme) {
            throw new IllegalArgumentException(nsme.getMessage());
        }
    }
}