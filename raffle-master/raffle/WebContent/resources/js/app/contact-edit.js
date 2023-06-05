


function initializeEditContactForm(basePath, callback)
{
	$("body").on( 'click', '.edit-contact-button', function(){
		
		var id = $(this).attr("data-id-contact");		
		showContactForm(id, callback );		
	});
}


function showContactForm(idContact, callback )
{
	
	BootstrapDialog.show({
		 title: '&nbsp;',
      message: $('<div></div>').load( basePath +  '/contact/edit-contact/?idContact=' + idContact),
      buttons: [
			{
			    label: 'Update Contact',
			    cssClass: 'btn-primary',
			    action: function(dialog) {

			    	var form = $(".edit-contact-form");

			    	var data = form.serialize();
			    	
			    	var zipCode = $(this).val();
					
					$.ajax({
						 type: "POST",
						 url: basePath + "/contact/update-contact",
						 data: data,
						 //contentType: "application/json; charset=utf-8",
						 dataType: "json"			 
						}).done(function(data){
							
							if(data.success)
							{
								toastr['success']("Contact updated successfully!")
								if(callback)
									callback(data);
								dialog.close();
							}
							else
							{
								toastr['error'](data.message);
							}
						}).fail(function(data){
							
							toastr['error']("Contact not updated! (Server Error)!");
							
						});
			    }
			},
          {
              label: 'Close',
              action: function(dialog) {
              dialog.close();
              }
          }]
  });
	
	
}


function loadZipCodeInformation(zipCode, callback)
{
	var zipCode = $(this).val();
	
	$.ajax({
		 type: "POST",
		 url: basePath + "contact/get-zip-info?zipCode="+zipCode,
		 data: {},
		 contentType: "application/json; charset=utf-8",
		 dataType: "json"			 
		}).done(function(data){
						
			if(data.error == null)
			{
				if(callback)
				{					
					/*
					//add data to city and state
					$("#city").val(data.city);
					$("#state").val(data.state);
					*/
					callback(data,true);
				}								
			}
			else
			{
				if(callback)
				{
					callback(data,false);
				}
				toastr['error'](data.error);
			}
		}).fail(function(data){
			
			$("#city").val("");
			$("#state").val("");
			toastr['error']("Zip code info not loaded! (Server Error)!");
			
		});
	
}



