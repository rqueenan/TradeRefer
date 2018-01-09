<style>
	#field_invoice input{
		padding: 5px 5px !Important;
	}
</style>
<div class="modal-body col-md-12" style="box-shadow: box-shadow: 0 rgba(0, 0, 0, 0.3);">
	<g:form name="invoiceCreation" controller="jobs" action="updateInvoice" id="${job?.id}">
		<g:hiddenField id="create_invoice_max" value="${max}" name="max" />
		<g:hiddenField id="create_invoice_offset" value="${offset}" name="offset" />
		<g:hiddenField id="create_invoice_sort" value="${sort}" name="sort" />
		<g:hiddenField id="create_invoice_order" value="${order}" name="order" />
		<div class="col-md-12">
			<h4 class="m-t-none m-b">Labour</h4>
		</div>
		<div class="form-group col-md-4">
			<label>Hours</label>
		</div>
		<div class="form-group col-md-4">
			<label>Hourly Rate</label>
		</div>
		<div class="form-group col-md-4">
			<label>Total Cost</label>
		</div>
		<div class="form-group col-md-4">
			<input type="number" min="0" placeholder="No." id="laborHours_invoice" value="${job?.laborHours}" name="laborHours_invoice" class="form-control" onchange="calculateLabor('${tradesmanInstance?.hourlyRate}'); calculateTotal();" required="required">
		</div>
		<div class="form-group col-md-4">
			<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input step=".01" style="display: inline; width: 90%;" min="0" type="number" placeholder="Per Hour" id="perHour_invoice" value="${hourlRate2D}" name="perHour_invoice" class="form-control" onchange="calculateLabor('${tradesmanInstance?.hourlyRate}');" required="required" disabled="disabled">
		</div>
		<div class="form-group col-md-4"> 
			<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input step=".01" style="display: inline; width: 90%;" min="0" type="number" placeholder="Cost" id="laborCost_invoice_create" value="${job?.laborCost}" name="laborCost_invoice" class="form-control" onchange="formatThisNumber('laborCost_invoice_create'); calculateTotal();" required="required">
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
		<div id="field_invoice">
			<g:if test="${job?.parts?.size() > 0}">
				<g:each in="${job?.parts.sort{it.id}}" var="part" status="i">
					<div id="field_${i}">
						<div class="form-group col-md-4">
							<input id="part_name${i}" name="part_name${i}" type="text" value="${part?.name}" placeholder="Part Name"  class="form-control input-md">
						</div>
						<div class="form-group col-md-3">
							<input id="qty_invoice_${i}" min="0" name="qty${i}" type="number" placeholder="Qty" value="${part?.quantity}" class="form-control input-md" onchange="calculateTotal();">
						</div>
						<div class="form-group col-md-4">
							<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input min="0" id="unit_price_invoice_${i}" style="display: inline; width: 88%;" name="unit_price${i}" type="number" step=any placeholder="Unit Price" value="${part?.pricePerUnit}" class="form-control input-md" onchange="formatThisNumber('unit_price_invoice_${i}'); calculateTotal();" >
						</div>
					</div>
					<div id="btnDiv${i}" class="col-md-1" style="    margin-bottom: 5%;">
						<button id="remove${i}" class="btn btn-danger remove-me" >-</button>
					</div>
					
				</g:each>
				<div id="field_${job?.parts?.size()}">
					<div class="form-group col-md-4">
						<input id="part_name${job?.parts?.size()}" name="part_name${job?.parts?.size()}" type="text" placeholder="Part Name"  class="form-control input-md">
					</div>
					<div class="form-group col-md-3">
						<input min="0" id="qty_invoice_${job?.parts?.size()}" name="qty${job?.parts?.size()}" type="number" placeholder="Qty" value="0" class="form-control input-md" onchange="calculateTotal();">
					</div>
					<div class="form-group col-md-4">
						<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input min="0" id="unit_price_invoice_${job?.parts?.size()}" style="display: inline; width: 88%;" name="unit_price${job?.parts?.size()}" type="number" step=any placeholder="Unit Price" value="0" class="form-control input-md" onchange="formatThisNumber('unit_price_invoice_${job?.parts?.size()}'); calculateTotal();" >
					</div>
				</div>
			</g:if>
			<g:else>
				<div id="field_0">
					<div class="form-group col-md-4">
						<input id="part_name0" name="part_name0" type="text" placeholder="Part Name"  class="form-control input-md">
					</div>
					<div class="form-group col-md-3">
						<input min="0" id="qty_invoice_0" name="qty0" type="number" placeholder="Qty" value="0" class="form-control input-md" onchange="calculateTotal();">
					</div>
					<div class="form-group col-md-4">
						<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input min="0" id="unit_price_invoice_0" style="display: inline; width: 88%;" name="unit_price0" type="number" step=any placeholder="Unit Price" value="0.00" class="form-control input-md" onchange="formatThisNumber('unit_price_invoice_0'); calculateTotal();" >
					</div>
				</div>
			</g:else>
		</div>
		
		<div class="col-md-1">
			<button id="add-more-create-invoice" name="add-more" class="btn btn-primary">+</button>
		</div>	<br/>
		<div class="col-md-4"></div>
		<div class="col-md-3"></div>
		<div class="form-group col-md-4">
			<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input type="number" placeholder="Total Cost" style="display: inline; width: 88%;" id="totalPartsCost" class="form-control" disabled="disabled" onchange="calculateTotal();" />
		</div>
		<div class="col-md-1"></div>
		
		<div class="col-md-12 hr-line-dashed"></div>
		<div class="col-md-12">
			<h4 class="m-t-none m-b">Invoice Total</h4>
		</div>
		<div class="form-group col-md-4"> 
			<label>VAT (%)</label><br/>
			<input min="0" type="number" step=".01" placeholder="VAT" id="vat" value="${setting?.VAT}" name="vat" onchange="calculateTotal();" style="display: inline; width: 70%;" class="form-control" disabled="disabled">
		</div>
		<div class="form-group col-md-4"> 
			<label>Discount</label><br/>
			<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input step=".01" min="0" type="number"  placeholder="Discount" style="display: inline; width: 70%;" id="discount" value="0" name="discount" onchange="calculateTotal(); formatThisNumber('discount');" class="form-control">
		</div>
		<div class="form-group col-md-4"> 
			<label>Total</label><br/>
			<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input min="0" type="number"  placeholder="Total Amount" style="display: inline; width: 90%;" id="total_createInvoice" onchange="formatThisNumber('total_createInvoice');" value="0" name="total" step=any class="form-control">
		</div>
	
		<input type="hidden" id="count_create_invoice" name="count" value="${job?.parts?.size()}">
		<div class="col-md-12" style="text-align: right;">
			<button type="button" class="btn btn-white" data-dismiss="modal">Close</button>
			<button type="submit" class="btn btn-primary">Create Invoice</button>
		</div>
	</g:form>
</div>
<div class="modal-footer"></div>
<script type="text/javascript">
$(document).ready(function () {
    var next_create_invoice = parseInt(${job?.parts?.size()});
    calculateTotal();
    $("#add-more-create-invoice").click(function(e){
        e.preventDefault();
        var addto = "#field_" + next_create_invoice;
        var addRemove = "#field_" + (next_create_invoice);
        next_create_invoice = next_create_invoice + 1;
        var newIn = '<div id="field_'+ next_create_invoice +'" name="field_'+ next_create_invoice +'"><div class="form-group col-md-4"><input id="part_name'+ next_create_invoice +'" name="part_name'+ next_create_invoice +'" type="text" placeholder="Part Name" class="form-control input-md"></div><div class="form-group col-md-3"><input id="qty_invoice_'+ next_create_invoice +'" name="qty'+ next_create_invoice +'" min="0" type="number" onchange="calculateTotal();" placeholder="Qty" value="0" class="form-control input-md"></div><div class="form-group col-md-4"><span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input  id="unit_price_invoice_'+ next_create_invoice +'" name="unit_price'+ next_create_invoice +'" min="0" type="number" onchange="calculateTotal(); formatThisNumber(\'unit_price_invoice_'+ next_create_invoice +'\');" placeholder="Unit Price" value="0.00" class="form-control input-md" style="display: inline; width: 88%;" step=".01" ></div></div>';
        var newInput = $(newIn);
        var removeBtn = '<div style="    margin-bottom: 5%;" id="btnDiv'+(next_create_invoice - 1)+'" class="col-md-1"><button id="remove' + (next_create_invoice - 1) + '" class="btn btn-danger remove-me" >-</button></div>';
        var removeButton = $(removeBtn);
        $(addto).after(newInput);
        $(addRemove).after(removeButton);
        $("#field_" + next_create_invoice).attr('data-source',$(addto).attr('data-source'));
        $("#count_create_invoice").val(next_create_invoice);  
        
            $('.remove-me').click(function(e){
                e.preventDefault();
                var fieldNum = this.id.replace("remove", "");
                //var fieldNum = this.id.charAt(this.id.length-1);
                var fieldID = "#field_" + fieldNum;
                var btnDiv = "#btnDiv" + fieldNum;
                $(fieldID).remove();
                $(btnDiv).remove();
                calculateTotal();
            });
    });
    formatThisNumber('laborCost_invoice_create');
    formatThisNumber('perHour_invoice');
    formatThisNumber('discount');
    formatThisNumber('total_createInvoice');
    var totalParts = next_create_invoice + 1;
	for(var i=0; i < totalParts; i++){
		formatThisNumber('unit_price_invoice_'+i);
	}
});
function calculateLabor(hourlyRate){
	var laborHrs = $("#laborHours_invoice").val();
	var laborCost = laborHrs * hourlyRate;
	$("#laborCost_invoice_create").val(parseFloat(laborCost).toFixed(2));
	calculateTotal();
}

function calculateTotal(){
	var totalCost = 0;
	var laborCost = parseFloat($("#laborCost_invoice_create").val());
	totalCost = laborCost;
	//alert("labor cost : " + laborCost);
	var totalParts = $("#count_create_invoice").val() + 1;
	var totalPartsCost = 0;
	for(var i=0; i < totalParts; i++){
		var elementExists = document.getElementById("unit_price_invoice_"+i);
		if(elementExists != null){
			totalPartsCost = totalPartsCost + (parseFloat($("#unit_price_invoice_"+i).val()) * parseFloat($("#qty_invoice_"+i).val()));
			totalCost = totalCost + (parseFloat($("#unit_price_invoice_"+i).val()) * parseFloat($("#qty_invoice_"+i).val()));
		}
	}
	//alert("total parts cost : " + totalPartsCost);
	//alert("total cost : " + totalCost);
	var discount = parseFloat($("#discount").val());
	$("#totalPartsCost").val(parseFloat(totalPartsCost).toFixed(2));
	var vat = parseFloat($("#vat").val());
	if(vat % 1 == 0){
		$("#vat").val(Math.round(vat));
	}
	var vatVal = (totalCost * vat)/100;
	totalCost = totalCost + vatVal;
	
	if(discount > 0){
		totalCost = totalCost - discount;
	}
	//alert("total cost with vat : " + totalCost);
	$("#total_createInvoice").val(parseFloat(totalCost).toFixed(2));
}
</script>