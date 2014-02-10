<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"  import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
   <meta http-equiv="Pragma" content="no-cache">
<title>Insert title here</title>
</head>

<form method ="get"action="background">	
<body>
	<h4>THE AVAILABLE MACHINES</h4>
	<table border="1">
	<tr>
	<th>IPAddress</th>
	<th>Statue</th>
	<th>SecurityLevel</th>
	<th>Selection</th>
	</tr>
	
	<%
		Set<String> machines=(Set<String>)session.getAttribute("Machine");
		ArrayList<String> service=(ArrayList<String>)session.getAttribute("Service");
	if(machines==null){
		
	}else{
		Iterator<String>iter=machines.iterator();
		while(iter.hasNext()){
	 	String ip=iter.next();
		String isWorking;
		if(service.contains(ip)){
			isWorking="ON";
		}else{
			isWorking="OFF";
		}
	%>
	<tr>
	<td ><input type="text" name="machineip" value="<%out.print(ip); %>" ></td>
 	 <td><input type="text" name="statue" value= "<%out.print(isWorking);%>"></td>
  	<td>1</td>
  	<td> <input type="submit" name="Switch" value="Switch"></td>
    </tr>
    
 <% }
		} %>
	</table>
	</form>
<form method ="post"action="display">	
	 <p>
        <strong>Refresh:</strong>
        <input type="submit" name="Refresh" value="Refresh">
      </p>
	</form>
	
</body>
</html>