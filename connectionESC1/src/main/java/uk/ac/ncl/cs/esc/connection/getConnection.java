package uk.ac.ncl.cs.esc.connection;

import org.pipeline.core.drawing.model.DefaultDrawingModel;

import com.connexience.server.api.API;
import com.connexience.server.api.IDynamicWorkflowService;
import com.connexience.server.api.IWorkflow;
import com.connexience.server.workflow.blocks.processor.DataProcessorBlock;


public interface getConnection {
	 public  API getAPI()throws Exception ;
	 public boolean getVerify(String username,String password);
	 public IDynamicWorkflowService getService(String id) throws Exception;
	 public DataProcessorBlock createBlock(IDynamicWorkflowService serviceDoc) throws Exception;
	 public IWorkflow createWorkflow(String name, DefaultDrawingModel drawing) throws Exception;
	 public API createAPI() throws Exception;
	 public API createCloudAPI(String cloud) throws Exception;
}

  