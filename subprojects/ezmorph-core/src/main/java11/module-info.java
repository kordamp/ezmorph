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

/**
 * @author Andres Almiray
 */
module org.kordamp.ezmorph {
    requires java.desktop;
    requires org.apache.commons.lang3;
    requires org.slf4j;

    requires transitive commons.beanutils;
    requires transitive junit;

    exports org.kordamp.ezmorph;
    exports org.kordamp.ezmorph.array;
    exports org.kordamp.ezmorph.bean;
    exports org.kordamp.ezmorph.object;
    exports org.kordamp.ezmorph.primitive;
    exports org.kordamp.ezmorph.test;
}