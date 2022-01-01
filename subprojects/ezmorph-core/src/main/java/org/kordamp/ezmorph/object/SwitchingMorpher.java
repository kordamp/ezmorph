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

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.kordamp.ezmorph.MorphException;
import org.kordamp.ezmorph.MorpherRegistry;
import org.kordamp.ezmorph.ObjectMorpher;

import java.util.HashMap;
import java.util.Map;

/**
 * An all-purpose Morpher that can morph to several classes.<br>
 * Because this Morpher accepts any class and morphs to Object it should not be
 * added to a MorpherRegistry as it may be too generic for some cases and may
 * result in unwanted transformations.
 *
 * @author Andres Almiray
 */
public class SwitchingMorpher implements ObjectMorpher {
    private Map<Class<?>, Class<?>> classMap = new HashMap<Class<?>, Class<?>>();
    private MorpherRegistry morpherRegistry;

    public SwitchingMorpher(Map<Class<?>, Class<?>> classMap, MorpherRegistry morpherRegistry) {
        this.morpherRegistry = morpherRegistry;
        if (classMap == null || classMap.isEmpty()) {
            throw new MorphException("Must specify at least one mapping");
        }
        this.classMap.putAll(classMap);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof SwitchingMorpher)) {
            return false;
        }

        SwitchingMorpher other = (SwitchingMorpher) obj;
        if (classMap.size() != other.classMap.size()) {
            return false;
        }
        for (Map.Entry<Class<?>, Class<?>> entry : classMap.entrySet()) {
            if (!other.classMap.containsKey(entry.getKey())) {
                return false;
            }
            if (!entry.getValue()
                .equals(other.classMap.get(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        for (Map.Entry<Class<?>, Class<?>> entry : classMap.entrySet()) {
            builder.append(entry.getKey());
            builder.append(entry.getValue());
        }
        return builder.toHashCode();
    }

    public Object morph(Object value) {
        if (value == null) {
            return null;
        }

        Class target = (Class) classMap.get(value.getClass());
        return morpherRegistry.morph(target, value);
    }

    public Class<?> morphsTo() {
        return Object.class;
    }

    public boolean supports(Class<?> clazz) {
        return true;
    }
}