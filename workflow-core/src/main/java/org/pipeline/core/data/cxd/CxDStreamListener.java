/*
 * CxDStreamListener.java
 */

package org.pipeline.core.data.cxd;
import org.pipeline.core.data.*;

/**
 * This interface defines the functionality of a class that can listen to data
 * being streamed from a CxD file. It streams on a row-by-row basis.
 * @author hugo
 */
public interface CxDStreamListener {
    /** A new row has been read */
    public void newRow(Data row);
}
