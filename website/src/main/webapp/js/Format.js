/**
 * 
 */
//On Page load


/*function reload(){
	
	var machines =new Object();
	machines.ip=$('#ip').val();
	machines.statue=$('#statue').val();
	machines.security=$('#security').val();
	
$.ajax({
	async: true,
	url:"JSONServlet",
	type:'POST',
	dataType:'json',
	data: JSON.stringify(machines),
	contentType: 'application/json',
    mimeType: 'application/json',
    
    success: function (data){
    
    	$('#added-machines').html(html);
    	$.each(data,function(index,machines){
    		
    		var button = $("<button onclick="+"\""+"useMachine("+index+")"+"\""+">"+"Switch</button>");
    	
    		  $("#added-machines").append($('<tr/>')
                      .append($('<td/>').append(machines.ip))
                      .append($('<td/>').append(machines.statue))
                      .append($('<td/>').append(machines.security))
                      .append($('<td/>').append(button)
                    	
                      )
                    	
              );
    		
    	});
    	
    },
    
    error:function(data,status,er) {
        alert("error: "+data+" status: "+status+" er:"+er);
    }
});



} */

/*function useMachine(number){
	
	var machines =new Object();
	machines.ip=$('#ip').val();
	machines.statue=$('#statue').val();
	machines.security=$('#security').val();
	
	$.ajax({
		
		url:"JSONServlet",
		type:'POST',
		dataType:'json',
		data: JSON.stringify(machines),
		contentType: 'application/json',
	    mimeType: 'application/json',
	    
	    success: function (data){
	    
	    	$.each(data,function(index,machines){
	    		
	    		if(index==number){
	    			var jsonText=machines;
	    		//	alert(jsonText);
	    			sendData(jsonText);
	    			return false;
	    		}
	    	
	    	});
	    
	    },
	    
	});

}


 function sendData(jsonText){
	 $.post('background',jsonText);
 }*/
 
function getData(){
	
	var machines =new Object();
	machines.ip=$('#ip').val();
	machines.statue=$('#statue').val();
	machines.security=$('#security').val();
	
	$.ajax({
		
		url:"JSONServlet",
		type:'POST',
		dataType:'json',
		data: JSON.stringify(machines),
		contentType: 'application/json',
	    mimeType: 'application/json',
	    
	    success: function (data){
	    
	    	if(data!="{}"){
	    		
	    		$('#added-machines').html("");
	    		  $("#added-machines").append($('<tr/>')
                          .append($('<td/>').append("IPAddress"))
                          .append($('<td/>').append("Statue"))
                          .append($('<td/>').append("SecurityLevel"))
                        	
                  );
	        	$.each(data,function(index,machines){
	        		
	        	/*	var button = $("<button onclick="+"\""+"useMachine("+index+")"+"\""+">"+"Switch</button>");*/
	        	
	        		  $("#added-machines").append($('<tr/>')
	                          .append($('<td/>').append(machines.ip))
	                          .append($('<td/>').append(machines.statue))
	                          .append($('<td/>').append(machines.security))
	                        	
	                  );
	        		
	        	});
	    		
	    		//reload();
	    	}else{
	    		
	    		
	    	}
	    
	    }
	   
	    
	});
	
	//Do an ajax GET request
	
		//If successful 
			//get data variable (which will be the JSON array of machines)
			//assign it to the global variable window.machines
			//Call function updateForm which will assign values from window.machines to an HTML element
	
	
		//Else if unsuccessful show error
	

	
}

//updateForm function

//window.machines is an array, when creating HTML buttons with javascript, make sure to pass the array index of each machine to its button.