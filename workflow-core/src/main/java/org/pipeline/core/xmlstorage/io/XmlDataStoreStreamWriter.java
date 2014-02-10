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

import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.pipeline.core.xmlstorage.*;

/**
 * This class just outputs an XmlDataStore to a Stream
 * @author  hugo
 */
public class XmlDataStoreStreamWriter extends XmlDataStoreReadWriter {
    
    /** Creates a new instance of XmlDataStoreStreamWriter */
    public XmlDataStoreStreamWriter(XmlDataStore dataStore) throws XmlStorageException {
        super(dataStore);
    }
    
    /** Write to an output stream */
    public void writeToOutputStream(OutputStream stream) throws XmlStorageException {
        try{
            // Write XML to a string
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            
            DOMSource source = new DOMSource(getDocument());
            StreamResult result = new StreamResult(stream);
            t.transform(source, result);                
            stream.flush();
        } catch (Exception e){
            throw new XmlStorageException("Error writing XML to PrintStream: " + e.getMessage());
        }        
    }
    
    /** Write to a stream */
    public void write(PrintStream printStream) throws XmlStorageException {
        try{
            // Write XML to a string
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            DOMSource source = new DOMSource(getDocument());
            StreamResult result = new StreamResult(printStream);
            t.transform(source, result);                
            printStream.flush();
        } catch (Exception e){
            throw new XmlStorageException("Error writing XML to PrintStream: " + e.getMessage());
        }
    }
}
