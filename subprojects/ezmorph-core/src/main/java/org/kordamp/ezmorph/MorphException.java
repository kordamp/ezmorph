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
package org.kordamp.ezmorph;

/**
 * A <strong>MorphException</strong> indicates that a call to
 * <code>Morpher.morph()</code> has failed to complete successfully.<br>
 * Based on common-beauntils ConversionException.<br>
 *
 * @author Andres Almiray
 */
public class MorphException extends RuntimeException {
    // ----------------------------------------------------------- Constructors

    /**
     * The root cause of this <code>ConversionException</code>, compatible
     * with JDK 1.4's extensions to <code>java.lang.Throwable</code>.
     */
    protected Throwable cause = null;

    /**
     * Construct a new exception with the specified message.
     *
     * @param message The message describing this exception
     */
    public MorphException(String message) {
        super(message);
    }

    /**
     * Construct a new exception with the specified message and root cause.
     *
     * @param message The message describing this exception
     * @param cause   The root cause of this exception
     */
    public MorphException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    // ------------------------------------------------------------- Properties

    /**
     * Construct a new exception with the specified root cause.
     *
     * @param cause The root cause of this exception
     */
    public MorphException(Throwable cause) {
        super(cause.getMessage());
        this.cause = cause;
    }

    /**
     * Returns the cause of this exception.
     *
     * @return a Throwable that represents the cause of this exception
     */
    public Throwable getCause() {
        return this.cause;
    }
}