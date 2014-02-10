/*
 * SerializedRow.java
 */

package com.connexience.server.workflow.engine.data;
import org.pipeline.core.data.*;
import java.io.*;

/**
 * This class contains a serialized row of data
 * @author hugo
 */
public class SerializedRow implements Serializable {
    static final long serialVersionUID = -1830686151688620462L;
    
    Object[] values;

    public SerializedRow() {
    }

    public SerializedRow(Data data, int rowIndex){
        try {
            Column c;
            values = new Object[data.getColumns()];

            for(int i=0;i<data.getColumns();i++){
                c = data.column(i);
                if(rowIndex<c.getRows()){
                    values[i] = c.getObjectValue(rowIndex);
                } else {
                    values[i] = null;
                }
            }
        } catch (Exception e){
            
        }
    }

    public SerializedRow(Object[] values) {
        this.values = values;
    }

    public Object[] getValues(){
        return values;
    }

    public Object getValue(int index){
        return values[index];
    }
}