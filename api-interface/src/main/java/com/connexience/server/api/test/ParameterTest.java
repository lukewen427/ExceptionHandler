/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.connexience.server.api.test;

import com.connexience.server.api.impl.InkspotWorkflowParameterList;

/**
 *
 * @author hugo
 */
public class ParameterTest {
    public static void main(String[] args){
        InkspotWorkflowParameterList params = new InkspotWorkflowParameterList();
        params.add("trainer.Neurons", "4");
        System.out.println("Block: " + params.getParameter(0).getBlockName());
        System.out.println("Param: " + params.getParameter(0).getName());
        System.out.println("Value: " + params.getParameter(0).getValue());
    }
}
