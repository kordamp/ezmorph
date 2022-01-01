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
package org.kordamp.ezmorph.object;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.kordamp.ezmorph.MorphException;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Morphs a Map into a Date.<br>
 * The Map should have at least one of the following keys
 * [yer,month,day,hour,minutes,seconds,milliseconds] and the values should be
 * instances of Number. Any key that is not defined will have zero (0) assigned
 * as its value.
 *
 * @author Andres Almiray
 */
public class MapToDateMorpher extends AbstractObjectMorpher {
    private Date defaultValue;

    public MapToDateMorpher() {
        super();
    }

    public MapToDateMorpher(Date defaultValue) {
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

        if (!(obj instanceof MapToDateMorpher)) {
            return false;
        }

        MapToDateMorpher other = (MapToDateMorpher) obj;
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
    public Date getDefaultValue() {
        return (Date) defaultValue.clone();
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        if (isUseDefault()) {
            builder.append(getDefaultValue());
        }
        return builder.toHashCode();
    }

    public Object morph(Object value) {
        if (value == null) {
            return null;
        }

        if (Date.class.isAssignableFrom(value.getClass())) {
            return (Date) value;
        }

        if (!supports(value.getClass())) {
            throw new MorphException(value.getClass() + " is not supported");
        }

        Map map = (Map) value;
        if (map.isEmpty()) {
            if (isUseDefault()) {
                return defaultValue;
            } else {
                throw new MorphException("Unable to parse the date " + value);
            }
        }

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, getValue(map, "year"));
        c.set(Calendar.MONTH, getValue(map, "month"));
        c.set(Calendar.DATE, getValue(map, "day"));
        c.set(Calendar.HOUR_OF_DAY, getValue(map, "hour"));
        c.set(Calendar.MINUTE, getValue(map, "minutes"));
        c.set(Calendar.SECOND, getValue(map, "seconds"));
        c.set(Calendar.MILLISECOND, getValue(map, "milliseconds"));
        return c.getTime();
    }

    public Class<?> morphsTo() {
        return Date.class;
    }

    /**
     * Sets the defaultValue to use if the value to be morphed is null.
     *
     * @param defaultValue return value if the value to be morphed is null
     */
    public void setDefaultValue(Date defaultValue) {
        this.defaultValue = (Date) defaultValue.clone();
    }

    public boolean supports(Class<?> clazz) {
        return clazz != null && Map.class.isAssignableFrom(clazz);
    }

    private int getValue(Map map, String key) {
        Object value = map.get(key);
        if (value == null || !(value instanceof Number)) {
            return 0;
        }

        Number n = (Number) value;
        return n.intValue();
    }
}