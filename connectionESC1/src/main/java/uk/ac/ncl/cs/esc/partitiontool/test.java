package uk.ac.ncl.cs.esc.partitiontool;

import java.util.ArrayList;
import java.util.HashMap;

public class test {
	
	
	public static void main(String args[]) throws Exception{
		
		HashMap<String,ArrayList<String>> blockInfo=new HashMap<String,ArrayList<String>>();
		/*first element is "Location",second is Clearance*/
		ArrayList security= new ArrayList();
		security.add("1");
		security.add("1");
		security.add("Service");
		security.add("60");
		ArrayList security1= new ArrayList();
		security1.add("0");
		security1.add("1");
		security1.add("Service");
		security1.add("100");
		ArrayList security2= new ArrayList();
		security2.add("0");
		security2.add("1");
		security2.add("Service");
		security2.add("500");
		ArrayList security3= new ArrayList();
		security3.add("0");
		security3.add("1");
		security3.add("Service");
		security3.add("40");
		ArrayList security4= new ArrayList();
		security4.add("1");
		security4.add("1");
		security4.add("Service");
		security4.add("10");
		ArrayList security5= new ArrayList();
		security5.add("1");
		security5.add("1");
		security5.add("Service");
		security5.add("14");
		
		blockInfo.put("DD1AB8AC-A979-4558-E8FA-A1B8CE1D068E", security);
		blockInfo.put("13B5068C-C079-300F-E3FD-66369BBB042A", security1);
		blockInfo.put("F0B42B83-0E7C-B30C-B16C-2F99B7CCC75A", security2);
		blockInfo.put("25A0BFDC-B058-4526-FFFE-2FEE8F4220D0", security3);
		blockInfo.put("A87B0403-81FB-7F4C-F720-1F2DA0D4D36E", security4);
		blockInfo.put("C1F46947-62E8-20AF-F4E9-F030339C1CD3", security5);
		String workflowId="8ac2c2303c3904e5013c3fe284750241";
		ArrayList<ArrayList<String>> connections=new ArrayList<ArrayList<String>>();
	
			ArrayList<String> temp1=new ArrayList<String>();
			    temp1.add("13B5068C-C079-300F-E3FD-66369BBB042A");
				temp1.add("F0B42B83-0E7C-B30C-B16C-2F99B7CCC75A");
				temp1.add("CSVLoad");
				temp1.add("Add #");
				temp1.add("imported-data");
				temp1.add("input-data");
				temp1.add("1");
				temp1.add("Data");
				temp1.add("10");
				temp1.add("12");
				ArrayList<String> temp2=new ArrayList<String>();
				temp2.add("F0B42B83-0E7C-B30C-B16C-2F99B7CCC75A");
				temp2.add("A87B0403-81FB-7F4C-F720-1F2DA0D4D36E");
				temp2.add("Add #");
				temp2.add("Subsample");
				temp2.add("output-data");
				temp2.add("input-data");
				temp2.add("0");
				temp2.add("Data");
				temp2.add("5");
				temp2.add("12");
		     	ArrayList<String> temp3=new ArrayList<String>();
		    	temp3.add("A87B0403-81FB-7F4C-F720-1F2DA0D4D36E");
				temp3.add("25A0BFDC-B058-4526-FFFE-2FEE8F4220D0");
				temp3.add("Subsample");
				temp3.add("Sort");
				temp3.add("subsampled-data");
				temp3.add("input-data");
				temp3.add("1");
				temp3.add("Data");
				temp3.add("20");
				temp3.add("0");
				ArrayList<String> temp4=new ArrayList<String>();
				temp4.add("A87B0403-81FB-7F4C-F720-1F2DA0D4D36E");
				temp4.add("DD1AB8AC-A979-4558-E8FA-A1B8CE1D068E");
				temp4.add("Subsample");
				temp4.add("CSVExport");
				temp4.add("remaining-data");
				temp4.add("input-data");
				temp4.add("1");
				temp4.add("Data");
				temp4.add("20");
				temp4.add("12");
				ArrayList<String> temp5=new ArrayList<String>();
				temp5.add("25A0BFDC-B058-4526-FFFE-2FEE8F4220D0"); 
				temp5.add("C1F46947-62E8-20AF-F4E9-F030339C1CD3");
				temp5.add("Sort");
				temp5.add("CSVExport");
				temp5.add("sorted-data");
				temp5.add("input-data");
				temp5.add("1");
				temp5.add("Data");
				temp5.add("30");
				temp5.add("10");
			connections.add(temp1);
			connections.add(temp2);
			connections.add(temp3);
			connections.add(temp4);
			connections.add(temp5);
	     new readInfo(blockInfo,workflowId,connections);
	}
}
