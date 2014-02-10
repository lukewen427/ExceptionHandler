/* =================================================================
 *                     conneXience Data Pipeline
 * =================================================================
 *
 * Copyright 2006 Hugo Hiden and Adrian Conlin
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details. 
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.pipeline.core.xmlstorage.io;
import org.pipeline.core.xmlstorage.*;
import java.io.*;

/**
 * This class provides byte array storage / retrieval for XmlDataStores
 * @author hugo
 */
public class XmlDataStoreByteArrayIO extends XmlDataStoreReadWriter {
    /** Object data */
    private byte[] data;
    
    /** Creates a new instance of XmlDataStoreByteArrayIO */
    public XmlDataStoreByteArrayIO(XmlDataStore dataStore) throws XmlStorageException {
        super(dataStore);
    }
    
    /** Creates from a byte array */
    public XmlDataStoreByteArrayIO(byte[] data) throws XmlStorageException {
        this.data = data;
    }
    
    /** Save to byte array */
    public byte[] toByteArray() throws XmlStorageException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        PrintStream p = new PrintStream(byteStream);
        XmlDataStoreStreamWriter writer = new XmlDataStoreStreamWriter(getDataStore());
        writer.setDescriptionIncluded(isDescriptionIncluded());
        writer.write(p);
        p.flush();                    
        return byteStream.toByteArray();              
    }
    
    /** Access as XmlDataStore */
    public XmlDataStore toXmlDataStore() throws XmlStorageException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        XmlDataStoreStreamReader reader = new XmlDataStoreStreamReader(byteStream);
        return reader.read();
    }
}
