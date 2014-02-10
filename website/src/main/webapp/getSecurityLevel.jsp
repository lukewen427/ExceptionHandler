<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"  import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Security Level Input</title>
</head>
<body>
<h2>The Services of the Selected Workflow </h2>
<form method="POST" action="securityLevel">
<table border="1">
<tr>
<th>BlockName</th>
<th>Id</th>
<th>Location</th>
<th>Clearance</th>
<th>Type</th>
<th>CPU</th>
</tr>
<%
HashMap<String,String> blocks=(HashMap<String,String>)session.getAttribute("blocks");
Set<String> keyset=blocks.keySet();
Iterator<String> iter=keyset.iterator();
while(iter.hasNext()){
String id=(String)iter.next();
%>

<tr>
<td ><input type="text" name="blockname" value=" <% out.print(blocks.get(id)); %>" ></td>
<td > <input type="text" name="blockId" value=" <% out.print(id); %>"> </td>

<td><select name="location">
<option value ="low">0</option>
<option value ="high">1</option>
</select></td>

<td><select name="clearance">
<option value ="low">0</option>
<option value ="high">1</option>
</select></td>

<td ><input type="text" name="theType" value="Service"></td>


<td > <input type="text" name="CPU" value=""> </td>

</tr>
<%} %>
</table>
<h2>The Data of the Selected Workflow</h2>
<table border="1">
<tr>
<th>sourceBlock</th>
<th>destinationBlock</th>
<th>sourceBlockPortName</th>
<th>detinationPortName</th>
<th>Location</th>
<th>Type</th>
<th>Size</th>
<th>Longevity</th>
</tr>

<%
ArrayList<ArrayList<String>> connections=(ArrayList<ArrayList<String>>) session.getAttribute("connections");
for(int a=0;a<connections.size();a++){
	ArrayList<String> connection=connections.get(a);
%>
<tr>

<td ><input type="text" name="sourceBlock" value=" <% out.print(connection.get(2)); %>" ></td>
<td ><input type="text" name="destinationBlock" value=" <% out.print(connection.get(3)); %>" ></td>
<td ><input type="text" name="sourceBlockPortName" value=" <% out.print(connection.get(4)); %>" ></td>
<td ><input type="text" name="detinationPortName" value=" <% out.print(connection.get(5)); %>" ></td>

<td><select name="datalocation">
<option value ="low">0</option>
<option value ="high">1</option>
</select></td>

<td ><input type="text" name="Type" value="Data"></td>
<td ><input type="text" name="Size" value=""></td>
<td > <input type="text" name="Longevity" value=""> </td>
<p><input type="hidden" name ="sourceBlockId" value=<% out.print(connection.get(0)); %>></p>
<p><input type="hidden" name ="destinationBlockId" value=<% out.print(connection.get(1)); %>></p>
</tr>
<%}%>
</table>

<% String workflowid=(String)session.getAttribute("workflowId"); %>
<p><input type="hidden" name ="workflowId" value=<% out.print(workflowid); %>></p>
 <p>
   <strong> Submit The Security Information of Each Block:</strong>
   <input type="submit" name="submitsecuritylevel" value="submit">
 </p>
</form>
</body>
</html>