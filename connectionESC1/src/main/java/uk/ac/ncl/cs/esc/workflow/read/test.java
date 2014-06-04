package uk.ac.ncl.cs.esc.workflow.read;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

public static void main(String[] args){
	System.out.println(getNumber("cloud10"));
}

public static int getNumber(String name){
	int number = 0;
	Pattern pattern = Pattern.compile("\\d+"); 
	Matcher matcher = pattern.matcher(name);  
	while(matcher.find()){
		number=Integer.valueOf(matcher.group(0));
	}
	return number;
}
}
