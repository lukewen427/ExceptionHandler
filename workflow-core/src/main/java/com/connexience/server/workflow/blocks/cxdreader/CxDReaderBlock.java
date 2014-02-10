/*
 * CxDReaderBlock.java
 */

package com.connexience.server.workflow.blocks.cxdreader;

import com.connexience.server.workflow.engine.*;
import com.connexience.server.workflow.blocks.*;

import org.pipeline.core.drawing.*;
import org.pipeline.core.drawing.model.*;
import org.pipeline.core.xmlstorage.*;
import org.pipeline.core.data.*;
import org.pipeline.core.data.cxd.*;

import java.io.*;

/**
 * This block just loads the specified CxD File into memory. It
 * is a temporary block that will have no purpose when the proper
 * data link blocks are created.
 * @author hugo
 */
public class CxDReaderBlock extends DefaultBlockModel {

    public CxDReaderBlock() throws DrawingException {
        super();
        getEditableProperties().add("FileName", "/Users/hugo/data.cxd", "Name of CxD file to import");
        
        DefaultOutputPortModel output = new DefaultOutputPortModel("imported-data", PortModel.RIGHT_OF_BLOCK, 50, this);
        output.addDataType(DataTypes.DATA_WRAPPER_TYPE);
        addOutputPort(output);
    }

    /** Import CxD File */
    @Override
    public BlockExecutionReport execute() throws BlockExecutionException {
        Data outputData = null;
        File dataFile = new File(getEditableProperties().stringValue("FileName", ""));
        if(dataFile.exists()){
            try {
                CxdFile file = new CxdFile(dataFile);
                outputData = file.load();
                DrawingDataUtilities.setOutputData(getOutput("imported-data"), outputData);
            } catch (Exception e){
                e.printStackTrace();
                return new BlockExecutionReport(this, BlockExecutionReport.INTERNAL_ERROR, "Error loading data: " + e.getMessage());
            }
        } else {
            return new BlockExecutionReport(this, BlockExecutionReport.INTERNAL_ERROR, "Data file does not exist");
        }
        return new BlockExecutionReport(this);
        
    }

    
}
