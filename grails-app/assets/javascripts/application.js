// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better
// to create separate JavaScript files as needed.
//
//= require jquery-2.2.0.min
//= require bootstrap
//= require_tree .
//= require_self

if (typeof jQuery !== 'undefined') {
    (function($) {
        $(document).ajaxStart(function() {
            $('#spinner').fadeIn();
        }).ajaxStop(function() {
            $('#spinner').fadeOut();
        });
    })(jQuery);
}

$(document).ready(function(){
    $('.i-checks').iCheck({
        checkboxClass: 'icheckbox_square-green',
        radioClass: 'iradio_square-green',
    });

    $('#date_1 .input-group.date').datepicker({
    	useCurrent: true,
        todayBtn: "linked",
        keyboardNavigation: false,
        forceParse: false,
        autoclose: true,
        format: 'dd MM yyyy'
    }).datepicker("setDate", "0");
    
    $("[name='status']").bootstrapSwitch();
    $("[name='status']").bootstrapSwitch('onColor','traderapp_switch');
    
    $('.footable').footable({
    	"columns": [{
			"sortable": false
		}]
    });
    
    var next = 0;
    $("#add-more").click(function(e){
        e.preventDefault();
        var addto = "#field" + next;
        var addRemove = "#field" + (next);
        next = next + 1;
        var newIn = '<div id="field'+ next +'" name="field'+ next +'"><div class="form-group col-md-4"><input id="part_name'+ next +'" name="part_name'+ next +'" type="text" placeholder="Part Name" class="form-control input-md"></div><div class="form-group col-md-3"><input id="qty'+ next +'" name="qty'+ next +'" min="0" type="number" onchange="calculateTotal();" placeholder="Qty" value="0" class="form-control input-md"></div><div class="form-group col-md-4"><span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input  id="unit_price'+ next +'" name="unit_price'+ next +'" min="0" type="number" onchange="formatThisNumber(\'unit_price'+ next +'\');" placeholder="Unit Price" value="0.00" class="form-control input-md" style="display: inline; width: 88%;" step=".01" ></div></div>';
        var newInput = $(newIn);
        var removeBtn = '<div id="btnDiv'+(next - 1)+'" class="col-md-1"><button id="remove' + (next - 1) + '" class="btn btn-danger remove-me" >-</button></div>';
        var removeButton = $(removeBtn);
        $(addto).after(newInput);
        $(addRemove).after(removeButton);
        $("#field" + next).attr('data-source',$(addto).attr('data-source'));
        $("#count").val(next);  
        
            $('.remove-me').click(function(e){
                e.preventDefault();
                var fieldNum = this.id.replace("remove", "");
                //var fieldNum = this.id.charAt(this.id.length-1);
                var fieldID = "#field" + fieldNum;
                var btnDiv = "#btnDiv" + fieldNum;
                $(fieldID).remove();
                $(btnDiv).remove();
            });
    });
    $('#tradesman_amazon_pay').on('ifClicked', function(event){toggleDiv('amazonPay_cred_div');});
    $('#tradesman_paypal').on('ifClicked', function(event){toggleDiv('paypal_cred_div');});
});

function calculateTotalLaborCost(hourlyRate, type){
	if(hourlyRate != null && hourlyRate != "" && hourlyRate > 0){
		if(type == "edit"){
			var laborHours = $("#laborHours_edit").val();
			var totalLaborCost = laborHours * hourlyRate;
			$("#laborTotalCost_edit").val(parseFloat(totalLaborCost).toFixed(2));
		} else {
			var laborHours = $("#laborHours").val();
			var totalLaborCost = laborHours * hourlyRate;
			$("#laborTotalCost").val(parseFloat(totalLaborCost).toFixed(2));
		}
	}
}

$("#customerName_create").autocomplete({
	source : function(request, response) {
		$.ajax({
			url : '/search/searchByCustomerName',
			data : request,
			success : function(data) {
				if ((data).length == 0) {
					data.push({
						id : 0,
						label : "No Results Found",
						key: "NoData"
					});
				}
				response(data); // set the response
			},
			error : function() { // handle server errors
				console.log('not able to load results');
			}
		});
	},
	autoFocus:true,
	select : function(event, ui) {
		if(ui.item.key == "NoData"){
			return false;
		} else {
			event.preventDefault();
			$("#customerName_create").val(ui.item.name);
			$("#customerEmail_create").val(ui.item.email);
			$("#addressLine1_create").val(ui.item.addressLine1);
			$("#addressLine2_create").val(ui.item.addressLine2);
			$("#addressLine3_create").val(ui.item.addressLine3);
			$("#postcode_create").val(ui.item.postcode);
		}
	}
});

$("#customerEmail_create").autocomplete({
	source : function(request, response) {
		$.ajax({
			url : '/search/searchByCustomerEmail',
			data : request,
			success : function(data) {
				if ((data).length == 0) {
					data.push({
						id : 0,
						label : "No Results Found",
						key: "NoData"
					});
				}
				response(data); // set the response
			},
			error : function() { // handle server errors
				console.log('not able to load results');
			}
		});
	},
	autoFocus:true,
	select : function(event, ui) {
		if(ui.item.key == "NoData"){
			return false;
		} else {
			event.preventDefault();
			$("#customerName_create").val(ui.item.name);
			$("#customerEmail_create").val(ui.item.email);
			$("#addressLine1_create").val(ui.item.addressLine1);
			$("#addressLine2_create").val(ui.item.addressLine2);
			$("#addressLine3_create").val(ui.item.addressLine3);
			$("#postcode_create").val(ui.item.postcode);
		}
	}
});

$("#tradesmanEmail_create").autocomplete({
	source : function(request, response) {
		$.ajax({
			url : '/search/searchByTradesmanEmail',
			data : request,
			success : function(data) {
				if ((data).length == 0) {
					data.push({
						id : 0,
						label : "No Results Found",
						key: "NoData"
					});
				}
				response(data); // set the response
			},
			error : function() { // handle server errors
				console.log('not able to load results');
			}
		});
	},
	autoFocus:true,
	select : function(event, ui) {
		if(ui.item.key == "NoData"){
			return false;
		} else {
			event.preventDefault();
			$("#tradesmanCompanyName_create").val(ui.item.name);
			$("#tradesmanEmail_create").val(ui.item.email);
			$.ajax({
				type:'get',
				url:'/search/getWorkTypes?id='+ui.item.id,
				success:function(data){
					$('#jobType_create')
				    .find('option')
				    .remove();
					if(data.length > 1){
						$('#jobType_create')
					    .find('option')
					    .end().append('<option value="">-Choose Job Type-</option>');
					}
					for(var i=0; i < data.length; i++){
						$('#jobType_create')
					    .find('option')
					    .end()
					    .append('<option value="'+data[i]+'">'+data[i]+'</option>');
					}
				}
			});
		}
	}
});

$("#tradesmanCompanyName_create").autocomplete({
	source : function(request, response) {
		$.ajax({
			url : '/search/searchByTradesmanCompanyName',
			data : request,
			success : function(data) {
				if ((data).length == 0) {
					data.push({
						id : 0,
						label : "No Results Found",
						key: "NoData"
					});
				}
				response(data); // set the response
			},
			error : function() { // handle server errors
				console.log('not able to load results');
			}
		});
	},
	autoFocus:true,
	select : function(event, ui) {
		if(ui.item.key == "NoData"){
			return false;
		} else {
			event.preventDefault();
			$("#tradesmanCompanyName_create").val(ui.item.name);
			$("#tradesmanEmail_create").val(ui.item.email);
			$.ajax({
				type:'get',
				url:'/search/getWorkTypes?id='+ui.item.id,
				success:function(data){
				    $('#jobType_create')
				    .find('option')
				    .remove();
					if(data.length > 1){
						$('#jobType_create')
					    .find('option')
					    .end().append('<option value="">-Choose Job Type-</option>');
					}
					for(var i=0; i < data.length; i++){
						$('#jobType_create')
					    .find('option')
					    .end()
					    .append('<option value="'+data[i]+'">'+data[i]+'</option>');
					}
				}
			});
		}
	}
});

function validateTextBox(id){
	var value = $('#'+id).val();
	if(value.trim() == null || value.trim() == ""){
		$('#'+id).val('');
	}
}

function validateReEnteredPassword(id1, id2){
	var value = $('#'+id1).val();
	if(value.trim() == null || value.trim() == ""){
		$('#'+id1).val('');
	} else{
		var re_password = $("#"+id2).val();
		if(value != re_password){
			if(!$("#"+id1).hasClass('errorBorder')){
				$("#"+id1).addClass('errorBorder');
			}
		} else{
			$("#"+id1).removeClass('errorBorder');
		}
	}
}

function checkEmail(mail){
	var regex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
	return regex.test(mail);
}

function checkPassword(password){
	var regex = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}/;
	return regex.test(password);
}

function validateEmail(id){
	var value = $('#'+id).val();
	if(value.trim() == null || value.trim() == ""){
		$('#'+id).val('');
	} else{
		if(checkEmail(value) == false){
			if(!$("#"+id).hasClass('errorBorder')){
				$("#"+id).addClass('errorBorder');
			}
		} else{
			$("#"+id).removeClass('errorBorder');
		}
	}
}

function validatePassword(id){
	var value = $('#'+id).val();
	if(value.trim() == null || value.trim() == ""){
		$('#'+id).val('');
	} else{
		if(checkPassword(value) == false){
			if(!$("#"+id).hasClass('errorBorder')){
				$("#"+id).addClass('errorBorder');
			}
		} else{
			$("#"+id).removeClass('errorBorder');
		}
	}
}

function checkUsernameExists(email){
	$.ajax({
		type:'get',
		url:'/newRegistrations/checkExistingUser?username='+email,
		success:function(data){
			if(data === "Exists"){
				return true;
			} else{
				return false;
			}
		}
	});
}

function validateForTradesman(){
	var email = $("#customerEmail_create").val();
	if(email){
		if(!checkEmail(email)){
			$("#errorAlert").html('');
			$("#errorAlert").html("Please enter valid email address !!");
			$("#errorAlert").show();
			event.preventDefault();
		} else{
			$.ajax({
				type:'get',
				url:'/jobs/validateNotTradesman?email='+email,
				success:function(data){
					if(data == true || data == 'true'){
						$("#errorAlert").html('');
						$("#errorAlert").html("This email address is already registered as tradesman in the system. Please enter different email address to create job for customer.");
						$("#errorAlert").show();
						event.preventDefault();
					} else{
						$("#errorAlert").html('');
						$("#errorAlert").hide();
						$("#createNewJob").submit();
					}
				}
			});
		}
	} else{
		$("#errorAlert").html('');
		$("#errorAlert").html("Please enter valid email address !!");
		$("#errorAlert").show();
		event.preventDefault();
	}
}

function validateNotCustomer(){
	var email = $("#tradesmanEmail_create").val();
	if(email){
		if(!checkEmail(email)){
			$("#errorAlert").html('');
			$("#errorAlert").html("Please enter valid email address !!");
			$("#errorAlert").show();
			event.preventDefault();
		} else{
			$.ajax({
				type:'get',
				url:'/jobs/validateNotCustomer?email='+email,
				success:function(data){
					if(data == true || data == 'true'){
						$("#errorAlert").html('');
						$("#errorAlert").html("This email address is already registered as customer in the system. Please enter different email address to create job for tradesman.");
						$("#errorAlert").show();
						event.preventDefault();
					} else{
						$("#errorAlert").html('');
						$("#errorAlert").hide();
						$("#createNewJob_customer").submit();
					}
				}
			});
		}
	} else{
		$("#errorAlert_editJob").html('');
		$("#errorAlert_editJob").html("Please enter valid email address !!");
		$("#errorAlert_editJob").show();
		event.preventDefault();
	}
}


function validateJobEditEmail(){
	var email = $("#customerEmail_edit").val();
	if(email){
		if(!checkEmail(email)){
			$("#errorAlert_editJob").html('');
			$("#errorAlert_editJob").html("Please enter valid email address !!");
			$("#errorAlert_editJob").show();
			event.preventDefault();
		} else{
			$.ajax({
				type:'get',
				url:'/jobs/validateNotTradesman?email='+email,
				success:function(data){
					if(data == true || data == 'true'){
						$("#errorAlert_editJob").html('');
						$("#errorAlert_editJob").html("This email address is already registered as tradesman in the system. Please enter different email address to create job for customer.");
						$("#errorAlert_editJob").show();
						event.preventDefault();
					} else{
						$("#errorAlert_editJob").html('');
						$("#errorAlert_editJob").hide();
						$("#editJob").submit();
					}
				}
			});
		}
	} else{
		$("#errorAlert_editJob").html('');
		$("#errorAlert_editJob").html("Please enter valid email address !!");
		$("#errorAlert_editJob").show();
		event.preventDefault();
	}
}

$("#customerForm").submit(function(event){
	if($("#name").val() && $("#email").val() && $("#password").val() && $("#reEnteredPassword").val()){
		var email =  $("#email").val();
		var password = $("#password").val();
		var reEnteredPassword = $("#reEnteredPassword").val();
		if(!checkEmail(email)){
			$("#errorAlert").html('');
			$("#errorAlert").html("Please enter valid email address !!");
			$("#errorAlert").show();
			event.preventDefault();
		} else{
			if(!checkPassword(password)){
				$("#errorAlert").html('');
				$("#errorAlert").html("Please enter valid password !!");
				$("#errorAlert").show();
				event.preventDefault();
			} else{
				if(password == reEnteredPassword){
					$("#errorAlert").html('');
					$("#errorAlert").hide();
					return;
				} else{
					$("#errorAlert").html('');
					$("#errorAlert").html("Password and Re-entered password does not match !!");
					$("#errorAlert").show();
					event.preventDefault();
				}
			}
		}
	} else{
		$("#errorAlert").html('');
		$("#errorAlert").html("Please enter all mandatory details !!");
		$("#errorAlert").show();
		event.preventDefault();
	}
});

$("#forgotPassowrd").submit(function(event){
	if($("#email").val()){
		var email =  $("#email").val();
		if(!checkEmail(email)){
			$("#errorAlert").html('');
			$("#errorAlert").html("Please enter valid email address !!");
			$("#errorAlert").show();
			event.preventDefault();
		} else{
			$("#errorAlert").html('');
			$("#errorAlert").hide();
			return;
		}
	} else {
		$("#errorAlert").html('');
		$("#errorAlert").html("Please enter Email Address");
		$("#errorAlert").show();
		event.preventDefault();
	}
});

$("#resetPassword").submit(function(event){
	if($("#password").val() && $("#reEnteredPassword").val()){
		var password = $("#password").val();
		var reEnteredPassword = $("#reEnteredPassword").val();
		if(!checkPassword(password)){
			$("#errorAlert").html('');
			$("#errorAlert").html("Please enter valid password !!");
			$("#errorAlert").show();
			event.preventDefault();
		} else{
			if(password == reEnteredPassword){
				$("#errorAlert").html('');
				$("#errorAlert").hide();
				return;
			} else{
				$("#errorAlert").html('');
				$("#errorAlert").html("Password and Re-entered password does not match !!");
				$("#errorAlert").show();
				event.preventDefault();
			}
		}
	} else{
		$("#errorAlert").html('');
		$("#errorAlert").html("Please enter all mandatory details !!");
		$("#errorAlert").show();
		event.preventDefault();
	}
});

$("#tradesmanForm").submit(function(event){
	if($("#name").val() && $("#companyName").val() && $("#email").val() && $("#password").val() && $("#reEnteredPassword").val()){
		var email =  $("#email").val();
		var password = $("#password").val();
		var reEnteredPassword = $("#reEnteredPassword").val();
		if(!checkEmail(email)){
			$("#errorAlert").html('');
			$("#errorAlert").html("Please enter valid email address !!");
			$("#errorAlert").show();
			event.preventDefault();
		} else{
			if(!checkPassword(password)){
				$("#errorAlert").html('');
				$("#errorAlert").html("Please enter valid password !!");
				$("#errorAlert").show();
				event.preventDefault();
			} else{
				if(password == reEnteredPassword){
					$("#errorAlert").html('');
					$("#errorAlert").hide();
					return;
				} else{
					$("#errorAlert").html('');
					$("#errorAlert").html("Password and Re-entered password does not match !!");
					$("#errorAlert").show();
					event.preventDefault();
				}
			}
		}
	} else{
		$("#errorAlert").html('');
		$("#errorAlert").html("Please enter all mandatory details !!");
		$("#errorAlert").show();
		event.preventDefault();
	}
});

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#blah')
                .attr('src', e.target.result)
                .width('100%')
                .height('100%');
            $('#showProfilePicture').hide();
            $('#showPreview').show();
        };

        reader.readAsDataURL(input.files[0]);
    }
}

function removeUploadedImage(){
	$("#featuredImageFile").val('');
	$('#showPreview').hide();
	$('#showProfilePicture').show();
}

function openEditJobModal(jobId){
	if(jobId){
		$("#editModal").modal('show');
		$("#refreshThisDiv").html('');
		var max = $("#_max").val();
		var offset = $("#_offset").val();
		var sort = $("#_sort").val();
		var order = $("#_order").val();
		$.ajax({
			type:'get',
			url:'/tradesman/getJobById?id='+jobId,
			data:{max:max, offset:offset, sort:sort, order:order},
			success:function(data){
				$("#refreshThisDiv").html(data);
				$('#date_2 .input-group.date').datepicker({
					todayBtn: "linked",
			        keyboardNavigation: false,
			        forceParse: false,
			        autoclose: true,
			        format: 'dd MM yyyy'
			    });
			}
		});
	}
}

function openCreateInvoiceModal(jobId){
	if(jobId){
		$("#invoiceModal").modal('show');
		$("#refreshThisDiv_invoice").html('');
		var max = $("#_max").val();
		var offset = $("#_offset").val();
		var sort = $("#_sort").val();
		var order = $("#_order").val();
		$.ajax({
			type:'get',
			url:'/tradesman/getJobByIDForInvoice?id='+jobId,
			data:{max:max, offset:offset, sort:sort, order:order},
			success:function(data){
				$("#refreshThisDiv_invoice").html(data);
			}
		});
	}
}

function openInvoiceModal(jobId){
	if(jobId){
		$("#viewInvoiceModal").modal('show');
		$("#refreshThisDiv_viewInvoice").html('');
		$.ajax({
			type:'get',
			url:'/tradesman/viewInvoiceById?id='+jobId,
			success:function(data){
				$("#refreshThisDiv_viewInvoice").html(data);
			}
		});
	}
}

function viewInvoice(invoiceId){
	if(invoiceId){
		$("#viewInvoiceModal").modal('show');
		$("#refreshThisDiv_viewInvoice").html('');
		$.ajax({
			type:'get',
			url:'/tradesman/viewInvoice?id='+invoiceId,
			success:function(data){
				$("#refreshThisDiv_viewInvoice").html(data);
			}
		});
	}
}

function openInvoiceModalForCustomer(jobId, role){
	if(jobId){
		$("#viewInvoiceModal").modal('show');
		$("#refreshThisDiv_viewInvoice").html('');
		if(role=="ROLE_CUSTOMER"){
			$.ajax({
				type:'get',
				url:'/customer/viewInvoiceById?id='+jobId,
				success:function(data){
					$("#refreshThisDiv_viewInvoice").html(data);
				}
			});
		} else if(role=="ROLE_FACEBOOK_CUSTOMER"){
			$.ajax({
				type:'get',
				url:'/facebookUser/viewInvoiceById?id='+jobId,
				success:function(data){
					$("#refreshThisDiv_viewInvoice").html(data);
				}
			});
		} else if(role=="ROLE_LINKEDIN_CUSTOMER"){
			$.ajax({
				type:'get',
				url:'/linkedInUser/viewInvoiceById?id='+jobId,
				success:function(data){
					$("#refreshThisDiv_viewInvoice").html(data);
				}
			});
		}
	}
}

function openInvoiceModalForFacebookUser(jobId){
	if(jobId){
		$("#viewInvoiceModal").modal('show');
		$("#refreshThisDiv_viewInvoice").html('');
		$.ajax({
			type:'get',
			url:'/facebookUser/viewInvoiceById?id='+jobId,
			success:function(data){
				$("#refreshThisDiv_viewInvoice").html(data);
			}
		});
	}
}

function openInvoiceModalForLinkedInUser(jobId){
	if(jobId){
		$("#viewInvoiceModal").modal('show');
		$("#refreshThisDiv_viewInvoice").html('');
		$.ajax({
			type:'get',
			url:'/linkedInUser/viewInvoiceById?id='+jobId,
			success:function(data){
				$("#refreshThisDiv_viewInvoice").html(data);
			}
		});
	}
}

function editInvoice(invoiceId, jobId){
	if(invoiceId && jobId){
		$("#viewInvoiceModal").modal('hide');
		$("#refreshThisDiv_editInvoice").html('');
		var max = $("#_max").val();
		var offset = $("#_offset").val();
		var sort = $("#_sort").val();
		var order = $("#_order").val();
		$.ajax({
			type:'get',
			url:'/tradesman/getInvoiceInstanceForEdit?invoiceId='+invoiceId+'&jobId='+jobId,
			data:{max:max, offset:offset, sort:sort, order:order},
			success:function(data){
				$("#editInvoiceModal").modal('show');
				$("#refreshThisDiv_editInvoice").html(data);
			}
		});
	}
}

function openFeedbackModal(jobId){
	if(jobId){
		$("#refreshThisDiv_feedback").html('');
		var max = $("#_max").val();
		var offset = $("#_offset").val();
		var sort = $("#_sort").val();
		var order = $("#_order").val();
		$.ajax({
			type:'get',
			url:'/jobs/viewFeedbackForm?jobId='+jobId,
			data:{max:max, offset:offset, sort:sort, order:order},
			success:function(data){
				$("#feedbackModal").modal('show');
				$("#refreshThisDiv_feedback").html(data);
			}
		});
	}
}

function formatThisNumber(id){
	var val = $("#"+id).val();
	var formattedVal = parseFloat(val).toFixed(2);
	$("#"+id).val(formattedVal);
}

function toggleDiv(divId){
	if($("#"+divId).hasClass('hideMe')){
		$("#"+divId).removeClass('hideMe');
	} else{
		$("#"+divId).addClass('hideMe');
	}
}