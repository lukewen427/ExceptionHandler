/*
 * JsonMetaDataExporter.java
 */
package org.pipeline.core.data.io;

import org.pipeline.core.data.*;
import org.json.*;

/**
 * This class writes a DataMetaData object to a JSON object
 * @author hugo
 */
public class JsonMetaDataExporter {
    /** Metadata to export */
    private DataMetaData metaData;

    public JsonMetaDataExporter(DataMetaData metaData) {
        this.metaData = metaData;
    }
    
    public JSONObject toJson() throws DataExportException {
        try {
            JSONObject mdJson = new JSONObject();
            JSONArray columns = new JSONArray();
            JSONObject columnJson;
            ColumnMetaData col;
            for(int i=0;i<metaData.getColumns();i++){
                col = metaData.column(i);
                columnJson = new JSONObject();
                columnJson.put("name", col.getName());
                columnJson.put("type", col.getColumnTypeId());
                columns.put(columnJson);
            }
            mdJson.put("columns", columns);
            return mdJson;
        } catch (Exception e){
            throw new DataExportException("Error creating metadata json: " + e.getMessage(), e);
        }
    }
}