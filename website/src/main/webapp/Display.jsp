<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Update Machines Statue</title>
<script type="text/javascript" src="js/jquery-2.0.3.js"></script>
<link href="bootstrap/css/bootstrap.css" type="text/css" rel="stylesheet" />
<script src="js/Format.js"> </script>
</head>

<body>
<h1 style="text-align:center">The Machines Statues<br></h1> 

 <script>
 
  $(document).ready(function() {
		//Get the Data
		//Set a timeout to call getData again every two seconds 1
		
	 	setInterval(function(){
			getData();
		},1000);  
		
	//  getData();

	}); 
 
 </script>
  

<div style="width:700px;padding:20px;S">
        <h5 style="text-align:center"><i style="color:#ccc"><small>Machines</small></i></h5>
 
        <table id="added-machines" class="table">
            <tr>
                <th>IPAddress</th>
                <th>Statue</th>
                <th>SecurityLevel</th>
                <th>Switch</th>
            </tr>
        </table>
    </div>


</body>
</html>