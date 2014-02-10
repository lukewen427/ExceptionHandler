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

package org.pipeline.core.drawing.customio;
import org.pipeline.core.drawing.*;
import org.pipeline.core.drawing.model.*;

/**
 * This class provides a custom input definition.
 * @author hugo
 */
public class CustomInputDefinition extends CustomPortDefinition {
    
    /** Creates a new instance of CustomInputDefinition */
    public CustomInputDefinition() {
        super();
        setLocation(PortModel.LEFT_OF_BLOCK);
    }
    
    /** Create a port based on this definition */
    public PortModel createPort(BlockModel block) {
        DefaultInputPortModel port = new DefaultInputPortModel(this.getName(), getLocation(), getOffset(), block);
        if(getDataType()!=null){
            port.addDataType(getDataType());
            port.setDataTypeRestricted(true);
        } else {
            port.setDataTypeRestricted(false);
        }
        port.setStreamable(isStreamable());
        return port;
    }
    
    /** Override toString method to provide useful text on the IO editor */
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("input");
        buffer.append(": ");
        buffer.append(getName());
        buffer.append("  ");
        buffer.append("at");
        buffer.append(" ");
        buffer.append(getOffset());
        buffer.append("%  ");
        buffer.append("along");
        buffer.append(" ");
        buffer.append(getLocationText());
        return buffer.toString();
       
    }
}
