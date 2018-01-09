<div class="modal-body col-md-12" style="box-shadow: 0 rgba(0, 0, 0, 0.3);">
	<div class="col-md-12">
		<div class="alert alert-danger alignCenter" role="alert" id="errorAlert_editJob" style="padding: 0px; display: none;">${flash.message}</div>
	</div>
	<form name="editJob" action="/jobs/updateJobTradesman/${job?.id}" id="editJob">
		<g:hiddenField id="edit_job_max" value="${max}" name="max" />
		<g:hiddenField id="edit_job_offset" value="${offset}" name="offset" />
		<g:hiddenField id="edit_job_sort" value="${sort}" name="sort" />
		<g:hiddenField id="edit_job_order" value="${order}" name="order" />
		<div class="form-group col-md-6 required">
			<label class='control-label'>Customer Name</label> 
			<input type="text" placeholder="Customer Name" name="customerName" id="customerName_edit" class="form-control" value="${job?.customerName}" required>
		</div>
		<div class="form-group col-md-6 required">
			<label class='control-label'>Customer Email</label> 
			<input type="email" placeholder="Customer Email" name="customerEmail" id="customerEmail_edit" value="${job?.customerEmail}" class="form-control" required>
		</div>
		<input type="hidden" name="tradesmanId_edit" id="tradesmanId_edit" value="${tradesmanInstance?.id}">
		<div class="col-md-12 hr-line-dashed" style="margin-top: 0px;"></div>
		<div class="col-md-12">
        	<h4 class="m-t-none m-b">Customer Address</h4>
        </div>
		<div class="form-group col-md-6 required">
			<label class='control-label'>Address Line 1</label> 
			<input type="text" placeholder="Address Line 1" name="addressLine1" id="addressLine1_edit" value="${job?.customerAddress1}" class="form-control" required>
		</div>
		<div class="form-group col-md-6">
			<label>Address Line 2</label>
			<input type="text" placeholder="Address Line 2" name="addressLine2" id="addressLine2_edit" value="${job?.customerAddress2}" class="form-control">
		</div>
		<div class="form-group col-md-6">
			<label>Address Line 3</label>
			<input type="text" placeholder="Address Line 3" name="addressLine3" id="addressLine3_edit" value="${job?.customerAddress3}" class="form-control">
		</div>
		<div class="form-group col-md-6 required">
			<label class='control-label'>Post Code</label>
			<input type="text" placeholder="Post Code" name="postcode" id="postcode_edit" value="${job?.customerPostcode}" class="form-control" required>
		</div>
		<div class="col-md-12 hr-line-dashed" style="margin-top: 0px;"></div>
		<div class="form-group col-md-6 " id="date_2">
			<label>Job Date</label>
			<div class="input-group date">
				<span class="input-group-addon"><i class="fa fa-calendar"></i></span><input type="text" name="jobDate_edit" class="form-control" value="${job_date}" >
			</div>
		</div>
		<div class="form-group col-md-6 required">
			<label class='control-label'>Job Type</label>
			<g:select class="form-control chosen-select" value="${job?.jobType}" name="jobType" from="${tradesmanInstance?.workType}" noSelection="['':'-Choose Job Type-']" required="required" />
		</div>
		<div class="col-md-12 hr-line-dashed"></div>
		<div class="col-md-12">
			<h4 class="m-t-none m-b">Parts</h4>
		</div>
		<div class="form-group col-md-4">
			<label>Description</label>
		</div>
		<div class="form-group col-md-3">
			<label>Quantity</label>
		</div>
		<div class="form-group col-md-4">
			<label>Unit Price</label>
		</div>
		<div class="col-md-1">
			&nbsp;
		</div>
		<div id="field_edit_job">
			<g:if test="${job?.parts?.size() > 0}">
				<g:each in="${job?.parts.sort{it.id}}" var="part" status="i">
					<div id="field_edit_job${i}">
						<div class="form-group col-md-4">
							<input id="part_name_edit_job${i}" name="part_name${i}" type="text" value="${part?.name}" placeholder="Part Name"  class="form-control input-md">
						</div>
						<div class="form-group col-md-3">
							<input id="qty_edit_job${i}" name="qty${i}" min="0" type="number" placeholder="Qty" value="${part?.quantity}" class="form-control input-md">
						</div>
						<div class="form-group col-md-4">
							<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input min="0" id="unit_price_edit_job${i}" style="display: inline; width: 88%;" name="unit_price${i}" type="number" step=any placeholder="Unit Price" onchange="formatThisNumber('unit_price_edit_job${i}');" value="${part?.pricePerUnit}" class="form-control input-md">
						</div>
					</div>
					<div id="btnDiv${i}" class="col-md-1">
						<button id="remove${i}" class="btn btn-danger remove-me" >-</button>
					</div>
				</g:each>
				<div id="field_edit_job${job?.parts?.size()}">
					<div class="form-group col-md-4">
						<input id="part_name_edit_job${job?.parts?.size()}" name="part_name${job?.parts?.size()}" type="text" placeholder="Part Name"  class="form-control input-md">
					</div>
					<div class="form-group col-md-3">
						<input id="qty_edit_job${job?.parts?.size()}" min="0" name="qty${job?.parts?.size()}" type="number" placeholder="Qty" value="0" class="form-control input-md">
					</div>
					<div class="form-group col-md-4">
						<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input min="0" id="unit_price_edit_job${job?.parts?.size()}" onchange="formatThisNumber('unit_price_edit_job${job?.parts?.size()}');" style="display: inline; width: 88%;" name="unit_price${job?.parts?.size()}" type="number" step=any placeholder="Unit Price" value="0" class="form-control input-md">
					</div>
				</div>
			</g:if>
			<g:else>
				<div id="field_edit_job0">
					<div class="form-group col-md-4">
						<input id="part_name_edit_job0" name="part_name0" type="text" placeholder="Part Name"  class="form-control input-md">
					</div>
					<div class="form-group col-md-3">
						<input id="qty_edit_job0" name="qty0" type="number" min="0" placeholder="Qty" value="0" class="form-control input-md">
					</div>
					<div class="form-group col-md-4">
						<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input min="0" id="unit_price_edit_job0" onchange="formatThisNumber('unit_price_edit_job0');" style="display: inline; width: 88%;" name="unit_price0" type="number" step=any placeholder="Unit Price" value="0" class="form-control input-md" >
					</div>
				</div>
			</g:else>
		</div>
		<div class="col-md-1">
			<button id="add-more-button" name="add-more" class="btn btn-primary">+</button>
		</div>
		<div class="col-md-12 hr-line-dashed"></div>
		<div class="col-md-12">
			<h4 class="m-t-none m-b">Labour</h4>
		</div>
		<div class="form-group col-md-4 required">
			<labe class='control-label'l>Hours</label>
		</div>
		<div class="form-group col-md-4 required">
			<label class='control-label'>Hourly Rate</label>
		</div>
		<div class="form-group col-md-4 required">
			<label class='control-label'>Labour Cost</label>
		</div>
		<div class="form-group col-md-4">
			<input type="number" min="0" placeholder="No." value="${job?.laborHours}" id="laborHours_edit" name="noOfHours" class="form-control" onchange="calculateTotalLaborCost('${tradesmanInstance?.hourlyRate}','edit');" required="required">
		</div>
		<div class="form-group col-md-4">
			<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input step=".01" style="display: inline; width: 90%;" min="0" type="number" placeholder="Per Hour" id="perHour_invoice" value="${hourlRate2D}" name="perHour_invoice" class="form-control" required="required" disabled="disabled">
		</div>
		<div class="form-group col-md-4"> 
			<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input step=".01" value="${job?.laborCost}" style="display: inline; width: 90%;" min="0" type="number" placeholder="Cost" id="laborTotalCost_edit" name="costOfHours" class="form-control" onload="formatThisNumber('laborTotalCost_edit');" onchange="formatThisNumber('laborTotalCost_edit');" required="required">
		</div>
		
		<div class="col-md-12 hr-line-dashed"></div>
		<div class="col-md-12">
			<h4 class="m-t-none m-b">Job Description</h4>
		</div>
		<div class="form-group col-md-12"> 
			<textarea maxlength="255" placeholder="Job Description" name="jobDesc" class="form-control" rows="4" >${job?.jobDescription}</textarea>
		</div>
		<div class="col-md-12" style="text-align: right;">
			<button type="button" class="btn btn-white" data-dismiss="modal">Close</button>
			<button type="button" onclick="validateJobEditEmail();" class="btn btn-primary">Save changes</button>
		</div>	
		<input type="hidden" id="edit_count" name="count" value="${job?.parts?.size()}">
	</form>
</div>
<div class="modal-footer"></div>
<script type="text/javascript">
$("#customerEmail_edit").autocomplete({
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
			$("#customerName_edit").val(ui.item.name);
			$("#customerEmail_edit").val(ui.item.email);
			$("#addressLine1_edit").val(ui.item.addressLine1);
			$("#addressLine2_edit").val(ui.item.addressLine2);
			$("#addressLine3_edit").val(ui.item.addressLine3);
			$("#postcode_edit").val(ui.item.postcode);
		}
	}
});

$("#customerName_edit").autocomplete({
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
			$("#customerName_edit").val(ui.item.name);
			$("#customerEmail_edit").val(ui.item.email);
			$("#addressLine1_edit").val(ui.item.addressLine1);
			$("#addressLine2_edit").val(ui.item.addressLine2);
			$("#addressLine3_edit").val(ui.item.addressLine3);
			$("#postcode_edit").val(ui.item.postcode);
		}
	}
});
$(document).ready(function () {
	var next_edit = parseInt($("#edit_count").val());
	$("#add-more-button").click(function(e){
	    e.preventDefault();
	    var addto = "#field_edit_job" + next_edit;
	    var addRemove = "#field_edit_job" + (next_edit);
	    next_edit = next_edit + 1;
	    var newIn = '<div id="field_edit_job'+ next_edit +'" name="field_edit_job'+ next_edit +'"><div class="form-group col-md-4"><input id="part_name_edit_job'+ next_edit +'" name="part_name'+ next_edit +'" type="text" placeholder="Part Name" class="form-control input-md"></div><div class="form-group col-md-3"><input min="0" id="qty_edit_job'+ next_edit +'" name="qty'+ next_edit +'" type="number" placeholder="Qty" value="0" class="form-control input-md"></div><div class="form-group col-md-4"><span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input min="0" id="unit_price_edit_job'+ next_edit +'" onchange="formatThisNumber(\'unit_price_edit_job'+ next_edit +'\');" name="unit_price'+ next_edit +'" type="number" style="display: inline; width: 88%;" placeholder="Unit Price" value="0.00" class="form-control input-md"></div></div>';
	    var newInput = $(newIn);
	    var removeBtn = '<div id="btnDiv'+(next_edit - 1)+'" class="col-md-1"><button id="remove' + (next_edit - 1) + '" class="btn btn-danger remove-me" >-</button></div>';
	    var removeButton = $(removeBtn);
	    $(addto).after(newInput);
	    $(addRemove).after(removeButton);
	    $("#field_edit_job" + next_edit).attr('data-source',$(addto).attr('data-source'));
	    $("#edit_count").val(next_edit);
	    $('.remove-me').click(function(e){
	        e.preventDefault();
	        var fieldNum = this.id.replace("remove", "");
	        //var fieldNum = this.id.charAt(this.id.length-1);
	        var fieldID = "#field_edit_job" + fieldNum;
	        var btnDiv = "#btnDiv" + fieldNum;
	        $(fieldID).remove();
	        $(btnDiv).remove();
	    });
	});
	$('.remove-me').click(function(e){
	    e.preventDefault();
	    var fieldNum = this.id.replace("remove", "");
	    //var fieldNum = this.id.charAt(this.id.length-1);
	    var fieldID = "#field_edit_job" + fieldNum;
	    var btnDiv = "#btnDiv" + fieldNum;
	    $(fieldID).remove();
	    $(btnDiv).remove();
	});
	formatThisNumber('laborTotalCost_edit');
	if(next_edit == 0){
		formatThisNumber('unit_price_edit_job0');
	}
	var totalParts = next_edit + 1;
	for(var i=0; i < totalParts; i++){
		formatThisNumber('unit_price_edit_job'+i);
	}
});
</script>