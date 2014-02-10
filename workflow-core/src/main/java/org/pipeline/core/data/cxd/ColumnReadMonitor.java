/*
 * ColumnReadMonitor.java
 */
package org.pipeline.core.data.cxd;
import org.pipeline.core.data.*;

/**
 * This class monitors the load process of a set of data
 * @author hugo
 */
public class ColumnReadMonitor {
    /** Number of columns */
    private int columns;
    
    /** Set of flags to indicate when all of the columns have finished */
    private boolean[] finishedFlags;
    
    /** Set of rows which is updated as data is read */
    
    /** Set the number of columns */
    public void setColumns(int columns){
        this.columns = columns;
        finishedFlags = new boolean[columns];
        for(int i=0;i<columns;i++){
            finishedFlags[i] = false;
        }
    }
    
    /** Have all the columns finished loading */
    public synchronized boolean allColumnsFinished(){
        for(int i=0;i<columns;i++){
            if(finishedFlags[i]==false){
                return false;
            }
        }
        return true;
    }
    
    /** Set a flag specifying that a column has been completely read */
    public void setColumnFinished(int index){
        finishedFlags[index] = true;
    }
    
    /** A new row has been read for a column */
    public void rowRead(int index, Column col) {

    }
}
