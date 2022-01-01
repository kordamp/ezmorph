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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.kordamp.ezmorph.MorphException;
import org.kordamp.ezmorph.primitive.CharMorpher;

import java.lang.reflect.Array;

/**
 * Morphs an array to a Character[].
 *
 * @author Andres Almiray
 */
public final class CharacterObjectArrayMorpher extends AbstractArrayMorpher {
    private static final Class CHARACTER_OBJECT_ARRAY_CLASS = Character[].class;
    private Character defaultValue;

    public CharacterObjectArrayMorpher() {
        super(false);
    }

    /**
     * @param defaultValue return value if the value to be morphed is null
     */
    public CharacterObjectArrayMorpher(Character defaultValue) {
        super(true);
        this.defaultValue = defaultValue;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof CharacterObjectArrayMorpher)) {
            return false;
        }

        CharacterObjectArrayMorpher other = (CharacterObjectArrayMorpher) obj;
        EqualsBuilder builder = new EqualsBuilder();
        if (isUseDefault() && other.isUseDefault()) {
            builder.append(getDefaultValue(), other.getDefaultValue());
            return builder.isEquals();
        }
        return !isUseDefault() && !other.isUseDefault() && builder.isEquals();
    }

    public Character getDefaultValue() {
        return defaultValue;
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        if (isUseDefault()) {
            builder.append(getDefaultValue());
        }
        return builder.toHashCode();
    }

    public Object morph(Object array) {
        if (array == null) {
            return null;
        }

        if (CHARACTER_OBJECT_ARRAY_CLASS.isAssignableFrom(array.getClass())) {
            // no conversion needed
            return (Character[]) array;
        }

        if (array.getClass()
            .isArray()) {
            int length = Array.getLength(array);
            int dims = getDimensions(array.getClass());
            int[] dimensions = createDimensions(dims, length);
            Object result = Array.newInstance(Character.class, dimensions);
            if (dims == 1) {
                CharMorpher morpher = null;
                if (isUseDefault()) {
                    if (defaultValue == null) {
                        for (int index = 0; index < length; index++) {
                            Array.set(result, index, null);
                        }
                        return result;
                    } else {
                        morpher = new CharMorpher(defaultValue.charValue());
                    }
                } else {
                    morpher = new CharMorpher();
                }
                for (int index = 0; index < length; index++) {
                    Array.set(result, index, morpher.morph(Array.get(array, index)));
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
        return CHARACTER_OBJECT_ARRAY_CLASS;
    }
}