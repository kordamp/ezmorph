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
package org.kordamp.ezmorph.bean.sample;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.Map;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class PrimitiveBean {
    private int[] parray;
    private ObjectBean pbean;
    private boolean pboolean;
    private byte pbyte;
    private char pchar;
    private Class pclass;
    private double pdouble;
    private float pfloat;
    private int pint;
    private List plist;
    private long plong;
    private Map pmap;
    private short pshort;
    private String pstring;

    public int[] getParray() {
        return parray;
    }

    public ObjectBean getPbean() {
        return pbean;
    }

    public byte getPbyte() {
        return pbyte;
    }

    public char getPchar() {
        return pchar;
    }

    public Class getPclass() {
        return pclass;
    }

    public double getPdouble() {
        return pdouble;
    }

    public float getPfloat() {
        return pfloat;
    }

    public int getPint() {
        return pint;
    }

    public List getPlist() {
        return plist;
    }

    public long getPlong() {
        return plong;
    }

    public Map getPmap() {
        return pmap;
    }

    public short getPshort() {
        return pshort;
    }

    public String getPstring() {
        return pstring;
    }

    public boolean isPboolean() {
        return pboolean;
    }

    public void setParray(int[] parray) {
        this.parray = parray;
    }

    public void setPbean(ObjectBean pbean) {
        this.pbean = pbean;
    }

    public void setPboolean(boolean pboolean) {
        this.pboolean = pboolean;
    }

    public void setPbyte(byte pbyte) {
        this.pbyte = pbyte;
    }

    public void setPchar(char pchar) {
        this.pchar = pchar;
    }

    public void setPclass(Class pclass) {
        this.pclass = pclass;
    }

    public void setPdouble(double pdouble) {
        this.pdouble = pdouble;
    }

    public void setPfloat(float pfloat) {
        this.pfloat = pfloat;
    }

    public void setPint(int pint) {
        this.pint = pint;
    }

    public void setPlist(List plist) {
        this.plist = plist;
    }

    public void setPlong(long plong) {
        this.plong = plong;
    }

    public void setPmap(Map pmap) {
        this.pmap = pmap;
    }

    public void setPshort(short pshort) {
        this.pshort = pshort;
    }

    public void setPstring(String pstring) {
        this.pstring = pstring;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}