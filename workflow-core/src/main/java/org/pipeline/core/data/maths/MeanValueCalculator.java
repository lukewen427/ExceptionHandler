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

package org.pipeline.core.data.maths;

import org.pipeline.core.data.*;

/**
 * Calculates the average value of a numerical column
 * @author hugo
 */
public class MeanValueCalculator {
    /** Column to calculate */
    private Column column;
    
    /** Creates a new instance of MeanValueCalculator */
    public MeanValueCalculator(NumericalColumn column) {
        this.column = (Column)column;
    }
    
    /** Calculate mean */
    public double doubleValue() {
        int count = 0;
        int n = column.getNonMissingRows();
        if(n>0){
            double mean = 0;
            NumericalColumn numberCol = (NumericalColumn)column;
            
            int size = column.getRows();
            for(int i=0;i<size;i++){
                if(!column.isMissing(i)){
                    try {
                        mean = mean + (numberCol.getDoubleValue(i) / n);
                    } catch (Exception e){
                        // Thrown by missing - ignore
                    }
                }
            }
            return mean;
            
        } else {
            return Double.NaN;
        }
    }
    
    /** Integer value */
    public int intValue(){
        return (int)doubleValue();
    }
    
    /** Long value */
    public long longValue(){
        return (long)doubleValue();
    }
}
