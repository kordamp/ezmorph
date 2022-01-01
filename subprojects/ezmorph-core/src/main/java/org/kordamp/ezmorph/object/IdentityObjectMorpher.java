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

import org.kordamp.ezmorph.ObjectMorpher;

/**
 * Morpher that performs no conversion.<br>
 * This morpher is a singleton.
 *
 * @author Andres Almiray
 */
public final class IdentityObjectMorpher implements ObjectMorpher {
    private static final IdentityObjectMorpher INSTANCE = new IdentityObjectMorpher();

    /**
     * Returns the singleton instance
     */
    public static IdentityObjectMorpher getInstance() {
        return INSTANCE;
    }

    private IdentityObjectMorpher() {
    }

    public boolean equals(Object obj) {
        return INSTANCE == obj;
    }

    public int hashCode() {
        return 42 + getClass().hashCode();
    }

    public Object morph(Object value) {
        return value;
    }

    public Class<?> morphsTo() {
        return Object.class;
    }

    public boolean supports(Class<?> clazz) {
        return true;
    }
}