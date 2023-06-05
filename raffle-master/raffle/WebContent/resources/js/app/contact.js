
var addNewCall;

function initializeEvents(basePath)
{
		
	$(".clients-list").on('click','.view-calls-button',function(){		
		var idContact = $(this).attr("data-contact-id");		
		loadContactDetails(idContact); 
		
	});
	
	$("body").on('click','.add-call-button',addNewCallHandler);
	
	
	$("#search-button").on('click', function(){
		reloadTable();
	});
	
	$("#firstNameInput, #lastNameInput").change(function(){
			reloadTable();
		});
	
	
	function reloadTable(callback)
	{		
		var firstNameFilter = $("#firstNameInput").val();
		var lastNameFilter 	= $("#lastNameInput").val();
		
		
		var url = basePath + "contact/table-view-list?firstNameFilter="+firstNameFilter+"&lastNameFilter="+lastNameFilter;
		
		$.get( url, function( data ) 
		{		
			
			$("#rows-container").replaceWith(data);		
			
			if(callback)
			{
				setTimeout(function(){					
					callback();
				},500);
			}
			
		});
		
		
		
		
	}
	
	function addNewCallHandler()
	{			
		var selectedContact = getSelectedContact();			
		addNewCall(selectedContact.id, selectedContact.name);	
	}
	
	function getSelectedContact()
	{
		var contactName =  $("#contact-name-label").text();		
		var idContact = $(".add-call-button").attr("data-contact-id");
		
		return {
			name: contactName,
			id : idContact			
		}; 
	}
	
	window.addNewCallHandler = addNewCallHandler;
	
	function loadContactDetails(idContact, callback)
	{
		var url = basePath + 'contact/view-contact-detail?idContact='+ idContact;
		
		$.post( url, function( data ) 
		{
			
			$("#contact-detail-empty").hide();
			
			var container = $("#contact-detail-container");
			
			container.html(data);						
			container.show();
					
			container.find(".full-height-scroll").slimScroll({
		        height: '100%'
		    })
			if(callback)
			{
				setTimeout(function(){					
					callback();
				},500);
			}
			
		});		
	}
	
	window.reloadContactDetails = function(){
		
		var contact = getSelectedContact();
		loadContactDetails(contact.id);
		
	}
	
	function addNewCall(idContact, contactName)
	{
		BootstrapDialog.show({
			 title: 'Add Call Information for ' + contactName ,
           message: $('<div></div>').load( basePath +  'contact/view-call-form/?idContact=' + idContact),
           buttons: [
               {
	               label: 'Close',
	               action: function(dialog) {
                   dialog.close();
	               }
               }]
       });
		
	}
	
}

