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
import org.kordamp.ezmorph.primitive.ByteMorpher;
import org.kordamp.ezmorph.primitive.DoubleMorpher;
import org.kordamp.ezmorph.primitive.FloatMorpher;
import org.kordamp.ezmorph.primitive.IntMorpher;
import org.kordamp.ezmorph.primitive.LongMorpher;
import org.kordamp.ezmorph.primitive.ShortMorpher;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Morphs to a subclass of Number.<br>
 * Supported types are - Byte, Short, Integer, Long, Float, BigInteger,
 * BigtDecimal.
 *
 * @author Andres Almiray
 */
public final class NumberMorpher extends AbstractObjectMorpher {
    private Number defaultValue;
    private Class<?> type;

    /**
     * Creates a new morpher for the target type.
     *
     * @param type must be a primitive or wrapper type. BigDecimal and BigInteger
     *             are also supported.
     */
    public NumberMorpher(Class<?> type) {
        super(false);

        if (type == null) {
            throw new MorphException("Must specify a type");
        }

        if (type != Byte.TYPE && type != Short.TYPE && type != Integer.TYPE && type != Long.TYPE
            && type != Float.TYPE && type != Double.TYPE && !Byte.class.isAssignableFrom(type)
            && !Short.class.isAssignableFrom(type) && !Integer.class.isAssignableFrom(type)
            && !Long.class.isAssignableFrom(type) && !Float.class.isAssignableFrom(type)
            && !Double.class.isAssignableFrom(type) && !BigInteger.class.isAssignableFrom(type)
            && !BigDecimal.class.isAssignableFrom(type)) {
            throw new MorphException("Must specify a Number subclass");
        }

        this.type = type;
    }

    /**
     * Creates a new morpher for the target type with a default value.<br>
     * The defaultValue should be of the same class as the target type.
     *
     * @param type         must be a primitive or wrapper type. BigDecimal and BigInteger
     *                     are also supported.
     * @param defaultValue return value if the value to be morphed is null
     */
    public NumberMorpher(Class type, Number defaultValue) {
        super(true);

        if (type == null) {
            throw new MorphException("Must specify a type");
        }

        if (type != Byte.TYPE && type != Short.TYPE && type != Integer.TYPE && type != Long.TYPE
            && type != Float.TYPE && type != Double.TYPE && !Byte.class.isAssignableFrom(type)
            && !Short.class.isAssignableFrom(type) && !Integer.class.isAssignableFrom(type)
            && !Long.class.isAssignableFrom(type) && !Float.class.isAssignableFrom(type)
            && !Double.class.isAssignableFrom(type) && !BigInteger.class.isAssignableFrom(type)
            && !BigDecimal.class.isAssignableFrom(type)) {
            throw new MorphException("Must specify a Number subclass");
        }

        if (defaultValue != null && !type.isInstance(defaultValue)) {
            throw new MorphException("Default value must be of type " + type);
        }

        this.type = type;
        setDefaultValue(defaultValue);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof NumberMorpher)) {
            return false;
        }

        NumberMorpher other = (NumberMorpher) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(type, other.type);
        if (isUseDefault() && other.isUseDefault()) {
            builder.append(getDefaultValue(), other.getDefaultValue());
            return builder.isEquals();
        }
        return !isUseDefault() && !other.isUseDefault() && builder.isEquals();
    }

    /**
     * Returns the default value for this Morpher.
     */
    public Number getDefaultValue() {
        return defaultValue;
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(type);
        if (isUseDefault()) {
            builder.append(getDefaultValue());
        }
        return builder.toHashCode();
    }

    public Object morph(Object value) {
        if (value != null && type.isAssignableFrom(value.getClass())) {
            // no conversion needed
            return value;
        }

        String str = String.valueOf(value)
            .trim();

        if (!type.isPrimitive()
            && (value == null || str.length() == 0 || "null".equalsIgnoreCase(str))) {
            // if empty string and class != primitive treat it like null
            return null;
        }

        if (isDecimalNumber(type)) {
            if (Float.class.isAssignableFrom(type) || Float.TYPE == type) {
                return morphToFloat(str);
            } else if (Double.class.isAssignableFrom(type) || Double.TYPE == type) {
                return morphToDouble(str);
            } else {
                return morphToBigDecimal(str);
            }
        } else {
            if (Byte.class.isAssignableFrom(type) || Byte.TYPE == type) {
                return morphToByte(str);
            } else if (Short.class.isAssignableFrom(type) || Short.TYPE == type) {
                return morphToShort(str);
            } else if (Integer.class.isAssignableFrom(type) || Integer.TYPE == type) {
                return morphToInteger(str);
            } else if (Long.class.isAssignableFrom(type) || Long.TYPE == type) {
                return morphToLong(str);
            } else {
                return morphToBigInteger(str);
            }
        }
    }

    public Class<?> morphsTo() {
        return type;
    }

    /**
     * Sets the defaultValue to use if the value to be morphed is null.<br>
     * The defaultValue should be of the same class as the type this morpher
     * returns with <code>morphsTo()</code>.
     *
     * @param defaultValue return value if the value to be morphed is null
     */
    public void setDefaultValue(Number defaultValue) {
        if (defaultValue != null && !type.isInstance(defaultValue)) {
            throw new MorphException("Default value must be of type " + type);
        }
        this.defaultValue = defaultValue;
    }

    private boolean isDecimalNumber(Class type) {
        return (Double.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type)
            || Double.TYPE == type || Float.TYPE == type || BigDecimal.class.isAssignableFrom(type));
    }

    private Object morphToBigDecimal(String str) {
        Object result = null;
        if (isUseDefault()) {
            result = new BigDecimalMorpher((BigDecimal) defaultValue).morph(str);
        } else {
            result = new BigDecimal(str);
        }
        return result;
    }

    private Object morphToBigInteger(String str) {
        Object result = null;
        if (isUseDefault()) {
            result = new BigIntegerMorpher((BigInteger) defaultValue).morph(str);
        } else {
            result = new BigInteger(str);
        }
        return result;
    }

    private Object morphToByte(String str) {
        Object result = null;
        if (isUseDefault()) {
            if (defaultValue == null) {
                return (Byte) null;
            } else {
                result = new ByteMorpher(defaultValue.byteValue()).morph(str);
            }
        } else {
            result = new ByteMorpher().morph(str);
        }
        return result;
    }

    private Object morphToDouble(String str) {
        Object result = null;
        if (isUseDefault()) {
            if (defaultValue == null) {
                return (Double) null;
            } else {
                result = new DoubleMorpher(defaultValue.doubleValue()).morph(str);
            }
        } else {
            result = new DoubleMorpher().morph(str);
        }
        return result;
    }

    private Object morphToFloat(String str) {
        Object result = null;
        if (isUseDefault()) {
            if (defaultValue == null) {
                return (Float) null;
            } else {
                result = new FloatMorpher(defaultValue.floatValue()).morph(str);
            }
        } else {
            result = new FloatMorpher().morph(str);
        }
        return result;
    }

    private Object morphToInteger(String str) {
        Object result = null;
        if (isUseDefault()) {
            if (defaultValue == null) {
                return (Integer) null;
            } else {
                result = new IntMorpher(defaultValue.intValue()).morph(str);
            }
        } else {
            result = new IntMorpher().morph(str);
        }
        return result;
    }

    private Object morphToLong(String str) {
        Object result = null;
        if (isUseDefault()) {
            if (defaultValue == null) {
                return (Long) null;
            } else {
                result = new LongMorpher(defaultValue.longValue()).morph(str);
            }
        } else {
            result = new LongMorpher().morph(str);
        }
        return result;
    }

    private Object morphToShort(String str) {
        Object result = null;
        if (isUseDefault()) {
            if (defaultValue == null) {
                return (Short) null;
            } else {
                result = new ShortMorpher(defaultValue.shortValue()).morph(str);
            }
        } else {
            result = new ShortMorpher().morph(str);
        }
        return result;
    }
}