/*
    $Id$

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV
    Authors: Bruno Lowagie, Paulo Soares, et al.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS

    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/

    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.

    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.

    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.

    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.io.font.cmap;

import com.itextpdf.io.font.PdfEncodings;

import java.util.ArrayList;
import java.util.List;

/**
 * @author psoares
 */
public abstract class AbstractCMap {

    private String cmapName;
    private String registry;
    private String ordering;
    private int supplement;
    
    public String getName() {
        return cmapName;
    }

    void setName(String cmapName) {
        this.cmapName = cmapName;
    }
    
    public String getOrdering() {
        return ordering;
    }

    void setOrdering(String ordering) {
        this.ordering = ordering;
    }
    
    public String getRegistry() {
        return registry;
    }

    void setRegistry(String registry) {
        this.registry = registry;
    }
    
    public int getSupplement() {
        return supplement;
    }
    
    void setSupplement(int supplement) {
        this.supplement = supplement;
    }

    abstract void addChar(String mark, CMapObject code);
    
    void addRange(String from, String to, CMapObject code) {
        byte[] a1 = decodeStringToByte(from);
        byte[] a2 = decodeStringToByte(to);
        if (a1.length != a2.length || a1.length == 0) {
            throw new IllegalArgumentException("Invalid map.");
        }
        byte[] sout = null;
        if (code.isString()) {
            sout = decodeStringToByte(code.toString());
        }
        int start = a1[a1.length - 1] & 0xff;
        int end = a2[a2.length - 1] & 0xff;
        for (int k = start; k <= end; ++k) {
            a1[a1.length - 1] = (byte)k;
            String mark = PdfEncodings.convertToString(a1, null);
            if (code.isArray()) {
                List<CMapObject> codes = (ArrayList<CMapObject>) code.getValue();
                addChar(mark, codes.get(k - start));
            } else if (code.isNumber()) {
                int nn = (int)code.getValue() + k - start;
                addChar(mark, new CMapObject(CMapObject.Number, nn));
            } else if (code.isString()) {
                CMapObject s1 = new CMapObject(CMapObject.HexString, sout);
                addChar(mark, s1);
                assert sout != null;
                ++sout[sout.length - 1];
            }
        }
    }
    
//    protected static byte[] toByteArray(String value) {
//        if (PdfEncodings.isPdfDocEncoding(value)) {
//            return PdfEncodings.convertToBytes(value, PdfEncodings.PdfDocEncoding);
//        } else {
//            return PdfEncodings.convertToBytes(value, null);
//        }
//    }

    public static byte[] decodeStringToByte(String range) {
        byte[] bytes = new byte[range.length()];
        for (int i = 0; i < range.length(); i++) {
            bytes[i] = (byte)range.charAt(i);
        }
        return bytes;
    }

    protected String toUnicodeString(String value, boolean isHexWriting) {
        byte[] bytes = decodeStringToByte(value);
        if (isHexWriting) {
            return PdfEncodings.convertToString(bytes, "UnicodeBigUnmarked");
        } else {
            if (bytes.length >= 2 && bytes[0] == (byte)254 && bytes[1] == (byte)255) {
                return PdfEncodings.convertToString(bytes, PdfEncodings.UnicodeBig);
            } else {
                return PdfEncodings.convertToString(bytes, PdfEncodings.PdfDocEncoding);
            }
        }
    }
}