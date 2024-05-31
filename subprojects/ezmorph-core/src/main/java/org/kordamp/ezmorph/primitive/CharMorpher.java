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
package org.kordamp.ezmorph.primitive;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.kordamp.ezmorph.MorphException;

/**
 * Morphs to a char.
 *
 * @author Andres Almiray
 */
public final class CharMorpher extends AbstractPrimitiveMorpher {
    private char defaultValue;

    public CharMorpher() {
        super();
    }

    /**
     * @param defaultValue return value if the value to be morphed is null
     */
    public CharMorpher(char defaultValue) {
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

        if (!(obj instanceof CharMorpher)) {
            return false;
        }

        CharMorpher other = (CharMorpher) obj;
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
    public char getDefaultValue() {
        return defaultValue;
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        if (isUseDefault()) {
            builder.append(getDefaultValue());
        }
        return builder.toHashCode();
    }

    /**
     * Morphs the input object into an output object of the supported type.
     *
     * @param value The input value to be morphed
     * @throws MorphException if conversion cannot be performed successfully
     */
    public char morph(Object value) {
        if (value == null) {
            if (isUseDefault()) {
                return defaultValue;
            } else {
                throw new MorphException("value is null");
            }
        }

        if (value instanceof Character) {
            return ((Character) value).charValue();
        } else {
            String s = String.valueOf(value);
            if (s.length() > 0) {
                return s.charAt(0);
            } else {
                if (isUseDefault()) {
                    return defaultValue;
                } else {
                    throw new MorphException("Can't morph value: " + value);
                }
            }
        }
    }

    public Class<?> morphsTo() {
        return Character.TYPE;
    }
}