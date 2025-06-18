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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.kordamp.ezmorph.MorphException;
import org.kordamp.ezmorph.primitive.LongMorpher;

import java.lang.reflect.Array;

/**
 * Morphs an array to a long[].
 *
 * @author Andres Almiray
 */
public final class LongArrayMorpher extends AbstractArrayMorpher {
    private static final Class LONG_ARRAY_CLASS = long[].class;
    private long defaultValue;

    public LongArrayMorpher() {
        super(false);
    }

    /**
     * @param defaultValue return value if the value to be morphed is null
     */
    public LongArrayMorpher(long defaultValue) {
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

        if (!(obj instanceof LongArrayMorpher)) {
            return false;
        }

        LongArrayMorpher other = (LongArrayMorpher) obj;
        EqualsBuilder builder = new EqualsBuilder();
        if (isUseDefault() && other.isUseDefault()) {
            builder.append(getDefaultValue(), other.getDefaultValue());
            return builder.isEquals();
        }
        return !isUseDefault() && !other.isUseDefault() && builder.isEquals();
    }

    /**
     * Returns the default value for this Morpher.
     */
    public long getDefaultValue() {
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

        if (LONG_ARRAY_CLASS.isAssignableFrom(array.getClass())) {
            // no conversion needed
            return (long[]) array;
        }

        if (array.getClass()
            .isArray()) {
            int length = Array.getLength(array);
            int dims = getDimensions(array.getClass());
            int[] dimensions = createDimensions(dims, length);
            Object result = Array.newInstance(long.class, dimensions);
            LongMorpher morpher = isUseDefault() ? new LongMorpher(defaultValue) : new LongMorpher();
            if (dims == 1) {
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
        return LONG_ARRAY_CLASS;
    }
}