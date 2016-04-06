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
package com.itextpdf.kernel.pdf;

import com.itextpdf.io.source.ByteUtils;

public class PdfNumber extends PdfPrimitiveObject {

    private static final long serialVersionUID = -250799718574024246L;
	protected static final byte Int = 1;
    protected static final byte Double = 2;

    private double value;
    private byte valueType;

    public PdfNumber(double value) {
        super();
        setValue(value);
    }

    public PdfNumber(int value) {
        super();
        setValue(value);
    }

    public PdfNumber(byte[] content) {
        super(content);
        this.valueType = Double;
        this.value = java.lang.Double.NaN;
    }

    private PdfNumber() {
        super();
    }

    @Override
    public byte getType() {
        return Number;
    }

    public double getValue() {
        if (java.lang.Double.isNaN(value))
            generateValue();
        return value;
    }

    public float getFloatValue() {
        return (float) getValue();
    }

    public long getLongValue() {
        return (long) getValue();
    }

    public int getIntValue() {
        return (int) getValue();
    }

    public void setValue(int value) {
        this.value = value;
        this.valueType = Int;
        this.content = null;
    }

    public void setValue(double value) {
        this.value = value;
        this.valueType = Double;
        this.content = null;
    }

    public void increment() {
        setValue(++value);
    }

    public void decrement() {
        setValue(--value);
    }

    /**
     * Marks object to be saved as indirect.
     *
     * @param document a document the indirect reference will belong to.
     * @return object itself.
     */
    @SuppressWarnings("unchecked")
    @Override
    public PdfNumber makeIndirect(PdfDocument document) {
        return super.makeIndirect(document);
    }

    /**
     * Marks object to be saved as indirect.
     *
     * @param document a document the indirect reference will belong to.
     * @return object itself.
     */
    @SuppressWarnings("unchecked")
    @Override
    public PdfNumber makeIndirect(PdfDocument document, PdfIndirectReference reference) {
        return super.makeIndirect(document, reference);
    }

    /**
     * Copies object to a specified document.
     * Works only for objects that are read from existing document, otherwise an exception is thrown.
     *
     * @param document document to copy object to.
     * @return copied object.
     */
    @SuppressWarnings("unchecked")
    @Override
    public PdfNumber copyTo(PdfDocument document) {
        return super.copyTo(document, true);
    }

    /**
     * Copies object to a specified document.
     * Works only for objects that are read from existing document, otherwise an exception is thrown.
     *
     * @param document         document to copy object to.
     * @param allowDuplicating indicates if to allow copy objects which already have been copied.
     *                         If object is associated with any indirect reference and allowDuplicating is false then already existing reference will be returned instead of copying object.
     *                         If allowDuplicating is true then object will be copied and new indirect reference will be assigned.
     * @return copied object.
     */
    @SuppressWarnings("unchecked")
    @Override
    public PdfNumber copyTo(PdfDocument document, boolean allowDuplicating) {
        return super.copyTo(document, allowDuplicating);
    }

    @Override
    public String toString() {
        if (content != null)
            return new String(content);
        else if (valueType == Int)
            return new String(ByteUtils.getIsoBytes(getIntValue()));
        else
            return new String(ByteUtils.getIsoBytes(getValue()));
    }

    @Override
    protected PdfNumber newInstance() {
        return new PdfNumber();
    }

    protected byte getValueType() {
        return valueType;
    }

    @Override
    protected void generateContent() {
        switch (valueType) {
            case Int:
                content = ByteUtils.getIsoBytes((int) value);
                break;
            case Double:
                content = ByteUtils.getIsoBytes(value);
                break;
            default:
                content = new byte[0];
        }
    }

    protected void generateValue() {
        try {
            value = java.lang.Double.parseDouble(new String(content));
        } catch (NumberFormatException e) {
            value = java.lang.Double.NaN;
        }
        valueType = Double;
    }

    @Override
    protected void copyContent(PdfObject from, PdfDocument document) {
        super.copyContent(from, document);
        PdfNumber number = (PdfNumber) from;
        value = number.value;
        valueType = number.valueType;
    }

}