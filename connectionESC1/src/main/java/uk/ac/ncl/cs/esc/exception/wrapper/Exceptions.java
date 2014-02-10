package uk.ac.ncl.cs.esc.exception.wrapper;

import java.util.Scanner;

public class Exceptions {
	public String ListExceptions(){
		System.out.println("Choose Excetpion type");
		System.out.println("Case 1: The Destination Cloud is not Ready");
		System.out.println("Case 2: Data lost during transmission");
		System.out.println("Case 3: The Running Cloud is failed");
		System.out.println("Case 4: No Exception Detected");	
		Scanner s=new Scanner(System.in);
		int option=s.nextInt();
		switch(option){
		case 1:{
			return "Case 1";
		}
		case 2:{
			
			return "Case 2";
		}
		case 3:{
			return "Case 3";
			
		}
		case 4:{
			
			return "Case 4";
		 }
		}
		return null;
	}
}
