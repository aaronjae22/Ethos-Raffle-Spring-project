function initializeEvents(basePath)
{
	
	$("#table-container").on('click','#edit-user-button',function(){
		showEditor();
		
		var idClient = $(this).parents(".user-row").find("td.id-column").attr("data-id"); 
		
		loadForm(idClient);		
	});
	
	$("#cancel-button").on('click',function(){
		cancelForm();
	});
	
	$("#add-user-button").on('click',function()
			{
				cancelForm();
				
				showEditor();
			});
	
	$("#reset-password-button").on('click',function(){
		
		swal({
	        title: "Are you sure?",
	        text: "This action will reset this user password (Password will be the same as the User Name),!",
	        type: "warning",
	        showCancelButton: true,
	        confirmButtonColor: "#DD6B55",
	        confirmButtonText: "Yes, change it!",
	        closeOnConfirm: false
	    }, function () {
	    	
	    	var idUser = $("#user-form").attr("data-id") ;
	    	
	        resetPassword(idUser);
	    });

		
	});
	
	function resetPassword(idUser)
	{		
		
		$.ajax({
			 type: "POST",
			 url: basePath + "user/do-reset-password/"+ idUser,
			 //data: $('#change-password-form').serialize(),
			 contentType: "application/x-www-form-urlencoded",
			 dataType: "json"			 
			}).done(function(data){
								
				//success
				if(data.success){
					//toastr['success']("Password changed Successfully!");
					swal("Done!", "The password has been changed!", "success");
					}
				else
				{
					toastr['error'](data.message);
				}
			}).fail(function(data){
				
				toastr['error']("Error saveing user information, unexpected error has occurred (Server Error)!");
				
			});
		
	}
	
	
	function showEditor()
	{
		$(".cd-panel-right").toggleClass("is-visible");	
		
	}
	
	
	function cancelForm()
	{
		
		$("#full-name-input, #username-input").val('');		
		$("#regular-user").prop('checked', true);
			
		$("#user-form").attr("data-id",0);		
		$("#active-check").prop	('checked',true);
	}
	
	
	function getUserInformation()
	{
		var user = {};
		user.idUser = $("#user-form").attr("data-id");
		user.fullName = $("#full-name-input").val();
		user.userName = $("#username-input").val();
		
		var isAdministrator = $("#administrator-user").is(":checked");		
		user.administrator = isAdministrator;
		
		var isChecked = $("#active-check").is(":checked")
		
		user.active = isChecked;
		
		return user;
	}
	
	function loadForm(idUser)
	{
		//var json = JSON.stringify({idUser : idUser});
		
		
		$.ajax({
			 type: "POST",
			 url: basePath + "/user/load-user?idUser="+idUser,
			 //data: json,
			 contentType: "application/json; charset=utf-8",
			 dataType: "json"			 
			}).done(function(data){
								
				loadFormInputs(data);
				
			}).fail(function(data){
				
				toastr['error']("Error loading user information, unexpected error has occurred (Server Error)!");
				
			});
	}
	
	function loadFormInputs(user)
	{
		$("#user-form").attr('data-id',user.idUser);
		$("#full-name-input").val(user.fullName);
		$("#username-input").val(user.userName);
		
		if(user.administrator)
		{
			$("#administrator-user").prop("checked",true);
		}
		else
		{
			$("#regular-user").prop("checked",true);
		}
		
		
		if(user.active)
		{
			$("#active-check").prop("checked",true);
		}
		else			
		{
			$("#active-check").prop("checked",false);
		}
		
		
		
	}
	saveUserGeneral  = saveUser;
	function saveUser()
	{
		
		var user = getUserInformation();
		
		var userData = JSON.stringify(user);
		
		var path = basePath + "user/administration/save-user";
		$.ajax({
			 type: "POST",
			 url: path,
			 data: userData,
			 contentType: "application/json; charset=utf-8",
			 dataType: "json"			 
			}).done(function(data){
				
				if(data.success)
				{
					toastr['success']("User saved successfully!");
					
					reloadTable();
					cancelForm();
					
					showEditor();
					
				}
				else
				{
					toastr['error']("Error saving user information! <br />" + data.message);
				}
				
				
			}).fail(function(data){
				
				toastr['error']("Error saving user information, unexpected error has occurred (Server Error)!");
				
			});
	}
	
	function reloadTable()
	{
		var path = basePath + "user/administration/table";
		
		$.ajax({
			 type: "GET",
			 url: path,
		//	 data: userData,
			 contentType: "application/json; charset=utf-8",
			 //dataType: "json"			 
			}).done(function(response){
				
				
				$("#users-table").html(response);
				
				
			}).fail(function(data){
				
				toastr['error']("Error loading user information, unexpected error has occurred (Server Error)!");
				
			});
	}		
	
	ljcmLoadData = getUserInformation;
	
}






