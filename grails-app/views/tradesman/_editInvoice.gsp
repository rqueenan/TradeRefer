<style>
	#field_edit_invoice input{
		padding: 5px 5px !Important;
	}
</style>
<div class="modal-body col-md-12" style="box-shadow: box-shadow: 0 rgba(0, 0, 0, 0.3);">
	<g:form name="invoiceEdit" controller="jobs" action="editInvoice" id="${invoiceInstance?.id}">
		<g:hiddenField id="edit_invoice_max" value="${max}" name="max" />
		<g:hiddenField id="edit_invoice_offset" value="${offset}" name="offset" />
		<g:hiddenField id="edit_invoice_sort" value="${sort}" name="sort" />
		<g:hiddenField id="edit_invoice_order" value="${order}" name="order" />
		<div class="col-md-12">
			<h4 class="m-t-none m-b">Labour</h4>
		</div>
		<div class="form-group col-md-4">
			<label>Labour Hours</label>
		</div>
		<div class="form-group col-md-4">
			<label>Per Hour Cost</label>
		</div>
		<div class="form-group col-md-4">
			<label>Total Labour Cost</label>
		</div>
		<div class="form-group col-md-4">
			<input type="number" placeholder="No." id="laborHours_invoice" value="${job?.laborHours}" name="laborHours_invoice" class="form-control" onchange="calculateLabor('${tradesmanInstance?.hourlyRate}'); calculateTotal();" required="required">
		</div>
		<div class="form-group col-md-4">
			<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input style="display: inline; width: 90%;" type="number" placeholder="Per Hour" id="perHour_invoice" value="${hourlRate2D}" name="perHour_invoice" class="form-control" required="required" disabled="disabled">
		</div>
		<div class="form-group col-md-4"> 
			<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input style="display: inline; width: 90%;" type="number" placeholder="Cost" id="laborCost_invoice" onchange="formatThisNumber('laborCost_invoice'); calculateTotal();" value="${job?.laborCost}" name="laborCost_invoice" class="form-control" required="required">
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
		<div id="field_edit_invoice">
			<g:if test="${job?.parts?.size() > 0}">
				<g:each in="${job?.parts.sort{it.id}}" var="part" status="i">
					<div id="field_edit_${i}">
						<div class="form-group col-md-4">
							<input id="part_name${i}" name="part_name${i}" type="text" value="${part?.name}" placeholder="Part Name"  class="form-control input-md">
						</div>
						<div class="form-group col-md-3">
							<input min="0" id="qty_edit_${i}" name="qty${i}" type="number" placeholder="Qty" value="${part?.quantity}" class="form-control input-md" onchange="calculateTotal();">
						</div>
						<div class="form-group col-md-4">
							<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input min="0" id="unit_price_edit${i}" style="display: inline; width: 88%;" name="unit_price${i}" type="number" step=any placeholder="Unit Price" value="${part?.pricePerUnit}" class="form-control input-md" onchange="formatThisNumber('unit_price_edit${i}'); calculateTotal();" >
						</div>
					</div>
					<div id="btnDiv${i}" class="col-md-1"     margin-bottom: 5%;>
						<button id="remove${i}" class="btn btn-danger remove-me" >-</button>
					</div>
				</g:each>
				<div id="field_edit_${job?.parts?.size()}">
					<div class="form-group col-md-4">
						<input id="part_name${job?.parts?.size()}" name="part_name${job?.parts?.size()}" type="text" placeholder="Part Name"  class="form-control input-md">
					</div>
					<div class="form-group col-md-3">
						<input min="0" id="qty_edit_${job?.parts?.size()}" name="qty${job?.parts?.size()}" type="number" placeholder="Qty" value="0" class="form-control input-md" onchange="calculateTotal();">
					</div>
					<div class="form-group col-md-4">
						<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input min="0" id="unit_price_edit${job?.parts?.size()}" style="display: inline; width: 88%;" name="unit_price${job?.parts?.size()}" type="number" step=any placeholder="Unit Price" value="0.00" class="form-control input-md" onchange="formatThisNumber('unit_price_edit${job?.parts?.size()}'); calculateTotal();" >
					</div>
				</div>
			</g:if>
			<g:else>
				<div id="field_edit_0">
					<div class="form-group col-md-4">
						<input id="part_name0" name="part_name0" type="text" placeholder="Part Name"  class="form-control input-md">
					</div>
					<div class="form-group col-md-3">
						<input id="qty_edit_0" min="0" name="qty0" type="number" placeholder="Qty" value="0" class="form-control input-md" onchange="calculateTotal();">
					</div>
					<div class="form-group col-md-4">
						<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input min="0" id="unit_price_edit0" style="display: inline; width: 88%;" name="unit_price0" type="number" step=any placeholder="Unit Price" value="0.00" class="form-control input-md" onchange="formatThisNumber('unit_price_edit0'); calculateTotal();" >
					</div>
				</div>
			</g:else>
		</div>
		<div class="col-md-1">
			<button id="add-more-edit-invoice" name="add-more" class="btn btn-primary">+</button>
		</div>
		<div class="col-md-4"></div>
		<div class="col-md-3"></div>
		<div class="form-group col-md-4">
			<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input min="0" type="number" placeholder="Total Cost" style="display: inline; width: 88%;" id="totalPartsCost" class="form-control" disabled="disabled" />
		</div>
		<div class="col-md-1"></div>
		<div class="col-md-12 hr-line-dashed"></div>
		<div class="col-md-12">
			<h4 class="m-t-none m-b">Invoice Total</h4>
		</div>
		<div class="form-group col-md-4"> 
			<label>VAT (%)</label><br/>
			<input style="width: 70%;" type="number" min="0" step=any placeholder="VAT" id="vat" value="${setting?.VAT}" name="vat" onchange="calculateTotal();" class="form-control" disabled="disabled">
		</div>
		<div class="form-group col-md-4"> 
			<label>Discount</label><br/>
			<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input min="0" type="number" step=any placeholder="Discount" id="discount" style="display: inline; width: 70%;" value="${invoiceInstance?.discount}" name="discount" onchange="formatThisNumber('discount'); calculateTotal();" class="form-control">
		</div>
		<div class="form-group col-md-4"> 
			<label>Total</label><br/>
			<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input min="0" type="number" step=any placeholder="Total Amount" id="total" style="display: inline; width: 90%;" value="${invoiceInstance?.totalPrice}" name="total" onchange="formatThisNumber('total');" class="form-control">
		</div>
		<input type="hidden" id="count_edit_invoice" name="count" value="${job?.parts?.size()}">
		<div class="col-md-12" style="text-align: right;">
			<button type="button" class="btn btn-white" data-dismiss="modal">Close</button>
			<button type="submit" class="btn btn-primary">Update Invoice</button>
		</div>
	</g:form>
</div>
<div class="modal-footer"></div>
<script type="text/javascript">
$(document).ready(function () {
    var next = parseInt(${job?.parts?.size()});
    $("#add-more-edit-invoice").click(function(e){
        e.preventDefault();
        var addto = "#field_edit_" + next;
        var addRemove = "#field_edit_" + (next);
        next = next + 1;
        var newIn = '<div id="field_edit_'+ next +'" name="field_edit_'+ next +'"><div class="form-group col-md-4"><input id="part_name'+ next +'" name="part_name'+ next +'" type="text" placeholder="Part Name" class="form-control input-md"></div><div class="form-group col-md-3"><input min="0" id="qty_edit_'+ next +'" name="qty'+ next +'" type="number" onchange="calculateTotal();" placeholder="Qty" value="0" class="form-control input-md"></div><div class="form-group col-md-4"><span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input id="unit_price_edit'+ next +'" name="unit_price'+ next +'" type="number" style="display: inline; width: 88%;" onchange="formatThisNumber(\'unit_price_edit'+ next +'\'); calculateTotal();" placeholder="Unit Price" min="0" value="0.00" class="form-control input-md"></div></div>';
        var newInput = $(newIn);
        var removeBtn = '<div style="    margin-bottom: 5%;" id="btnDiv'+(next - 1)+'" class="col-md-1"><button id="remove' + (next - 1) + '" class="btn btn-danger remove-me" >-</button></div>';
        var removeButton = $(removeBtn);
        $(addto).after(newInput);
        $(addRemove).after(removeButton);
        $("#field_edit_" + next).attr('data-source',$(addto).attr('data-source'));
        $("#count_edit_invoice").val(next);
        $('.remove-me').click(function(e){
            e.preventDefault();
            var fieldNum = this.id.replace("remove", "");
            //var fieldNum = this.id.charAt(this.id.length-1);
            var fieldID = "#field_edit_" + fieldNum;
            var btnDiv = "#btnDiv" + fieldNum;
            $(fieldID).remove();
            $(btnDiv).remove();
            calculateTotal();
        });
    });
    $('.remove-me').click(function(e){
        e.preventDefault();
        var fieldNum = this.id.replace("remove", "");
        //var fieldNum = this.id.charAt(this.id.length-1);
        var fieldID = "#field_edit_" + fieldNum;
        var btnDiv = "#btnDiv" + fieldNum;
        $(fieldID).remove();
        $(btnDiv).remove();
        calculateTotal();
    });
    calculateTotal();
    formatThisNumber('laborCost_invoice');
    formatThisNumber('discount');
    formatThisNumber('total');
    var totalParts = next + 1;
	for(var i=0; i < totalParts; i++){
		formatThisNumber('unit_price_edit'+i);
	}
});
function calculateLabor(hourlyRate){
	var laborHrs = $("#laborHours_invoice").val();
	var laborCost = laborHrs * hourlyRate;
	$("#laborCost_invoice").val(laborCost);
	calculateTotal();
}

function calculateTotal(){
	var totalCost = 0;
	var laborCost = parseFloat($("#laborCost_invoice").val());
	totalCost = laborCost;
	var totalParts = parseInt($("#count_edit_invoice").val()) + 1;
	var totalPartsCost = 0;
	for(var i=0; i < totalParts; i++){
		var elementExists = document.getElementById("unit_price_edit"+i);
		if(elementExists != null){
			totalPartsCost = totalPartsCost + (parseFloat($("#unit_price_edit"+i).val()) * parseFloat($("#qty_edit_"+i).val()));
			totalCost = totalCost + (parseFloat($("#unit_price_edit"+i).val()) * parseFloat($("#qty_edit_"+i).val()));
		}
	}
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
	$("#total").val(parseFloat(totalCost).toFixed(2));
}
</script>