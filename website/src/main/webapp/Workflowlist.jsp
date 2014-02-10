<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"  import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>List Workflows</title>
</head>
<body>
<h2>List the Workflows</h2>
<form method="GET" action="getWorkflowInfo">

<table border="1">
<tr>
<th>Select</th>
<th>Name</th>
<th>WorkflowId</th>
</tr>

<% 
HashMap<String,String>workflowlist=(HashMap<String,String>)session.getAttribute("workflows");
Set<String> keyset=workflowlist.keySet();
Iterator<String> iter=keyset.iterator();
while(iter.hasNext()){
String id=(String)iter.next();
%>

<tr>
<td><input type="checkbox" name="selectedworkflow" value="<% out.print(id); %>" />&nbsp;</td>
<td ><input type="text" name="workflowid" value=" <% out.print(id); %>" ></td>
<td > <input type="text" name="workflowname" value=" <% out.print(workflowlist.get(id)); %>"> </td>
</tr>
<% } %>
</table>
 <p>
   <strong> submit a selected workflow:</strong>
   <input type="submit" name="submitSecurityLevel" value="submit">
 </p>
</form>
</body>
</html>