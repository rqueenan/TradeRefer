<sec:access expression="hasRole('ROLE_CUSTOMER')">
	<meta name="layout" content="customer_main" />
</sec:access>
<sec:access expression="hasRole('ROLE_FACEBOOK_CUSTOMER')">
	<meta name="layout" content="facebook_main" />
</sec:access>
<sec:access expression="hasRole('ROLE_LINKEDIN_CUSTOMER')">
	<meta name="layout" content="linkedIn_main" />
</sec:access>
<style type="text/css">

#addressBookWidgetDiv {
  min-width: 300px; 
  width: 600px;  /*width can be removed and the widget will take up all the page width*/
  max-width: 100%;
  min-height: 228px; 
  height: 240px;
}
#walletWidgetDiv {
  min-width: 300px; 
  width: 600px;  /*width can be removed and the widget will take up all the page width*/
  max-width: 100%;
  min-height: 228px; 
  height: 240px;
}
</style>
<script type="text/javascript">
  window.onAmazonLoginReady = function() {
    amazon.Login.setClientId('${tradesman?.amazon_clientId}'); 
  };

  window.onAmazonPaymentsReady = function() {
	  var orderReferenceId = null;

	    new OffAmazonPayments.Widgets.Wallet({ 
	      sellerId: '${tradesman?.amazon_merchantId}',
	      // Add the onOrderReferenceCreate function to 
	      // generate an Order Reference ID. 
	      onOrderReferenceCreate: function(orderReference) {
	        // Use the following cod to get the generated Order Reference ID.
	        orderReferenceId = orderReference.getAmazonOrderReferenceId();
	        console.log(orderReference);
	      },
	      design: {
	        designMode: 'responsive'
	      },
	      onPaymentSelect: function(orderReference) {
	    	  $("#orderRefId").val(orderReferenceId);
	      },
	      onError: function(error) {
	        // Your error handling code.
	        // During development you can use the following
	        // code to view error messages:
	         console.log(error.getErrorCode() + ': ' + error.getErrorMessage());
	        // See "Handling Errors" for more information.
	      }
	    }).bind('walletWidgetDiv');
  };

	function disableCancelButton(){
		$('.cancelPayment').bind('click', function(e){
	        e.preventDefault();
		})
	}
</script>
<script async="async" type="text/javascript"
src='https://static-eu.payments-amazon.com/OffAmazonPayments/gbp/sandbox/lpa/js/Widgets.js'>
</script>
<!-- Place this code in your HTML where you would like the wallet widget to appear. -->
<div class="row page-wrapper">
	<div class="col-md-12">
	<p style="margin-left: 20%;"><label>Buyer Name : &nbsp;</label>${buyerName}</p>
	<p style="margin-left: 20%;"><label>Buyer Email : &nbsp;</label>${buyerEmail}</p>
	<p style="margin-left: 20%; margin-right: 24%;"><label>Buyer User Id : &nbsp;</label><span>${userId}</span><span style="float: right;"><label>Amount : &nbsp;</label>&pound; ${invoice?.totalPrice}</span></p>
	<div id="walletWidgetDiv" style="margin-left: 20%;"> </div>
	<div class="row" style="margin-top: 1%;">
	<div>
		<form action="/amazonPay/approve" style="    float: right;margin-right: 24%;">
			<input type="text" name="orderRefId" id="orderRefId" value="" style="display: none;">
			<input type="text" name="amount" id="amount" value="${invoice?.totalPrice}" style="display: none;">
			<input type="text" name="invoiceId" value="${invoice?.id}" style="display: none;">
			<input type="text" name="tradesmanId" value="${tradesman?.id}" style="display: none;">
			<g:link controller="amazonPay" class="btn btn-warning cancelPayment" action="cancel" id="${invoice?.id}">Cancel Payment</g:link>
			<button type="submit" class="btn btn-success" onclick="disableCancelButton();">Pay Invoice</button>
		</form>
	</div>
</div>
</div>
</div>