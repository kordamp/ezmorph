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

import org.kordamp.ezmorph.MorphException;
import org.kordamp.ezmorph.ObjectMorpher;

/**
 * Morphs to a Class.<br>
 * This morpher is a singleton.
 *
 * @author Andres Almiray
 */
public final class ClassMorpher implements ObjectMorpher {
    private static final ClassMorpher INSTANCE = new ClassMorpher();

    /**
     * Returns the singleton instance
     */
    public static ClassMorpher getInstance() {
        return INSTANCE;
    }

    private ClassMorpher() {
    }

    public boolean equals(Object obj) {
        return INSTANCE == obj;
    }

    public int hashCode() {
        return 42 + getClass().hashCode();
    }

    public Object morph(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Class) {
            return (Class) value;
        }

        if ("null".equals(value)) {
            return null;
        }

        try {
            return Class.forName(value.toString());
        } catch (Exception e) {
            throw new MorphException(e);
        }
    }

    public Class<?> morphsTo() {
        return Class.class;
    }

    public boolean supports(Class<?> clazz) {
        return true;
    }
}