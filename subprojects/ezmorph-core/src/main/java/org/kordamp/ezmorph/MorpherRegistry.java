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
package org.kordamp.ezmorph;

import org.kordamp.ezmorph.object.IdentityObjectMorpher;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Convenient class that manages Morphers.<br>
 * A MorpherRehistry manages a group of Morphers. A Morpher will always be
 * associated with a target class, it is possible to have several Morphers
 * registered for a target class, if this is the case, the first Morpher will be
 * used when performing a conversion and no specific Morpher is selected in
 * advance.<br>
 * {@link MorphUtils} may be used to register standard Morphers for primitive
 * types and primitive wrappers, as well as arrays of those types.
 *
 * @author Andres Almiray
 */
public class MorpherRegistry implements Serializable {
    private static final long serialVersionUID = -3894767123320768419L;
    private Map<Class<?>, List<Morpher>> morphers = new HashMap<Class<?>, List<Morpher>>();

    public MorpherRegistry() {

    }

    /**
     * Deregisters all morphers.
     */
    public synchronized void clear() {
        morphers.clear();
    }

    /**
     * Deregister all Morphers of a type.<br>
     *
     * @param type the target type the Morphers morph to
     */
    public synchronized void clear(Class<?> type) {
        List<Morpher> registered = morphers.get(type);
        if (registered != null) {
            morphers.remove(type);
        }
    }

    /**
     * Deregister the specified Morpher.<br>
     * The registry will remove the target <code>Class</code> from the morphers
     * Map if it has no other registered morphers.
     *
     * @param morpher the target Morpher to remove
     */
    public synchronized void deregisterMorpher(Morpher morpher) {
        List<Morpher> registered = morphers.get(morpher.morphsTo());
        if (registered != null && !registered.isEmpty()) {
            registered.remove(morpher);
            if (registered.isEmpty()) {
                morphers.remove(morpher.morphsTo());
            }
        }
    }

    /**
     * Returns a morpher for <code>clazz</code>.<br>
     * If several morphers are found for that class, it returns the first. If no
     * Morpher is found it will return the IdentityObjectMorpher.
     *
     * @param clazz the target class for which a Morpher may be associated
     */
    public synchronized Morpher getMorpherFor(Class<?> clazz) {
        List<Morpher> registered = morphers.get(clazz);
        if (registered == null || registered.isEmpty()) {
            // no morpher registered for clazz
            return IdentityObjectMorpher.getInstance();
        } else {
            return registered.get(0);
        }
    }

    /**
     * Returns all morphers for <code>clazz</code>.<br>
     * If no Morphers are found it will return an array containing the
     * IdentityObjectMorpher.
     *
     * @param clazz the target class for which a Morpher or Morphers may be
     *              associated
     */
    public synchronized Morpher[] getMorphersFor(Class<?> clazz) {
        List<Morpher> registered = morphers.get(clazz);
        if (registered == null || registered.isEmpty()) {
            // no morphers registered for clazz
            return new Morpher[]{IdentityObjectMorpher.getInstance()};
        } else {
            Morpher[] morphs = new Morpher[registered.size()];
            int k = 0;
            for (Morpher morpher : registered) {
                morphs[k++] = morpher;
            }
            return morphs;
        }
    }

    /**
     * Morphs and object to the specified target class.<br>
     * This method uses reflection to invoke primitive Morphers and Morphers that
     * do not implement ObjectMorpher.
     *
     * @param target the target class to morph to
     * @param value  the value to morph
     * @return an instance of the target class if a suitable Morpher was found
     * @throws MorphException if an error occurs during the conversion
     */
    public Object morph(Class<?> target, Object value) {
        if (value == null) {
            // give the first morpher in the list a shot to convert
            // the value as we can't access type information on it
            Morpher morpher = getMorpherFor(target);
            if (morpher instanceof ObjectMorpher) {
                return ((ObjectMorpher) morpher).morph(value);
            } else {
                try {
                    Method morphMethod = morpher.getClass()
                        .getDeclaredMethod("morph", new Class[]{Object.class});
                    return morphMethod.invoke(morpher, value);
                } catch (Exception e) {
                    throw new MorphException(e);
                }
            }
        }

        Morpher[] morphers = getMorphersFor(target);
        for (int i = 0; i < morphers.length; i++) {
            Morpher morpher = morphers[i];
            if (morpher.supports(value.getClass())) {
                if (morpher instanceof ObjectMorpher) {
                    return ((ObjectMorpher) morpher).morph(value);
                } else {
                    try {
                        Method morphMethod = morpher.getClass()
                            .getDeclaredMethod("morph", new Class[]{Object.class});
                        return morphMethod.invoke(morpher, value);
                    } catch (Exception e) {
                        throw new MorphException(e);
                    }
                }
            }
        }
        return value;
    }

    /**
     * Register a Morpher for a target <code>Class</code>.<br>
     * The target class is the class this Morpher morphs to. If there are another
     * morphers registered to that class, it will be appended to a List.
     *
     * @param morpher a Morpher to register. The method <code>morphsTo()</code>
     *                is used to associate the Morpher to a target Class
     */
    public void registerMorpher(Morpher morpher) {
        registerMorpher(morpher, false);
    }

    /**
     * Register a Morpher for a target <code>Class</code>.<br>
     * The target class is the class this Morpher morphs to. If there are another
     * morphers registered to that class, it will be appended to a List.
     *
     * @param morpher  a Morpher to register. The method <code>morphsTo()</code>
     *                 is used to associate the Morpher to a target Class
     * @param override if registering teh Morpher should override all previously
     *                 registered morphers for the target type
     */
    public synchronized void registerMorpher(Morpher morpher, boolean override) {
        List<Morpher> registered = morphers.get(morpher.morphsTo());
        if (override || registered == null) {
            registered = new ArrayList<Morpher>();
            morphers.put(morpher.morphsTo(), registered);
        }
        if (!registered.contains(morpher)) {
            registered.add(morpher);
        }
    }
}