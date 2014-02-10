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

package org.pipeline.core.data;

import org.pipeline.core.data.columns.*;

import java.awt.Color;
import java.util.*;

/**
 * This class can create columns for specific classes of data and
 * is used so that the data handling routines can be extended with
 * additional column types.
 * @author hugo
 */
public abstract class ColumnFactory {    
    /** Column types */
    private static Hashtable columnTypes = new Hashtable();
    
    /** Column class type list */
    private static Hashtable columnClassTypes = new Hashtable();
    
    /** Register default column types */
    static {
        registerColumnType(new ColumnTypeInfo("date-column", "Date", Date.class, DateColumn.class));
        registerColumnType(new ColumnTypeInfo("double-column", "Double", Double.class, DoubleColumn.class));
        registerColumnType(new ColumnTypeInfo("integer-column", "Integer", Long.class, IntegerColumn.class));
        registerColumnType(new ColumnTypeInfo("string-column", "Text", String.class, StringColumn.class, Color.WHITE, Color.BLUE));
    }      
    
    /** Register a column type */
    public static void registerColumnType(ColumnTypeInfo type){
        if(!columnTypes.containsKey(type.getId())){
            columnTypes.put(type.getId(), type);
            columnClassTypes.put(type.getColumnRepresentationClass(), type);
        }
    }
    
    /** Remove a column type */
    public static void unregisterColumnType(String id){
        try {
            ColumnTypeInfo type = getColumnType(id);
            columnClassTypes.remove(type.getColumnRepresentationClass());
        } catch (Exception e){
        }
        columnTypes.remove(id);
    }
    
    /** Get a column type */
    public static ColumnTypeInfo getColumnType(String typeId){
        return (ColumnTypeInfo)columnTypes.get(typeId);
    }
    
    /** Get the column type info for a specified representation class */
    public static ColumnTypeInfo getColumnTypeInfo(Class representationClass){
        return ((ColumnTypeInfo)columnClassTypes.get(representationClass));
    }
    
    /** Get the column type info for the specified column */
    public static ColumnTypeInfo getColumnTypeInfo(Column column) throws DataException {
        if(columnClassTypes.containsKey(column.getClass())){
            return ((ColumnTypeInfo)columnClassTypes.get(column.getClass()));
        } else {
            throw new DataException("Cannot identify column type of: " + column.getName());
        }
    }
    
    /** Get the column type info for the specified column */
    public static ColumnTypeInfo getColumnTypeInfo(ColumnMetaData metaData) throws DataException {        
        if(columnClassTypes.containsKey(metaData.getDataType())){
            return ((ColumnTypeInfo)columnClassTypes.get(metaData.getDataType()));
        } else {
            throw new DataException("Cannot identify column type of: " + metaData.getName());
        }        
    }
    
    /** Create a column */
    public static Column createColumn(String id) throws DataException {
        try {
            if(columnTypes.containsKey(id)){
                ColumnTypeInfo info = (ColumnTypeInfo)columnTypes.get(id);
                Object col = info.getColumnRepresentationClass().newInstance();
                return (Column)col;
                
            } else {
                throw new DataException("Column type: " + id + " does not exist");
            }
            
        } catch (Exception e){
            throw new DataException("Cannot create column: " + e.getMessage());
        }
    }
    
    /** List the column types */
    public static Enumeration getColumnTypes(){
        return columnTypes.elements();
    }
    
    /** Create an empty set of data from a set of DataMetaData */
    public static Data createEmptyData(DataMetaData metaData) throws DataException {
        Data data = new Data();
        ColumnMetaData[] columns = metaData.columnArray();
        Column column;
        
        // Create the new empty columns
        for(int i=0;i<columns.length;i++){
            column = createColumn(columns[i].getColumnTypeId());
            column.setName(columns[i].getName());
            data.addColumn(column);
        }
        
        return data;
    }

    /** Convert a column from one type to another */
    public static Column convert(Column source, String newType) throws DataException {
        Column target = createColumn(newType);
        for(int i=0;i<source.getRows();i++){
            if(!source.isMissing(i)){
                target.appendStringValue(source.getStringValue(i));
            } else {
                target.appendObjectValue(new MissingValue());
            }
        }
        return target;
    }

    /** Change the data type of a column in a data set */
    public static void changeColumnType(Data data, int index, String newType) throws DataException {
        Column newColumn = convert(data.column(index), newType);
        data.replaceColumn(index, newColumn);
    }
}
