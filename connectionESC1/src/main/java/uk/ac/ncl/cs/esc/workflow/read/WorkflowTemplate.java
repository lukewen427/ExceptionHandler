package uk.ac.ncl.cs.esc.workflow.read;
public interface WorkflowTemplate {



    public double[][] getWorkflow();

    public int[][] getDataSecurity();

    public double[][] getCcost();

    public double[][] getCpucost();

    public int[] getCloud();

    public int[][] getSsecurity();


}