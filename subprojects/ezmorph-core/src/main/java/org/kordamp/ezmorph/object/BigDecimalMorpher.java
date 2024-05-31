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
package org.kordamp.ezmorph.object;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.kordamp.ezmorph.MorphException;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Morphs to a BigDecimal.
 *
 * @author Andres Almiray
 */
public final class BigDecimalMorpher extends AbstractObjectMorpher {
    private BigDecimal defaultValue;

    public BigDecimalMorpher() {
        super();
    }

    /**
     * @param defaultValue return value if the value to be morphed is null
     */
    public BigDecimalMorpher(BigDecimal defaultValue) {
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

        if (!(obj instanceof BigDecimalMorpher)) {
            return false;
        }

        BigDecimalMorpher other = (BigDecimalMorpher) obj;
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
    public BigDecimal getDefaultValue() {
        return defaultValue;
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        if (isUseDefault()) {
            builder.append(getDefaultValue());
        }
        return builder.toHashCode();
    }

    public Object morph(Object value) {
        if (value instanceof BigDecimal) {
            return value;
        }

        if (value == null) {
            if (isUseDefault()) {
                return defaultValue;
            } else {
                return (BigDecimal) null;
            }
        }

        if (value instanceof Number) {
            if (value instanceof Float) {
                Float f = ((Float) value);
                if (f.isInfinite() || f.isNaN()) {
                    throw new MorphException("BigDecimal can not be infinite or NaN");
                }
            } else if (value instanceof Double) {
                Double d = ((Double) value);
                if (d.isInfinite() || d.isNaN()) {
                    throw new MorphException("BigDecimal can not be infinite or NaN");
                }
            } else if (value instanceof BigInteger) {
                return new BigDecimal((BigInteger) value);
            }

            return new BigDecimal(((Number) value).doubleValue());
        } else {
            try {
                String str = String.valueOf(value)
                    .trim();
                if (str.length() == 0 || str.equalsIgnoreCase("null")) {
                    return (BigDecimal) null;
                } else {
                    return new BigDecimal(str);
                }
            } catch (NumberFormatException nfe) {
                if (isUseDefault()) {
                    return defaultValue;
                } else {
                    throw new MorphException(nfe);
                }
            }
        }
    }

    public Class<?> morphsTo() {
        return BigDecimal.class;
    }
}