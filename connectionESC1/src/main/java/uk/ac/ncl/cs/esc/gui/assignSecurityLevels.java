package uk.ac.ncl.cs.esc.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import uk.ac.ncl.cs.esc.workflow.read.readWorkflow;
import uk.ac.ncl.cs.esc.workflow.restructure.WorkflowRes;
import uk.ac.ncl.cs.esc.workflow.restructure.WorkflowRestructure;

public class assignSecurityLevels extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String workflowId;
	String workflowName;
	HashMap<String,String> blocks;
	ArrayList<ArrayList<String>> links;
	protected JButton b1;
	MyTableModel model;
	MyTableModel2 model2;
	public assignSecurityLevels(String workflowId,String workflowName) {

		this.workflowId=workflowId;
		this.workflowName=workflowName;
		try {
			this.blocks=getBlocks(workflowId);
			this.links=getConnection(workflowId); 
			System.out.println(blocks);
			System.out.println(links);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 //Create and set up the window.
        JFrame frame = new JFrame("Security Levels");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        	 model=new MyTableModel();
		 JTable table = new JTable(model);
		 
	        table.setPreferredScrollableViewportSize(new Dimension(500, 100));
	        table.setFillsViewportHeight(true);
	        
	        model2=new MyTableModel2();
	     JTable table2=new JTable(model2);   
	     table2.setPreferredScrollableViewportSize(new Dimension(500, 100));
	        table2.setFillsViewportHeight(true);
	      //Create the scroll pane and add the table to it.
	        JScrollPane scrollPane = new JScrollPane(table);
	        JScrollPane scrollPane2 = new JScrollPane(table2);
	        // create a button for submition
	        b1=new JButton("Submit");
	        b1.addActionListener(this);
	        b1.setLocation(0, 150);
	        
	        //Add the scroll pane to this panel.
	        add(scrollPane,BorderLayout.NORTH);
	        add(scrollPane2,BorderLayout.CENTER);
	        add(b1,BorderLayout.SOUTH);
	        this.setOpaque(true); //content panes must be opaque
	     
	        frame.setContentPane(this);

	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		ArrayList<ArrayList<String>> connections=new ArrayList<ArrayList<String>>();
		HashMap<String,ArrayList<String>> blockInfo=new HashMap<String,ArrayList<String>>();
		connections=getConnections();
		blockInfo=getBlockInfo();
	//	System.out.println(connections);
//		System.out.println(blockInfo);
		new readWorkflow(workflowId,connections,blockInfo);
    }
	
	private HashMap<String,ArrayList<String>> getBlockInfo(){
		HashMap<String,ArrayList<String>> theBlocks=new HashMap<String,ArrayList<String>>();
		ArrayList<String> block=new ArrayList<String>();
		int size=0;
		Object[][] data=model.returnBlocks();
		size=data.length;
		for(int a=0;a<size;a++){
			ArrayList<String> temp=new ArrayList<String>();
			String Id=(String) data[a][1];
			String location=String.valueOf(data[a][2]);
			String clearance=String.valueOf(data[a][3]);
			String service="Service";
			String CPU=String.valueOf(data[a][4]);
			temp.add(location);
			temp.add(clearance);
			temp.add(service);
			temp.add(CPU);
			block=(ArrayList<String>) temp.clone();
			theBlocks.put(Id, block);
		}
		return theBlocks;
		
	}
	private ArrayList<ArrayList<String>> getConnections(){
		 Object[][] data=model2.returnConnection();
		 ArrayList<ArrayList<String>> connections=new ArrayList<ArrayList<String>>();	
		 ArrayList<String> thelink=new ArrayList<String>();
		 for(int a=0;a<data.length;a++){
			 ArrayList<String> temp=new ArrayList<String>();
			 ArrayList<String>connection=links.get(a);
			 String sourceId=connection.get(0);
			 String destinationId=connection.get(1);
			 String sourceName=connection.get(2);
			 String destinationName=connection.get(3);
			 String sourcePort=connection.get(4);
			 String destinationPort=connection.get(5);
			 String location=String.valueOf(data[a][4]);
			 String type="Data";
			 String Longevity=String.valueOf(data[a][5]);
			 String Size=String.valueOf(data[a][6]);		
			 temp.add(sourceId);
			 temp.add(destinationId);
			 temp.add(sourceName);
			 temp.add(destinationName);
			 temp.add(sourcePort);
			 temp.add(destinationPort);
			 temp.add(location);
			 temp.add(type);
			 temp.add(Longevity);
			 temp.add(Size);
			 thelink=(ArrayList<String>) temp.clone();
			 connections.add(thelink);
		 }
		return connections;
		
	}
	class MyTableModel2 extends AbstractTableModel{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String [] columnNames={"sourceBlock","destinationBlock","sourceBlockPortName","detinationPortName","location","Longevity","Size"};
		private Object[][] data= getConnection();
		
		public int getRowCount() {
			
			return data.length;
		}

		public int getColumnCount() {
			
			return columnNames.length;
		}
		
		 public String getColumnName(int col) {
	            return columnNames[col];
	        }

		public Object getValueAt(int rowIndex, int columnIndex) {
				
			return data [rowIndex][columnIndex];
		 }
		
		public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 2) {
                return false;
            } else {
                return true;
            }
        }
		
		public void setValueAt(Object value, int row, int col) {
			 data[row][col] = value;
	         fireTableCellUpdated(row, col);
		}
		public Object[][] returnConnection(){
			return data;
		}
		
	}
	
	public Object[][] getConnection(){
		int columSize=links.size();
		Object connection[][]=new Object[columSize][7];
		for(int a=0;a<links.size();a++){
			ArrayList<String> link=links.get(a);
			connection[a][0]=link.get(2);
			connection[a][1]=link.get(3);
			connection[a][2]=link.get(4);
			connection[a][3]=link.get(5);
			connection[a][4]=new Integer(0);
			connection[a][5]=new Integer(0);
			connection[a][6]=new Integer(0);
		}
		return connection;
	}
	class MyTableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private String [] columnNames={"BlockName","BlockId","location","clearance","CPU"};
		
		private Object[][] data=getData(); 
		

		public int getRowCount() {
			
			return data.length;
		}

		public int getColumnCount() {
			
			return columnNames.length;
		}
		
		 public String getColumnName(int col) {
	            return columnNames[col];
	        }

		public Object getValueAt(int rowIndex, int columnIndex) {
				
			return data [rowIndex][columnIndex];
		 }
		
		public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 2) {
                return false;
            } else {
                return true;
            }
        }
		
		public void setValueAt(Object value, int row, int col) {
			 data[row][col] = value;
	         fireTableCellUpdated(row, col);
		}
		
		public Object[][] returnBlocks(){
			return data;
		}
		
		}
	
	
	public Object[][] getData(){
		int columSize=blocks.size();
		Object data[][]=new Object[columSize][5];
		Iterator<String> keys=blocks.keySet().iterator();
		int a=0;
		while(keys.hasNext()){
			String id=keys.next();
			String name=blocks.get(id);
			data[a][0]=name;
			data[a][1]=id;
			data[a][2]=new Integer(0);
			data[a][3]=new Integer(0);
			data[a][4]=new Integer(0);
			a++;
		}
		
		   
		return data;
	}
	
	/*get the blocks of the selected workflow*/
	 public HashMap<String,String> getBlocks(String workflowid) throws Exception{
			
			WorkflowRestructure blocklist=new WorkflowRes();
			HashMap<String,String> blocks=new HashMap<String,String>();
			blocks=blocklist.Blocklist(workflowid);
		    
		    return blocks;
		} 
	 
	 /*get links between two blocks*/
	 
	 public ArrayList<ArrayList<String>> getConnection (String workflowid) throws Exception{
		 ArrayList<ArrayList<String>> connection=new ArrayList<ArrayList<String>>();
		 WorkflowRestructure getconnection=new WorkflowRes();
		 connection=getconnection.getConnection(workflowid);
		return connection; 
	 }
}
