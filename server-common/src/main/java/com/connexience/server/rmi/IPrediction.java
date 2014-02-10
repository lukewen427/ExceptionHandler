package com.connexience.server.rmi;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * User: nsjw7
 * Date: Mar 15, 2011
 * Time: 2:18:55 PM
 * This interface represents how clients can query provenance data
 */
public interface IPrediction extends Remote
{

 
  public Double getPrediction(String blockUUID, int inputSize) throws RemoteException, Exception;

}
