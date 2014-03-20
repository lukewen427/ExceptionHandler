package uk.ac.ncl.cs.esc.workflow.restructure;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.ncl.cs.esc.deployment.DpartitionSet;
import uk.ac.ncl.cs.esc.partitiontool.BlockSet;

import com.connexience.server.api.APIConnectException;
import com.connexience.server.api.APIInstantiationException;
import com.connexience.server.api.APIParseException;
import com.connexience.server.api.APISecurityException;
import com.connexience.server.api.IDocument;

public interface WorkflowRestructure {
	public JSONObject getWorkflowAsJsonObject(String workflowId) throws Exception;
	public HashMap<String,String> Blocklist(String workflowId) throws Exception;
	public HashMap<String, ArrayList<String>>  ConnectionMap(String workflowId) throws Exception;
	public HashMap<String,String> Workflowlist() throws Exception;
	public  HashMap<String,ByteArrayOutputStream> CreateWorkflow(String cloudName,ArrayList<Object> partition,String partitionName,
			ArrayList<ArrayList<String>> connections,BlockSet blockset,ArrayList<ArrayList<String>>inputs,
			HashMap<String, ByteArrayOutputStream> theresults) throws Exception;
	public String getBlockServiceId(String BlockId,String workflowId) throws Exception;
	public ArrayList<String> getPors(String workflowId,String sourceId,String endId) throws Exception;
	public ArrayList<ArrayList<String>> getSource(String workflowId) throws Exception;
	public ArrayList<String> getInputports(String workflowId,String blockId) throws Exception;
	public ArrayList<String> getOutputports(String workflowId,String blockId) throws Exception;
	public ArrayList<ArrayList<String>> getConnection(String workflowId) throws Exception;
	public String getBlockName(String BlockId,String workflowId) throws Exception;
}

