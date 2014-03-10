package uk.ac.ncl.cs.esc.connection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.pipeline.core.drawing.model.DefaultDrawingModel;
import org.pipeline.core.xmlstorage.XmlDataStore;
import org.pipeline.core.xmlstorage.io.XmlDataStoreStreamWriter;

import com.connexience.server.api.API;
import com.connexience.server.api.APIConnectException;
import com.connexience.server.api.APIFactory;
import com.connexience.server.api.APIInstantiationException;
import com.connexience.server.api.APIParseException;
import com.connexience.server.api.APISecurityException;
import com.connexience.server.api.IDocument;
import com.connexience.server.api.IDocumentVersion;
import com.connexience.server.api.IDynamicWorkflowService;
import com.connexience.server.api.IFolder;
import com.connexience.server.api.IUser;
import com.connexience.server.api.IWorkflow;
import com.connexience.server.api.impl.InkspotTypeRegistration;
import com.connexience.server.workflow.blocks.processor.DataProcessorBlock;
import com.connexience.server.workflow.service.DataProcessorServiceDefinition;


public class myConnection implements getConnection {
	
	private  API api;	  
	  
     public API createAPI() throws Exception{
	    	
    /*	   String APPLICATION_ID = "ff8081813809e96a01396c7ae0a25230";
			String APPLICATION_KEY = "617f46004c326010edfc94d0f918d1e9";
		 	String urlString="http://www.esciencecentral.co.uk/APIServer";
			InkspotTypeRegistration.register();
	        APIFactory factory = new APIFactory();
	        factory.setApiClass(com.connexience.client.api.impl.HttpClientAPI.class);
	        api = factory.authenticateApplication(new URL(urlString), APPLICATION_ID, APPLICATION_KEY);
	        api.authenticate("wenluke427@gmail.com","laowen427");
	    	  return api;  */
    	 
   	 String APPLICATION_ID = "ff8080813a022d6e013a02326740000a";
			String APPLICATION_KEY = "1a8ab737fce0ce60978fd2ecd8374ed1";
		 	String urlString="http://10.66.66.176:8080/APIServer";
			InkspotTypeRegistration.register();
	        APIFactory factory = new APIFactory();
	        factory.setApiClass(com.connexience.client.api.impl.HttpClientAPI.class);
	        api = factory.authenticateApplication(new URL(urlString), APPLICATION_ID, APPLICATION_KEY);
	        api.authenticate("h.g.hiden@ncl.ac.uk","hugo");
	        return api;
    	 
	    }
	   public API createCloudAPI(String cloud) throws Exception{
		   
		   if(cloud.equals("Cloud0")){
		    String APPLICATION_ID = "ff8080813a022d6e013a02326740000a";
			String APPLICATION_KEY = "1a8ab737fce0ce60978fd2ecd8374ed1";
		 	String urlString="http://10.8.149.12:8080/APIServer";
			InkspotTypeRegistration.register();
	        APIFactory factory = new APIFactory();
	        factory.setApiClass(com.connexience.client.api.impl.HttpClientAPI.class);
	        api = factory.authenticateApplication(new URL(urlString), APPLICATION_ID, APPLICATION_KEY);
	        api.authenticate("h.g.hiden@ncl.ac.uk","hugo");
	        return api;
	        
	        }else{
	        	String APPLICATION_ID = "ff8080813a022d6e013a02326740000a";
				String APPLICATION_KEY = "1a8ab737fce0ce60978fd2ecd8374ed1";
			 	String urlString="http://10.66.66.176:8080//APIServer";
				InkspotTypeRegistration.register();
		        APIFactory factory = new APIFactory();
		        factory.setApiClass(com.connexience.client.api.impl.HttpClientAPI.class);
		        api = factory.authenticateApplication(new URL(urlString), APPLICATION_ID, APPLICATION_KEY);
		        api.authenticate("h.g.hiden@ncl.ac.uk","hugo");
	    	   return api;
	        }
		  
	   } 
   public  API getAPI() throws Exception {
		  
		   if(api!=null){
	            return api;
	        } else {
	            throw new Exception("Parser API has not been created yet");
	        }
			
		}
	public boolean getVerify(String username,String password){
		String APPLICATION_ID = "ff8080813a022d6e013a02326740000a";
		String APPLICATION_KEY = "1a8ab737fce0ce60978fd2ecd8374ed1";
	 	String urlString="http://10.66.66.176:8080/APIServer";
		
	 //           String APPLICATION_ID = "ff8080813a022d6e013a02326740000a";
		//		String APPLICATION_KEY = "1a8ab737fce0ce60978fd2ecd8374ed1";
		//	 	String urlString="http://10.8.149.10:8080/APIServer";
		
//		String APPLICATION_ID = "ff8081813809e96a01396c7ae0a25230";
//		String APPLICATION_KEY = "617f46004c326010edfc94d0f918d1e9";
//	 	String urlString="http://www.esciencecentral.co.uk/APIServer";
				InkspotTypeRegistration.register();
		        APIFactory factory = new APIFactory();
		        factory.setApiClass(com.connexience.client.api.impl.HttpClientAPI.class);
		        try {
					 api = factory.authenticateApplication(new URL(urlString), APPLICATION_ID, APPLICATION_KEY);
					try {
						api.authenticate(username, password);
					} catch (APISecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					} catch (APIParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					} catch (APIInstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}
				} catch (APIConnectException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
		        return true;
		
	}
	public IDynamicWorkflowService getService(String id) throws Exception {
		//System.out.println(id);
		IDocument doc = api.getDocument(id);
		if(doc instanceof IDynamicWorkflowService){
			return (IDynamicWorkflowService) doc;
		}else{
			throw new Exception("Object is not a service");
		}
		
	}
	public DataProcessorBlock createBlock(IDynamicWorkflowService serviceDoc)
			throws Exception {
		  List<IDocumentVersion> serviceVersions = api.getDocumentVersions(serviceDoc);
	        IDocumentVersion version = null;
	        int versionNumber = 0;
	        for(IDocumentVersion v : serviceVersions){
	            if(v.getVersionNumber()>=versionNumber){
	                version = v;
	            }
	        }
	        
	        if(version!=null){
	            DataProcessorBlock block = new DataProcessorBlock();

	            String serviceXml = api.getServiceXml(serviceDoc);
	            DataProcessorServiceDefinition def = new DataProcessorServiceDefinition();
	            def.loadXmlString(serviceXml);


	            block.setServiceDefinition(def);
	            block.initialiseForService();
	            
	            block.setServiceId(serviceDoc.getId());
	            block.setVersionId(version.getId());
	            block.setVersionNumber(version.getVersionNumber());
	            block.setUsesLatest(true);
			
			    return block;
		}else{
			throw new Exception("cannot find latest version of block");
		}
		
	}
	public IWorkflow createWorkflow(String name, DefaultDrawingModel drawing)
			throws Exception {
		IWorkflow wf=(IWorkflow) api.createObject(IWorkflow.XML_NAME);
		wf.setName(name);
		IUser u=api.getUserContext();
		IFolder home=api.getUserFolder(u);
		
		wf=(IWorkflow)api.saveDocument(home,wf);
		
		XmlDataStore wfData = drawing.storeObject();
	    XmlDataStoreStreamWriter writer = new XmlDataStoreStreamWriter(wfData);
	    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	    writer.writeToOutputStream(outStream);
	    ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
	    IDocumentVersion v = api.upload(wf, inStream);
		return wf;
	}

}
