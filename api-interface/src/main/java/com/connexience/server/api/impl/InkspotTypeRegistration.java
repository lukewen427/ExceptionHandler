/*
 * InkspotTypeRegistration.java
 */

package com.connexience.server.api.impl;

import com.connexience.server.api.*;
import com.connexience.server.api.util.*;

/**
 * This class registers all of the standard object implementations with the
 * ObjectBuilder object.
 * @author nhgh
 */
public class InkspotTypeRegistration {
    /** Perform registration */
    public static void register(){
        ObjectBuilder.registerObject(IUser.XML_NAME, InkspotUser.class);
        ObjectBuilder.registerObject(IFolder.XML_NAME, InkspotFolder.class);
        ObjectBuilder.registerObject(IDocument.XML_NAME, InskpotDocument.class);
        ObjectBuilder.registerObject(IPermission.XML_NAME, InkspotPermission.class);
        ObjectBuilder.registerObject(IPermissionList.XML_NAME, InkspotPermissionList.class);
        ObjectBuilder.registerObject(IAPIErrorMessage.XML_NAME, InkspotErrorMessage.class);
        ObjectBuilder.registerObject(IDocumentVersion.XML_NAME, InkspotDocumentVersion.class);
        ObjectBuilder.registerObject(IApplicationRegistration.XML_NAME, InkspotApplicationRegistration.class);
        ObjectBuilder.registerObject(IGroup.XML_NAME, InkspotGroup.class);
        ObjectBuilder.registerObject(IDocument.XML_NAME, com.connexience.server.api.impl.InkspotDocument.class);
        ObjectBuilder.registerObject(IPropertyItem.XML_NAME, InkspotPropertyItem.class);
        ObjectBuilder.registerObject(IPropertyList.XML_NAME, InkspotPropertyList.class);
        ObjectBuilder.registerObject(IXmlMetaData.XML_NAME, InkspotXmlMetaData.class);
        ObjectBuilder.registerObject(IAuthenticationRequest.XML_NAME, InkspotAuthenticationRequest.class);
        ObjectBuilder.registerObject(IWorkflow.XML_NAME, InkspotWorkflow.class);
        ObjectBuilder.registerObject(IWorkflowInvocation.XML_NAME, InkspotWorkflowInvocation.class);
        ObjectBuilder.registerObject(ILatLongHolder.XML_NAME, InkspotLatLongHolder.class);
        ObjectBuilder.registerObject(IDynamicWorkflowService.XML_NAME, InkspotDynamicWorkflowService.class);
        ObjectBuilder.registerObject(IDynamicWorkflowLibrary.XML_NAME, InkspotDynamicWorkflowLibrary.class);
        ObjectBuilder.registerObject(ISignatureData.XML_NAME, InkspotSignatureData.class);
        ObjectBuilder.registerObject(IExternalObject.XML_NAME, InkspotExternalObject.class);
        ObjectBuilder.registerObject(ILink.XML_NAME, InkspotLink.class);
        ObjectBuilder.registerObject(IWorkflowParameter.XML_NAME, InkspotWorkflowParameter.class);
        ObjectBuilder.registerObject(IWorkflowParameterList.XML_NAME, InkspotWorkflowParameterList.class);
        ObjectBuilder.registerObject(INewUser.XML_NAME, InkspotNewUser.class);
    }
}