/*
 * CallSender.java
 */
package com.connexience.server.workflow.api.rpc;

import com.connexience.server.workflow.rpc.*;
import com.connexience.server.api.*;
import org.apache.log4j.*;

/**
 * This class manages the process of sending a call to the server. If there are
 * any IOExceptions, it retries a specified number of times
 * @author hugo
 */
public class CallSender {
    private static Logger logger = Logger.getLogger(CallSender.class);
    
    RPCClient client;
    CallObject call;
    int retryCount = 20;
    int initialRetryInterval = 1000;
    int maxWaitInterval = 10000;
    double retryMultiplier = 2;
    
    public CallSender(RPCClient client, CallObject call) {
        this.call = call;
        this.client = client;
    }
    
    public void setResendProperties(int retryCount, int initialRetryInterval, int maxWaitInterval, double retryMultiplier){
        this.retryCount = retryCount;
        this.initialRetryInterval = initialRetryInterval;
        this.maxWaitInterval = maxWaitInterval;
        this.retryMultiplier = retryMultiplier;
    }
    
    public void setup(RPCClientApi source){
        retryCount = source.getRetryCount();
        initialRetryInterval = source.getInitialRetryInterval();
        maxWaitInterval = source.getMaxWaitInterval();
        retryMultiplier = source.getRetryMultiplier();
    }
    
    public boolean syncCall() throws RPCException, APIConnectException {
        int count = 0;
        double waitTime = initialRetryInterval;
        for(count = 1;count<=retryCount;count++){
            if(!attemptSend()){
                call.resetToResend();
                try {
                    Thread.sleep((int)waitTime);
                }  catch (Exception e){}
                waitTime = waitTime * retryMultiplier;
                if(waitTime>=maxWaitInterval){
                    waitTime = maxWaitInterval;
                }
            } else {
                return true;
            }
        }
        return false;
    }
    
    private boolean attemptSend() throws RPCException, APIConnectException {
        try {
            client.syncCall(call);
        } catch (RPCException e){
            if(call.isIoException()){
                // This resulted in an IO Exception, Ok to retry
                logger.info("IO Exception sending call to: " + call.getMethodName());
                return false;
            } else {
                throw e;
            }
        }
        
        if(call.getStatus()!=CallObject.CALL_EXECUTED_OK){
            if(call.isIoException()){
                // Can retry IO exceptions
                logger.info("IO Error returned in call to: " + call.getMethodName());
                return false;
                
            } else if(call.getStatus()==CallObject.CALL_TIMEOUT){
                // Can retry timeouts
                logger.info("Timeout returned in call to: " + call.getMethodName());
                return false;
                
            } else {
                throw new APIConnectException("Error executing call: " + call.getStatusMessage());
            }
        } else {
            return true;
        }
    }
}
