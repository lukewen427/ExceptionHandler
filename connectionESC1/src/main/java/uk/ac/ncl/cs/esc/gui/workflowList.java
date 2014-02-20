package uk.ac.ncl.cs.esc.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class workflowList extends JPanel implements ActionListener {
	HashMap<String, String> workflows;

	public workflowList(){
		this.workflows=getWorkflows();
		Set<String> keys=workflows.keySet();
		Iterator<String> keySet=keys.iterator();
		int num=keys.size();
		int a=0;
		String []elements= new String[num];
		while(keySet.hasNext()){
			String workflowName=workflows.get(keySet.next());
			elements[a]=workflowName;
			a++;
		}
		
		JComboBox list=new JComboBox(elements);
		list.setSelectedIndex(keys.size()-1);
		 list.addActionListener(this);
		 
		 
		add(list, BorderLayout.CENTER);
		 setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
	}
	public HashMap<String,String> getWorkflows(){
		getWorkflowInfo workflows=new getWorkflowInfo();
		HashMap<String, String> workflowlist = null;
		try {
			workflowlist = workflows.getWorkflows();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return workflowlist;
	}
	public void actionPerformed(ActionEvent e) {
		JComboBox cd=(JComboBox)e.getSource();
		
		String workflowName=(String)cd.getSelectedItem();
		String workflowId=null;
		while(workflows.keySet().iterator().hasNext()){
			workflowId=workflows.keySet().iterator().next();
			String value=workflows.get(workflowId);
			if(workflowName.equals(value)){
				break;
			}
		}
		
		new assignSecurityLevels(workflowId,workflowName);
		
	}
	  private static void createAndShowGUI() {
	        //Create and set up the window.
	        JFrame frame = new JFrame("WorkflowList");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 
	        //Create and set up the content pane.
	        JComponent newContentPane = new workflowList();
	        newContentPane.setOpaque(true); //content panes must be opaque
	        frame.setContentPane(newContentPane);
	 
	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
	    }
	  public static void main(String[] args) {
	        //Schedule a job for the event-dispatching thread:
	        //creating and showing this application's GUI.
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                createAndShowGUI();
	            }
	        });
	    }
	
}
