package uk.ac.ncl.cs.esc.deployment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class getClouds{

	public HashMap<String,String>  getHttp (String url) throws IOException{
		HashMap<String,String> machines=new HashMap<String,String>();
		try {
			BufferedReader br= new BufferedReader(new InputStreamReader(
					new FileInputStream(url)));
			String data;
	
			while((data = br.readLine())!=null){
				String replace=data.replaceAll(",", "");
				
			String[] machineArr=replace.split(" ");
				
				for(int i=1;i<machineArr.length;i=i+4){
					String name=machineArr[i];
					String statue=machineArr[i+1];
					String[] getName=name.split("=");
					String ip=getName[1];
					String [] getStatues=statue.split("=");
					String theStatue=getStatues[1];
					machines.put(ip, theStatue);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return machines;
	}
	
	/*public static void main(String[] arg){
		String url="/Users/zhenyuwen/git/ExceptionHandler/website/statues.txt";
		getClouds test=new getClouds();
		try {
			System.out.println(test.getHttp(url));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	  }

