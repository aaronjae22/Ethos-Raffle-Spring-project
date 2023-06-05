function initializeEvents(basePath)
{
	$(document).ready(function() {
		
		$('.i-checks').iCheck({
			checkboxClass: 'icheckbox_square-green',
			radioClass: 'iradio_square-green',
		});
		
		//EVENTS
		$("#addNewTicket").on('click',function()
		{					
			addNewTicket();					
		});
		
		$("#period-select").on('change',function(){			
			//reload to other period
			document.location = basePath + "dashboard/home/" + $(this).val();
		});
		
		
		$("#exportTickets").on('click',function()
		{
			window.open(basePath+"/dashboard/home/export-results?idPeriod="+getPeriodId(),"_blank");
		});
	
		$("#exportContacts").on('click',function()
		{
			window.open(basePath+"/dashboard/home/export-contacts?idPeriod="+getPeriodId(),"_blank");
		});
	
		$("#toggleFilters").on('click',function()
		{
			toggleFilters();
		});
	
		
		$("#view-counter-week").on('click', function(){
			
			viewTicketCounterByWeek(getPeriodId());
		});
		
			
		
	
		$("#searchTextInput, #filters-row input").on('change',function()
		{
			loadTable();
		});
		
		$("#searchButton").on('click',function(){
			loadTable();			
		});
		
		$("#ticketsTable-container").on('click',".delete-ticket-button",function(){
			
			var cell = $($(this).parents("td").first());
			var ticketId = cell.attr('data-id');			
			deleteTicket(ticketId,cell);
		});
		
		
		$("#ticketsTable-container").on('click','.winner-ticket-button',function(){
			
			var cell = $($(this).parents("td").first());
			var ticketId = cell.attr('data-id');
			setWinnerTicket(ticketId, cell, true);
			
		});
		
		
		$("#ticketsTable-container").on('click',".details-ticket-button",function(){
			
			var cell = $($(this).parents("td").first());
			var ticketId = cell.attr('data-id');
			
			viewTicketDetails(ticketId);
		});
		
		
		$("#ticketsTable-container").on("click", ".edit-ticket-button",function(){
			
			var cell = $($(this).parents("td").first());
			var ticketId = cell.attr('data-id');
			
			editTicketInformation(ticketId);
			
		});
		
		$("#ticketsTable-container").on('click',".winner-ticket-label",function(){
			
			var cell = $($(this).parents("td").first());
			var ticketId = cell.attr('data-id');
			
			setWinnerTicket(ticketId, cell, false);
			
		});
		
		$("#purchaseDate").datepicker({
	        startView: 0,
	        todayBtn: "linked",
	        keyboardNavigation: false,
	        forceParse: false,
	        autoclose: true,
	        format: "mm/dd/yyyy"
	    });

	
		var url = basePath + '/contact/list';
	
		var $lastName = $("#last-name-select");
	
		$lastName.on("select2:select", function (e) { 
		
			
			var selectedContact = $lastName.select2('data');
			
			fillContactForm(selectedContact[0]);
			
		});
		
		$(".general-form").on('click','.add-ticket-row',function(event)
		{
			addTicketRow(this,event);
		});	
		
		$(".general-form").on('click',".delete-ticket-row",function(event){			
			deleteTicketRow(this,event);		
		});
		
		
		
		
		
		 $(".general-form").validate({
             rules: {
            	 lastName: {
                     required: true
                 },
                 firstName: {
                     required: true
                 },
                 purchaseDate: {
                     required: true
                 },
                 /*paymentType: {
                     required: true
                 },*/
                 address: {
                     required: true
                 }
             }
         });

		 
		
		
		
		
		  
		 
		$(".general-form").on('change',"[name=bundleNumber]",function(input, e,v)
				{
					
					var input = $(this);
					
					if(input.val().trim() == "")
						return;
					
					var validate = validateTicketNumberText(input.val());
					
					
					setTimeout(function(){
						
						if(input.val() != null || input.val() != "")
						{
							var row = input.find(".ticket-numbers-row");												
								
							row.find(".ticket-numbers").prop("disabled", true);
							
						}
						else
							{
							row.find(".ticket-numbers").prop("disabled", false);
							}
						
					},1000);
					
					
					if(!validate)
					{
						setTimeout(function(){							
							input.focus();							
						},200);
						return false;
					}
					else
					{
						var ticketRow = input.parents(".ticket-numbers-row");
						loadBundleTickets( getPeriodId(), input.val(), true , function(bundleData){ 
							
							if(bundleData != null)
							{
								if(bundleData.success && bundleData.bundleNumber != null) //tiene datos de bundle
								{
									
									if(!bundleData.broken){
										
										var items = [];
									
										//LJCM: load car tickets from select2
										loadMultipleTags(ticketRow.find(".car-tickets"),bundleData.carTickets);
										loadMultipleTags(ticketRow.find(".vac-tickets"),bundleData.vacTickets);
									}
									else
									{
										input.focus();
										toastr['error']("This bundle can't be used, because is Split into individual CAR and VAC. Tickets!, please provide a valid bundle number.");
										ticketRow.find("#bundleNumber").val('');
									}
								}
								else
									{
										input.focus();
										toastr['error'](bundleData.message);
									}
							}							
						} );
					}
					
				});
		
		
		
		$("#cancel-ticket-form").on('click',function(){
			
			cleanForm();
		});
		
		initializeTicketsTags(".general-form");
		
		$lastName.select2({
			tags:true,
			  ajax: {
				    url: url,
				    dataType: 'json',
				    delay: 500,
				    data: function (params) {
				      return {
				        searchTerm: params.term, // search term
				        limit: 20
				      };
				    },
				    processResults: function (data, params) {
				      // parse the results into the format expected by Select2
				      // since we are using custom formatting functions we do not need to
				      // alter the remote JSON data, except to indicate that infinite
				      // scrolling can be usedd
				      params.page = params.page || 1;
	
				      
				      for(var i = 0 ; i < data.length; i++)
				      {
					      var contact = data[i];
					      contact.id = contact.idContact;
				      }
	
				      						      
				      return {
				        results: data,
				        pagination: {
				          more: (params.page * 30) < data.total_count
				        }
				      };
				    },
				    cache: true
				  },
				  escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
				  minimumInputLength: 2,
				  dropdownCssClass: "bigdrop",
				  templateResult: formatRepo, // omitted for brevity, see the source of this page
				  templateSelection: formatRepoSelection // omitted for brevity, see the source of this page
				});
		
		
		
		
		$("#change-period-button").on('click',function(){
					
			BootstrapDialog.show({
				 title: 'Change Active Period',
	           message: $('<div></div>').load( basePath +  'dashboard/period-selection'),
	           buttons: [{
	               label: 'Close',
	               action: function(dialog) {
	                   dialog.close();
	               }
	           }]
	       });
			
			
		});
		
		
		$("#zipcode").on('change',loadZipCodeInformation);
		
		
		$("body").on('click',".set-active-period",function(){
			
			var idPeriod = $(this).parents('tr').attr("data-id");
			
			changeActivePeriod(idPeriod);
			
		});
		
	});
	
	
	function loadMultipleTags(select2Element, newTags)
	{ 
		
		if(newTags && newTags.length > 0)
		{
			select2Element.empty();
			
			  $.each(newTags,function(i,item){
	
			  var newOption = new Option(item, item, false, false);
			  select2Element.append(newOption);
			});
			  
			select2Element.val(newTags);
			select2Element.trigger('change');
		}
	}
	
	
	
	
	
	function loadZipCodeInformation()
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
					//add data to city and state
					$("#city").val(data.city);
					$("#state").val(data.state);
				}
				else
				{
					$("#city").val("");
					$("#state").val("");
					
					toastr['error'](data.error);
				}
			}).fail(function(data){
				
				$("#city").val("");
				$("#state").val("");
				toastr['error']("Zip code info not loaded! (Server Error)!");
				
			});
		
	}
				
	
	function changeActivePeriod(idPeriod)
	{
			
			
			$.ajax({
				 type: "POST",
				 url: basePath + "dashboard/set-active-period?idPeriod="+idPeriod,
				 data: {},
				 contentType: "application/json; charset=utf-8",
				 dataType: "json"			 
				}).done(function(data){
					
					
					if(data.success)
					{
						toastr['success']("Active Period changed successfully!")
						
						setTimeout(function(){ document.location = basePath + "dashboard/home" },3000);
						
					}
					else
					{
						toastr['error'](data.message);
						//toastr['error']("Ticket not saved, an unexpected error has occurred!");
					}
				}).fail(function(data){
					
					toastr['error']("Period not changed! (Server Error)!");
					
				});
	}
	
	
	function validateTicketNumberText(id)
	{
		if(id.length != 4)
		{
			toastr['warning']("The ticket number (" + id + ") is not valid, Please enter a 4 digits number!");
			return false;
		}
		
		try
		{
			var val =  parseFloat(id);
			
			if(val <= 0 || val % 1 != 0 )						
			{
				toastr['warning']("The ticket number (" + id + ") is not valid, Please enter a 4 digits number!");
				return false;
			}
		}
		catch(e)
		{
			toastr['warning']("The ticket number (" + id + ") is not valid, must be an integer.<br /> Please enter a 4 digits number");
			return false;
		}				
		return true;
	}
	
	 function formatRepo (repo) {
	      if (repo.loading) return repo.text;
	
	
	      if(repo.text)
	      {
		      var temp =
			      '<div class="list-group">				'+ 
			      "<h3> Add new contact (" + repo.text + ") </h3> "+
			     '</div>'
		      return temp;
	      }
	      var template = '<div class="list-group">										'+
	       '   <a href="#" class="list-group-item">								'+
	       '     <h4 class="list-group-item-heading"><i class="fa fa-user "></i> &nbsp;&nbsp;' + repo.firstName + ' ' + repo.lastName + '</h4>		'+
	       '<div class="row">'+
	       '     <div class="col-md-6 text-success"> <i class="fa fa-envelope "></i> &nbsp;&nbsp; ' 	+ (repo.email != null ? repo.email : 'N/A')  + ' </div>'+
	       '     <div class="col-md-6 text-success"> <i class="fa fa-phone "></i> &nbsp;&nbsp; ' 	+ (repo.phone != null ? repo.phone : 'N/A')  + ' </div>'+
	       '     <div class="col-md-6 "><p> <i class="fa fa-map-marker"></i> &nbsp;&nbsp; ' 		+ (repo.address != null ? repo.address : 'N/A')  + ' </p></div>'+
	       '     <div class="col-md-6 "> ' 		+ (repo.city != null ? repo.city : '') + ', ' + (repo.state != null ? repo.state : '') + ', ' + (repo.zipcode != null ? repo.zipcode : '')  + ' </div>'+
	       '</div> '+
	       '   </a>																				'+
	       ' </div>'
	       
	       return template;
	    }
	
	 function formatRepoSelection (repo) 
	 {
		 if(repo.text)
			 return repo.text;
		 else
	      return repo.lastName || '';
	    }
	
	function loadTable(callback)
	{
		var textInput = $("#searchTextInput");
		var idPeriod = getPeriodId();
		
		var filtersUrl = "";
		if($("#filters-row").is(":visible"))
		{
			var filtersInput = $("#filters-row input[data-id]");
			for(var i = 0 ; i <  filtersInput.length ; i++ )
			{
				
				var input = $(filtersInput[i]);			
				if(input.val())
					filtersUrl += "&" + input.attr("data-id") + "=" + encodeURIComponent(input.val());
			}
		}
		var url = basePath + 'dashboard/home/view-list' + '?idPeriod='+ idPeriod + '&searchText='+textInput.val()+ filtersUrl;
	
		$.get( url, function( data ) {		
			$("#ticketsTable-container #filters-row-body").replaceWith(data);		
			
			if(callback)
			{
				setTimeout(function(){					
					callback();
				},500);
			}
			
		});
		
	}
	
	
	function toggleFilters()
	{
		$("#filters-row").toggleClass("hidden");
	}
	
	function showEditor()
	{
		$(".cd-panel-right").toggleClass("is-visible");	
	}
	
	function addNewTicket()
	{
	
		showEditor();	
	}
	
	
	function fillContactForm(contact)
	{
		$("#firstName").val(contact.firstName);
		$("#phone").val(contact.phone);
		$("#email").val(contact.email);
				
		//$("#paymentType").val(contact.paymentType);
		setPaymentTypeValues(contact.paymentType);
		
		$("#address").val(contact.address);
		$("#city").val(contact.city);
		$("#state").val(contact.state);
		$("#zipcode").val(contact.zipcode);			
	}
	
	function setPaymentTypeValues(contactPaymenTypeList)
	{
		var checkboxes = $("[name=paymentType]");		
		var contactValues = (""+contactPaymenTypeList).split(",");
		
		checkboxes.each(function(i, checkbox){
			
			checkbox = $(checkbox);
			
			var val = checkbox .val();
			
			if( contactValues.indexOf(val) > -1 )
			{
				checkbox .prop("checked", true);
			}
			else
				checkbox .prop("checked", false);
			
			checkbox .iCheck('update');
		});
	}
	window.setPaymentTypeValues = setPaymentTypeValues;
	
	function addTicketRow()
	{
		var newRow;
		var newRow = $($("#row-template").html());
		newRow.removeAttr("id");
		newRow.removeClass("row-1");
		
		newRow.addClass("row-"+ ($(".ticket-numbers-row") .length + 1));
		
		//agregarla al final
		$(".ticket-rows").append(newRow);
		
		//agregar los select2
		initializeTicketsTags(newRow);
	}
	
	function deleteTicketRow(scope,event)
	{

		var row = $(scope).parents(".ticket-numbers-row");		
		row.find(".ticket-numbers").each(function(item){
			$(this).select2('destroy');
		});
		row.remove();
		
	}
	
	function renderTotals()
	{
		
		var ticketTable = $("#ticketsTable");
		
		var tbody = ticketTable.find("tbody");
		
		var totals = {};
		totals.countBundle = 	parseInt(tbody.attr("data-count-bundle")).formatMoney(0,'.',',');
		totals.countCar = 		parseInt(tbody.attr("data-count-car")).formatMoney(0,'.',',');
		totals.countVac = 		parseInt(tbody.attr("data-count-vac")).formatMoney(0,'.',',');			
		
		totals.sumBundle 	= "$ " + parseFloat(tbody.attr("data-sum-bundle")).formatMoney(2,'.',',');
		totals.sumCar 		= "$ " + parseFloat(tbody.attr("data-sum-car")).formatMoney(2,'.',',');
		totals.sumVac 		= "$ " + parseFloat(tbody.attr("data-sum-vac")).formatMoney(2,'.',',');
		
		
		$("#totalCar").text(totals.sumCar);
		$("#totalVac").text(totals.sumVac);
		$("#totalBundle").text(totals.sumBundle);
		
		$("#counterBundle").text(totals.countBundle);		
		$("#counterCar").text(totals.countCar);
		$("#counterVac").text(totals.countVac);
		
		
         $(".counter-label").addClass('animated bounce');

         setTimeout(function(){        	 
        	 $(".counter-label").removeClass('animated bounce');
         },2000);
	}
	
	function getTicketInputs()
	{
		var contact = {};
		
		var previousContact = $("#last-name-select").select2('data')[0];
		
		if(previousContact)		
		{
			if(!previousContact.text)
			{
				contact.idContact = previousContact.idContact;
				contact.lastName = previousContact.lastName;
			}
			else
			{
				contact.idContact = 0;
				contact.lastName = previousContact.text;
			}
		}
		
		//contact.idProduct = $("[name='product']:checked").val();
		contact.firstName 	= $("#firstName").val();
		contact.phone		= $("#phone").val();
		contact.email		= $("#email").val();
		
		
		var paymentType = "";
		var paymentTypeList = $("[name=paymentType]:checked");
		paymentTypeList.each(function(i, item){
			
			paymentType += $(item).val()+ ( i == paymentTypeList.length -1 ? "" : ",");
			
		});
		contact.paymentType		= paymentType;
		
		contact.purchaseDate= $("#purchaseDate").datepicker('getDate');		
		contact.address	= $("#address").val();
		
		contact.memo	= $("#memo").val();
		
		contact.city		= $("#city").val();
		contact.state		= $("#state").val();
		contact.zipcode	= $("#zipcode").val();
		
		contact.ticketNumber	= $("#ticketNumber").val();
	
		return contact;
	}
	
	
	function initializeTicketsTags(selector)
	{
		if(selector)
		{
			
			var select2Vars = $(selector).find(".ticket-numbers").select2({
				tags:true,
				multiple: true,
		        allowclear: true,
		        dropdownCssClass: 'select2-hidden',
				placeholder: "Add your tickets # here",
				tokenSeparators: [',',' '],
				
			});
			
			
			select2Vars.on('select2:selecting',function(evt){
				
				var id =  evt.params.args.data.id;				
				var validate = validateTicketNumberText(id);
				
				var select = $(this);
				var idProduct = select.hasClass("car-tickets") ? 1: 2;
				
				
				
				ticketNumberValidation(select, id, idProduct, function(success, resultData){
					
					//si es correcto, entonces validar tambien el bundle info
					if(success)
						bundleValidationTicket( select, id, idProduct  );
					
				} );
				
				
				
				return validate;
				
			});
			
			$('.ticket-numbers').next('.select2').find('.select2-selection').one('focus', select2Focus).on('blur', function () {
			    $(this).one('focus', select2Focus)
			})
			
			function select2Focus() {
			    $(this).closest('.select2').prev('select').select2('open');
			}
			
		}
	}
	
	
	//valida 	
	function ticketNumberValidation(select2, ticketNumber, productId, callback)
	{
		
		
		var idPeriod = getPeriodId();
		
		$.ajax({
			 type: "POST",
			 url: basePath + "ticket/validate-ticket-number?idPeriod="+idPeriod+"&ticketNumber="+ticketNumber+"&idProduct="+productId,
			 data: {},
			 contentType: "application/json; charset=utf-8",
			 dataType: "json"			 
			}).done(function(data){
							
				if(data.success)
				{
					if(callback)
						callback(true,data);
				}
				else
				{
					if(callback)
						callback(false,data);
					
					
					var selectedItems = select2.select2('data');									
					var vals = select2.val()||[];					
					var newVals = [];		
					newVals = vals.filter(function(ele){
						return ele != ticketNumber;
					})														
					select2.val(newVals);
					select2.trigger('change');
					
					
					$.each(data.multipleMessage, function(i, message){						
							toastr['error'](message);						
					});
					
					setTimeout(function(){						
						select2.select2('open');
					},300)
					
				}
			}).fail(function(data){
				
				
				if(callback)
					callback(false,null);
				
				toastr['error']("Ticket information not loaded! (Server Error)!");				
			});
		
	}
	
	///valida si la ticket esta en algun bundle
	function bundleValidationTicket(select2, ticketNumber, productId)
	{
		var idPeriod = getPeriodId();
		//obtener la informacion del bundle
		loadBundleTickets( idPeriod, ticketNumber, false, function(data){
			
			
			
			if(data!=null && data.success)
			{
				if(data.bundleNumber != null && !data.broken )
				{
					var selectedItems = select2.select2('data');
					
					
					var vals = select2.val()||[];					
					var newVals = [];		
					
					
					newVals = vals.filter(function(ele){
						return ele != ticketNumber;
					})
										
					
					select2.val(newVals);
					select2.trigger('change');
					
					select2.focus();
					
					var dialogInstance =BootstrapDialog.show({
						 title: 'Bundle Information',
						 size: BootstrapDialog.SIZE_WIDE,
			            message: $('<div></div>').load( basePath +  '/ticket/view-bundle-details?idPeriod='+ idPeriod +'&bundleNumber='+ data.bundleNumber +'&ticketNumberFocus='+ticketNumber),
			            buttons: [{
			                label: 'Close',
			                action: function(dialog) {
			                    dialog.close();
			                }
			            }]
			        });
					
					window.confirmBundleSplit = function(bundleNumber, ticketFocus)
					{
						var vals = select2.val() || [];						
						vals.push(ticketFocus);
											
											
						//agregar la ticket que no se permitió agregar
						select2.val(vals);						
						select2.trigger('change');
						
						dialogInstance.close();
						
						
					}
					//el ticket esta en algun bundle
					toastr['error']("This ticket is part of an active Bundle, you have to separate the Bundle!");
				}
			}
			else
			{
				//no data available
			}
		} );
		
		
	}
	
	function saveMultipleTickets(tickets)
	{
		var json = JSON.stringify(tickets); 
		
		$.ajax({
			 type: "POST",
			 url: basePath + "ticket/save-multiple-tickets",
			 data: json,
			 contentType: "application/json; charset=utf-8",
			 dataType: "json"			 
			}).done(function(data){
				
				if(data.success)
				{
					toastr['success']("The ticket was saved successfully!.");
					
					cleanForm();					
					
					loadTable(renderTotals);					
				}
				else
				{
					toastr['error'](data.message);
					//toastr['error']("Ticket not saved, an unexpected error has occurred!");
				}
			}).fail(function(data){
				
				toastr['error']("Ticket not saved, an unexpected error has occurred (Server Error)!");
				
			});
	}
	
	function cleanForm()
	{
		
		$("#firstName").val('');
		$("#phone").val('');
		$("#email").val('');
		
		$("[name=paymentType]").prop('checked',false);
		$("[name=paymentType]").iCheck('update');
		
				
		$("#address").val('');
		$("#city").val('');
		$("#state").val('');
		$("#zipcode").val('');
		
		$("#memo").val('');
		
		$("#last-name-select").select2('val',null);
		
		var rows = $(".ticket-numbers-row");
		
		for(var i  = 0 ; i < rows.length; i ++)
		{
			var row = $(rows[i]);
			
			if(row.hasClass('row-1'))
			{
			
				row.find(".ticket-numbers").each(function(item){					
					$(this).select2('val',null);					
				}); 
				
				row.find("#bundleNumber").val('');
			}
			else				
			{
				row.find(".ticket-numbers").each(function(item){					
					$(this).select2('destroy');					
				}); 
				
				row.remove();		
			}
		}
		
	}
	
	function saveTicket(contact)
	{
		var json = JSON.stringify(contact); 
		
		$.ajax({
			 type: "POST",
			 url: basePath + "ticket/save-ticket",
			 data: json,
			 contentType: "application/json; charset=utf-8",
			 dataType: "json"			 
			}).done(function(data){
				
				if(data.success)
				{
					loadTable(renderTotals);
					
					toastr['success']("The ticket was saved successfully!.")					
				}
				else
				{
					toastr['error']("Ticket not saved, an unexpected error has occurred!");
				}
			}).fail(function(data){
				
				alert('fail: ' + data);
				
			});		
	}
	
	saveTicketGeneral = function(e)
	{		
		
		if(!$(".general-form").valid())
		{			
			return;
		}
		
		
		if( $("[name=paymentType]:checked").length == 0 )
		{
			toastr['error']("Please provide Payment Type information!");
			return;
		}
		
		
		var ticket = getTicketInputs();		
		var bundleTickets = [];
	
	
		var ticketRows = $(".general-form .ticket-numbers-row");
		var  genralBundleTicket = {bundleTickets : []};
		
		for(var i  = 0 ; i < ticketRows.length ; i++)
		{
			var bundleTicket = {};
			var row = $(ticketRows[i]);			
			var bundleNumber = row.find('[name=bundleNumber]').val();
			
			bundleTicket.bundleNumber = bundleNumber;
			bundleTicket.carTickets = [];
			bundleTicket.vacTickets = [];
			
			var carTicketNumbers = row.find(".car-tickets").select2('data');
			
			for(var j = 0 ; j < carTicketNumbers.length; j++)
			{
				bundleTicket.carTickets.push(carTicketNumbers[j].id);
			}

			var vacTicketNumbers = row.find(".vac-tickets").select2('data');
			for(var j = 0 ; j < vacTicketNumbers.length; j++)
			{
				bundleTicket.vacTickets.push(vacTicketNumbers[j].id);
			}
			
			if(bundleTicket.vacTickets.length + bundleTicket.carTickets.length > 0)
				genralBundleTicket.bundleTickets.push(bundleTicket);
		}		
		
		genralBundleTicket.contactInformation = ticket;
		
		
		
		if(genralBundleTicket.bundleTickets.length == 0  )
		{
			toastr['error']("Please provide the Ticket number(s)");
			return;
		}
			
		validateTicketNumbers( genralBundleTicket,  function(){ //onSuccess
			saveMultipleTickets(genralBundleTicket)	;
		});
					
	}
	
	
	deleteTicket = function(idTicket,cell)
	{
		
		var message = '<br/> <div> <input id="remove-item" name="remove-item" style="display:inline;width:20px;margin:0px;height:20px;" type="checkbox" /> <label for="remove-item" style="cursor:pointer" >Remove this item!</label><div/> '
		
		
		//confirmar
		swal({
	        title: "Are you sure?",
	        text: 'This action will void this ticket, the ticket number will be available to be used!' + message,
	        type: "warning",
	        showCancelButton: true,
	        confirmButtonColor: "#DD6B55",
	        confirmButtonText: "Yes, void this ticket!",
	        html: true,
	        closeOnConfirm: true
	    }, function () {
	    	
	    	voidTicket(idTicket,cell);
	    	
	    });
		
	}
		
	function voidTicket(idTicket,cell)
	{
		
		var deleteData = $("#remove-item").is(":checked");
				
		$.ajax({
			 type: "POST",
			 url: basePath + "ticket/delete-ticket?idTicket=" + idTicket+"&remove=" + deleteData,
			 //data: json,
			 contentType: "application/json; charset=utf-8",
			 dataType: "json"			 
			}).done(function(data){
				
				if(data.success)
				{
					
					loadTable(renderTotals);
					
					toastr['success']("The ticket was void successfully!.");
					
					$(cell).removeClass("regular-ticket").addClass("inactive-ticket");
					
					
				}
				else
				{
					toastr['error'](data.message);
				}
			}).fail(function(data){
				
				toastr['error']("Ticket not saved, an unexpected error has occurred (Server Error)!");
				
			});
	}
	
	
	function validateTicketNumbers( generalBundleTicket, callback )
	{		
		var ticketNumbers = generalBundleTicket; //Send All ticket info//getAllTicketAllNumbers(generalBundleTicket);		
		var json = JSON.stringify( ticketNumbers  );
		
		
		var bundleValidation = validateBundleTicketNumbers(generalBundleTicket);
				
		if(bundleValidation != null && bundleValidation.length > 0)
		{
			var messages = "<ul>";
			for(var i = 0 ; i < bundleValidation.length ;i ++)
			{
				messages += "<li>" + bundleValidation[i] + "</li>";
			}
			messages += "</ul>";
			
			toastr.options.timeOut = 10000;
			toastr['error'](messages);
			
			return ;
		}
		
		
		$.ajax({
			 type: "POST",
			 url: basePath + "ticket/validate-ticket",
			 data: json,
			 contentType: "application/json; charset=utf-8",
			 dataType: "json"			 
			}).done(function(data){
				
				if(data.success)
				{
					if(data.multipleMessage && data.multipleMessage.length > 0)
					{
						var messageHtml = "<ul>";
						
						for(var  i = 0 ; i < data.multipleMessage.length;i++)
						{
							messageHtml+= "<li>"+data.multipleMessage[i]+"</li>";
						}
						messageHtml+="</ul>";
						
						toastr.options.timeOut = 10000;
						toastr['error'](messageHtml);
							
					}
					else
					{
						if(callback)
						{
							callback();
						}
					}
				}
				else
				{ 
					toastr['error'](data.message);
				}
			}).fail(function(data){
				
				toastr['error']("Ticket not validated, an unexpected error has occurred (Server Error)!");
				
			});
		
	}
	
	
	function validateBundleTicketNumbers(generalBundleTicket)
	{		
		var messages = [];
		
		for(var i = 0 ; i < generalBundleTicket.bundleTickets.length;i++)
		{
			var ticketRow = generalBundleTicket.bundleTickets[i];			
			if(ticketRow.bundleNumber)
			{
				var carTicketsCount = ticketRow.carTickets ? ticketRow.carTickets.length : 0;				
				var vacTicketsCount = ticketRow.vacTickets ? ticketRow.vacTickets.length : 0;
				
				
				if(carTicketsCount != 4 || vacTicketsCount != 5)
				{
					messages.push( "The bundle " + ticketRow.bundleNumber + " must have 4 Car tickets, and 5 vacation tickets!");
				}
							
			}
		}
		
		return messages;
	}
	
	function viewTicketDetails(idTicket)
	{
		BootstrapDialog.show({
			 title: 'Additional Ticket Information',
            message: $('<div></div>').load( basePath +  'dashboard/home/view-ticket-details?idTicket=' + idTicket),
            buttons: [{
                label: 'Close',
                action: function(dialog) {
                    dialog.close();
                }
            }]
        });
	}
	
	function editTicketInformation(idTicket)
	{
		BootstrapDialog.show({
			 title: 'Edit Ticket Form',
			 size:BootstrapDialog.SIZE_WIDE,
           message: $('<div></div>').load( basePath +  'ticket/ticket-edit-form?idTicket=' + idTicket+"&idPeriod="+getPeriodId()),
           buttons: [
                     {
                    	 label: 'Change Ticket',
                    	 cssClass: 'btn-success btn-lg',
                    	 action: function(dialog) {
                    		 
                    		 
                    		 saveEditTicketInformation();
                 		                    		 
                    		 dialog.close();
                    	 }
                     },
                     {
               label: 'Close',
               cssClass:'btn-lg',
               action: function(dialog) {
                   dialog.close();
               }
           },
           
           ]
       });
	}
	
	function saveEditTicketInformation()
	{
		//obtener la data necesaria
		
		
		var idPeriod = getPeriodId(); 
		var newTicketNumber = $("#newTicketNumber").val(); 
		var oldTicketNumber = $("#oldTicketNumber").val(); 
		var idProduct;		
		var purchaseDate = $("#purchaseDate").datepicker('getDate'); 
		var productName = $("#productName").val();;
		
		var paymentType = "";
		var paymentTypeList = $("[name=paymentType]:checked");
		paymentTypeList.each(function(i, item){
			
			paymentType += $(item).val()+ ( i == paymentTypeList.length -1 ? "" : ",");
			
		});
		
		
		if(newTicketNumber == oldTicketNumber)
			{
			toastr['error']("The Old and New ticket number must be different!");
			return;
			}
		
		idProduct = productName == "Car" ? 1 : 2;
		
		
		if(productName == "Bundle")
			idProduct = 3;
		
		
		swal({
			title: "Are you sure?",
			text: "This action will change this ticket as Information!",
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#1ab394",
			confirmButtonText: "Yes, change it!",
			closeOnConfirm: true
		}, function () {
			
			$.ajax({
				 type: "POST",
				 url: basePath + "/ticket/save-new-edit-ticket",
				 data: {
					 
					 idPeriod : idPeriod, 
					 newTicketNumber : newTicketNumber,
					 oldTicketNumber : oldTicketNumber, 
					 idProduct		 : idProduct , 
					 purchaseDate	 : purchaseDate, 
					 paymentType	 : paymentType
					 
				 },
				 contentType: "application/x-www-form-urlencoded",
				 dataType: "json"			 
				}).done(function(data){
					
					if(data.success)
					{
						if(data.multipleMessage && data.multipleMessage.length > 0)
						{
							var messageHtml = "<ul>";
							
							for(var  i = 0 ; i < data.multipleMessage.length;i++)
							{
								messageHtml+= "<li>"+data.multipleMessage[i]+"</li>";
							}
							messageHtml+="</ul>";
							
							toastr.options.timeOut = 10000;
							toastr['error'](messageHtml);
								
						}
						else
						{
							loadTable();
							
							toastr['success']("The ticket information saved successfully!");
						}
					}
					else
					{ 
						toastr['error'](data.message);
					}
				}).fail(function(data){
					
					toastr['error']("Ticket not changed, an unexpected error has occurred (Server Error)!");
					
				});
			
		});
		
		
		
		
		
		
	}
	
	function viewTicketCounterByWeek(idPeriod)
	{
		BootstrapDialog.show({
			 title: 'Ticket Count by Week',
           message: $('<div></div>').load( basePath +  'ticket/view-ticket-counters?idPeriod=' + idPeriod),
           buttons: [{
               label: 'Close',
               action: function(dialog) {
                   dialog.close();
               }
           }]
       });
	}
	
	function setWinnerTicket(idTicket, cell, makeWinner)
	{
		
		if( makeWinner )
		{
			swal({
				title: "Are you sure?",
				text: "This action will mark this ticket as Winner!",
				type: "warning",
				showCancelButton: true,
				confirmButtonColor: "#1ab394",
				confirmButtonText: "Yes, set as Winner!",
				closeOnConfirm: true
			}, function () {
				
				setWinnerTicketRequest(idTicket,cell, true);
			});			
		}
		else
		{
			setWinnerTicketRequest(idTicket,cell, false);
			loadTable();
		}
		
	}
	
	function setWinnerTicketRequest(idTicket,cell, makeWinner){
		
		$.ajax({
			 type: "POST",
			 url: basePath + "ticket/winner-ticket?idTicket=" + idTicket+"&makeWinner="+makeWinner,
			 //data: json,
			 contentType: "application/json; charset=utf-8",
			 dataType: "json"			 
			}).done(function(data){
				
				if(data.success)
				{
					if(data.multipleMessage && data.multipleMessage.length > 0)
					{
						var messageHtml = "<ul>";
						
						for(var  i = 0 ; i < data.multipleMessage.length;i++)
						{
							messageHtml+= "<li>"+data.multipleMessage[i]+"</li>";
						}
						messageHtml+="</ul>";
						
						toastr.options.timeOut = 10000;
						toastr['error'](messageHtml);
							
					}
					else
					{
						loadTable();
						if(makeWinner)
							toastr['success']("Ticket is now a winner ticket!");
						else
							toastr['success']("This ticket is now NOT a winner ticket!");
					}
				}
				else
				{ 
					toastr['error'](data.message);
				}
			}).fail(function(data){
				
				toastr['error']("Ticket not changed, an unexpected error has occurred (Server Error)!");
				
			});
	}
	
}

function loadBundleTickets(idPeriod,ticketNumber,isBundle ,callback)
{
	$.ajax({
		 type: "POST",
		 url: basePath + "ticket/view-bundle-info?isBundleNumber=" + isBundle+ "&idPeriod="+idPeriod+"&ticketNumber="+ticketNumber,
		 data: {},
		 contentType: "application/json; charset=utf-8",
		 dataType: "json"			 
		}).done(function(data){
						
			if(data.error == null)
			{
				if(callback)
					callback(data);
			}
			else
			{
				if(callback)
					callback(data);
				
				toastr['error'](data.error);
			}
		}).fail(function(data){
			
			if(callback)
				callback(null);
			
			toastr['error']("Ticket information not loaded! (Server Error)!");				
		});
	
}

//http://localhost:8080/raffle/ticket/validate-ticket-number?idPeriod=7&ticketNumber=0005&idProduct=1

function getPeriodId()
{
	 return $("#period-select").val();
}


Number.prototype.formatMoney = function(c, d, t){
	var n = this, 
	    c = isNaN(c = Math.abs(c)) ? 2 : c, 
	    d = d == undefined ? "." : d, 
	    t = t == undefined ? "," : t, 
	    s = n < 0 ? "-" : "", 
	    i = String(parseInt(n = Math.abs(Number(n) || 0).toFixed(c))), 
	    j = (j = i.length) > 3 ? j % 3 : 0;
	   return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
	 };
	 
	 