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

import org.kordamp.ezmorph.ObjectMorpher;

import java.lang.reflect.Array;

/**
 * Base class for array Morphers.
 *
 * @author Andres Almiray
 */
public abstract class AbstractArrayMorpher implements ObjectMorpher {
    private boolean useDefault = false;

    public AbstractArrayMorpher() {
    }

    /**
     * @param useDefault if morph() should return a default value if the value to
     *                   be morphed is null
     */
    public AbstractArrayMorpher(boolean useDefault) {
        this.useDefault = useDefault;
    }

    /**
     * Returns if this morpher will use a default value.
     */
    public boolean isUseDefault() {
        return useDefault;
    }

    /**
     * Sets if this morpher will use a default value.
     */
    public void setUseDefault(boolean useDefault) {
        this.useDefault = useDefault;
    }

    public boolean supports(Class<?> clazz) {
        return clazz.isArray();
    }

    /**
     * Creates an array representing the dimensions for conversion.
     */
    protected int[] createDimensions(int length, int initial) {
        Object dims = Array.newInstance(int.class, length);
        Array.set(dims, 0, new Integer(initial));
        return (int[]) dims;
    }

    /**
     * Returns the number of dimensions in an array class.
     */
    protected int getDimensions(Class arrayClass) {
        if (arrayClass == null || !arrayClass.isArray()) {
            return 0;
        }

        return 1 + getDimensions(arrayClass.getComponentType());
    }
}