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

package org.pipeline.core.data.manipulation.text;
import org.pipeline.core.data.manipulation.*;
import org.pipeline.core.data.*;
import org.pipeline.core.data.columns.*;
import org.pipeline.core.data.text.*;
import org.pipeline.core.xmlstorage.*;

import java.util.*;

/**
 * This class splits a text column into a set of columns based on a delimiter
 * @author nhgh
 */
public class TextColumnSplitterPicker extends ColumnPicker {
    /** Delimiting text string used to split column */
    private TextColumnSplitter splitter = new TextColumnSplitter();
    
    /**
     * Creates a new instance of TextColumnSplitterPicker
     */
    public TextColumnSplitterPicker() {
        super();
        setCopyData(false);
        setLimitColumnTypes(false);
        setMultiColumnPicker(true);
    }
    
    /** Get the delimiter text */
    public String getDelimiter(){
        return splitter.getDelimiter();
    }
    
    /** Set the delimiter text */
    public void setDelimiterText(String delimiter){
        splitter.setDelimiter(delimiter);
    }

    /** Extract columns */
    public Column[] pickColumns(Data data) throws IndexOutOfBoundsException, DataException {
        Column column = super.pickColumn(data);
        return splitter.splitColumn(column);
    }
}