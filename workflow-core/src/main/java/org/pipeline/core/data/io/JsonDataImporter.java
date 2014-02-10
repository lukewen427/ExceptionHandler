/*
 * JsonDataImporter.java
 */

package org.pipeline.core.data.io;

import org.pipeline.core.data.*;
import org.json.*;

/**
 * Create a set of data from a JSON representation
 * @author hugo
 */
public class JsonDataImporter {
    /** JSON Object being imported */
    private JSONObject dataJson;

    public JsonDataImporter(JSONObject dataJson) {
        this.dataJson = dataJson;
    }

    public Data toData() throws DataImportException {
        try {
            Data data = new Data();
            int columnCount = dataJson.getInt("columns");
            int rowCount = dataJson.getInt("rowCount");
            JSONArray columnNames = dataJson.getJSONArray("columnNames");
            JSONArray columnTypes = dataJson.getJSONArray("columnTypes");
            JSONArray rows = dataJson.getJSONArray("rows");
            JSONArray rowJson;
            Column column;
            String value;

            // Create the columns
            for(int i=0;i<columnCount;i++){
                column = ColumnFactory.createColumn(columnTypes.getString(i));
                column.setName(columnNames.getString(i));
                data.addColumn(column);
            }

            // Enter the data
            for(int i=0;i<rowCount;i++){
                rowJson = rows.getJSONArray(i);
                for(int j=0;j<rowJson.length();j++){
                    value = rowJson.getString(j);
                    if(!value.equals(MissingValue.MISSING_VALUE_REPRESENTATION)){
                        data.column(j).appendStringValue(value);
                    } else {
                        data.column(j).appendObjectValue(new MissingValue());
                    }
                }
            }

            // Load the index data if there is any
            if(dataJson.has("indexColumn")){
                JSONObject indexColumnJson = dataJson.getJSONObject("indexColumn");
                String columnType = indexColumnJson.getString("columnType");
                String name = indexColumnJson.getString("name");
                Column indexColumn = ColumnFactory.createColumn(columnType);
                indexColumn.setName(name);
                JSONArray indexDataJson = indexColumnJson.getJSONArray("data");
                for(int i=0;i<indexDataJson.length();i++){
                    value = indexDataJson.getString(i);
                    if(!value.equals(MissingValue.MISSING_VALUE_REPRESENTATION)){
                        indexColumn.appendCxDFormatValue(value);
                    } else {
                        indexColumn.appendObjectValue(new MissingValue());
                    }
                }
                data.setIndexColumn(indexColumn);
            }
            
            // Load the annotations if there are any
            if(dataJson.has("hasAnnotations") && dataJson.getBoolean("hasAnnotations")==true){
                data.getAnnotations().parseJson(dataJson.getJSONObject("annotations"));
            }
            
            // Load the properties if there are any
            if(dataJson.has("hasProperties") && dataJson.getBoolean("hasProperties")==true){
                data.setProperties(new SimpleJsonPropertiesImporter(dataJson.getJSONObject("properties")).parseJson());
            }
            return data;
        } catch (Exception e){
            throw new DataImportException("Error parsing JSON: " + e.getMessage(), e);
        }
    }
}