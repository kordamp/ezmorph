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
package org.kordamp.ezmorph.primitive;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.kordamp.ezmorph.MorphException;

/**
 * Morphs to a boolean.
 *
 * @author Andres Almiray
 */
public final class BooleanMorpher extends AbstractPrimitiveMorpher {
    private boolean defaultValue;

    public BooleanMorpher() {
        super();
    }

    /**
     * @param defaultValue return value if the value to be morphed is null
     */
    public BooleanMorpher(boolean defaultValue) {
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

        if (!(obj instanceof BooleanMorpher)) {
            return false;
        }

        BooleanMorpher other = (BooleanMorpher) obj;
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
    public boolean getDefaultValue() {
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
    public boolean morph(Object value) {
        if (value == null) {
            if (isUseDefault()) {
                return defaultValue;
            } else {
                throw new MorphException("value is null");
            }
        }

        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        } else if (value instanceof Number) {
            if (value instanceof Double
                && (Double.isInfinite(((Number) value).doubleValue()) || Double.isNaN(((Number) value).doubleValue()))) {
                return true;
            }
            if (value instanceof Float
                && (Float.isInfinite(((Number) value).floatValue()) || Float.isNaN(((Number) value).floatValue()))) {
                return true;
            }
            long l = ((Number) value).longValue();
            return l != 0;
        } else {
            String s = String.valueOf(value);

            if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("yes")
                || s.equalsIgnoreCase("on")) {
                return true;
            } else if (s.equalsIgnoreCase("false") || s.equalsIgnoreCase("no")
                || s.equalsIgnoreCase("off")) {
                return false;
            } else if (isUseDefault()) {
                return defaultValue;
            }
        }

        throw new MorphException("Can't morph value: " + value);
    }

    public Class<?> morphsTo() {
        return Boolean.TYPE;
    }
}