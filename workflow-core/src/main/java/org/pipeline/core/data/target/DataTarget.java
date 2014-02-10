/*
 * DataTarget.java
 */

package org.pipeline.core.data.target;

import org.pipeline.core.data.*;

/**
 * This interface defines a data target that can accept data from an external
 * source and retain or transfer it somewhere else.
 * @author hugo
 */
public interface DataTarget {
    /** Set the data in this target */
    public void setData(Data data) throws DataException;
}