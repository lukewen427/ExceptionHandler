<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C/DTD HTML 4.0 Transitional//EN">
  <html>
  <head>
    <meta http-equiv="Pragma" content="no-cache">
    <title>Security FVT Login Page </title>
  </head>

  <body>
    <h2>Form Login</h2>
    <% if(!("yes".equals(session.getAttribute("retry")))){ %>
    <form method="GET" action="Controller">
      <p>
        <strong>Please enter user ID and password:</strong>
        <br>
        <strong>User ID</strong>
        <input type="text" size="20" name="j_username">
        <strong>Password</strong>
        <input type="password" size="20" name="j_password">
      </p>
       <p>
        <strong>And then click this button:</strong>
        <input type="submit" name="login" value="Login">
      </p>
      </form>
  <%} %>    
      
		<% if("yes".equals(session.getAttribute("retry"))){ %>
		
		 <form method="GET" action="Controller">
		 <p>
        <strong>The user ID or password can not be verified, please try again</strong>
        <br>
        <strong>User ID</strong>
        <input type="text" size="20" name="j_username">
        <strong>Password</strong>
        <input type="password" size="20" name="j_password">
      </p>
      <p>
        <strong>And then click this button:</strong>
        <input type="submit" name="login" value="Login">
      </p>
      
      <%} %>
    </form>

  </body>
  </html>
