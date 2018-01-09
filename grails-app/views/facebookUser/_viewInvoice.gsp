<style type="text/css">
.ibox-content{
border-width: 0px 0 !Important;
}
</style>

		
<script async="async" type='text/javascript' 
    src='https://static-eu.payments-amazon.com/OffAmazonPayments/gbp/sandbox/lpa/js/Widgets.js'>
  </script>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal">
		<span aria-hidden="true">&times;</span>
		<span class="sr-only">Close</span>
	</button>
	<h4 class="modal-title">${invoiceInstance?.invoiceNumber}</h4>
</div>
<div class="modal-body col-md-12" style="box-shadow: 0 rgba(0, 0, 0, 0.3); border: 1px solid rgba(0, 0, 0, 0);">
	<div class="row">
		<div class="col-lg-12">
			<div class="wrapper wrapper-content animated fadeInRight">
				<g:if test="${invoiceInstance?.status == 'SENT' || invoiceInstance?.status == 'OVERDUE'}">
					<div id="buttonDiv">
						<g:if test="${tradesmanInstance?.amazonPayEnabled == true && tradesmanInstance?.amazon_merchantId && tradesmanInstance?.amazon_clientId && tradesmanInstance?.amazon_accessKey && tradesmanInstance?.amazon_secretKey && fbUser?.paymentMethod?.contains('AmazonPay')}">
							<div class="col-md-2" style="float: right;">
								<div id="LoginWithAmazon"></div>	
								<script type="text/javascript">
									window.onAmazonLoginReady = function() { 
										amazon.Login.setClientId('${tradesmanInstance?.amazon_clientId}'); 
									};
									window.onAmazonPaymentsReady = function(){
										var authRequest; 
										OffAmazonPayments.Button('LoginWithAmazon', '${tradesmanInstance?.amazon_merchantId}', {
											type:  "PwA", 
											color: "Gold", 
											size:  "small", 
											language: "en-GB",
											authorization: function() { 
												loginOptions = {scope: "profile payments:widget", popup: "false"}; 
												var redirectUrl = "${redirectUrl}"+"${invoiceInstance.id}";
												authRequest = amazon.Login.authorize (loginOptions, redirectUrl); 
											},
											onError: function(error) {
												alert("The following error occurred: " + error.getErrorCode() 
								  			         + ' - ' + error.getErrorMessage());
								  			    }  
								  			    });
								  			  }
								</script>
							</div>
						</g:if>
						<g:if test="${tradesmanInstance?.paypalEnabled == true && tradesmanInstance?.paypal_clientId && tradesmanInstance?.paypal_clientSecret && fbUser?.paymentMethod?.contains('PayPal')}">
							<div class="col-md-2" style="float: right;">
								<g:form controller="paypal" action="approve" params="[invoiceId:invoiceInstance.id]">
									<input type="image" src="https://www.paypalobjects.com/webstatic/en_US/i/btn/png/silver-pill-paypal-34px.png" name="submit" alt="PayPal – The safer, easier way to pay online!">
								</g:form>
							</div>
						</g:if>
	                </div>
				</g:if>
				<div class="ibox-content p-xl">
					<g:if test="${tradesmanInstance?.profilePic}">
           				<rendering:inlinePng bytes="${tradesmanInstance?.profilePic}" width="15%" />
           			</g:if>
					<div class="row">
						<div class="col-sm-6">
							<h5>From:</h5>
							<address>
								<strong>${tradesmanInstance?.name}</strong><br>
								<strong>${tradesmanInstance?.companyName}</strong><br>
								${tradesmanInstance?.addressLine1}&nbsp;${tradesmanInstance?.addressLine2}<br>
								${tradesmanInstance?.addressLine3}&nbsp;${tradesmanInstance?.postcode}<br>
								<g:if test="${tradesmanInstance?.phone}">
									<abbr title="Phone">Ph:</abbr>&nbsp;${tradesmanInstance?.phone}<br>
								</g:if>
								<g:if test="${invoiceInstance?.tradesmanEmail}">
									<abbr title="Email">Email:</abbr>&nbsp;${invoiceInstance?.tradesmanEmail}
								</g:if>
							</address>
						</div>
						<div class="col-sm-6 text-right">
							<h4>Invoice No.</h4>
							<h4 class="text-navy">${invoiceInstance?.invoiceNumber}</h4>
							<span>To:</span>
							<address>
								<strong>${invoiceInstance?.customerName}</strong><br>
								${job?.customerAddress1}&nbsp;${job?.customerAddress2}<br>
								${job?.customerAddress3}&nbsp;${job?.customerPostcode}<br>
								<abbr title="Email">Email:</abbr>&nbsp;${job?.customerEmail}
							</address>
							<p>
								<span><strong>Invoice Date:</strong> <g:formatDate date="${invoiceInstance?.invoiceDate}" format="dd MMM yyyy"/></span><br/>
								<g:if test="${invoiceInstance?.jobDate}">
									<span><strong>Job Date:</strong> <g:formatDate date="${invoiceInstance?.jobDate}" format="dd MMM yyyy"/></span>
								</g:if>
							</p>
						</div>
					</div>
					<div class="table-responsive m-t">
						<table class="table invoice-table">
                        	<thead>
	                        	<tr>
	                            	<th>Item List</th>
	                                <th>Quantity</th>
	                                <th>Unit Price</th>
	                                <th>Total Price</th>
	                          	</tr>
                          	</thead>
	                    	<tbody>
	                        	<g:each in="${job?.parts.sort{it.id}}" var="part">
	                            	<tr>
			                        	<td>
			                            	<div>
			                                	<strong>${part?.name}</strong>
			                               	</div>
			                           	</td>
			                            <td>${part?.quantity}</td>
			                            <td>&pound;&nbsp;<g:formatNumber number="${part?.pricePerUnit}" format="0.00"/></td>
			                            <td>&pound;&nbsp;<g:formatNumber number="${part?.quantity * part?.pricePerUnit}" format="0.00"/></td>
			                       	</tr>
	                           	</g:each>
	                      	</tbody>
	                  	</table>
	              	</div><!-- /table-responsive -->
	                <div class="table-responsive m-t">
	                	<table class="table invoice-table">
	                    	<thead>
		                    	<tr>
		                        	<th>Labor Hours</th>
		                            <th>Total Labor Cost</th>
		                       	</tr>
	                     	</thead>
	                        <tbody>
	                        	<tr>
		                        	<td>${invoiceInstance?.laborHours}</td>
		                            <td>&pound;&nbsp;<g:formatNumber number="${invoiceInstance?.laborCost}" format="0.00"/></td>
		                       	</tr>
	                      	</tbody>
						</table>
	            	</div><!-- /table-responsive -->
	                <table class="table invoice-total">
	                	<tbody>
	                    	<g:if test="${invoiceInstance?.discount > 0}">
	                        	<tr>
		                        	<td><strong>Discount:</strong></td>
		                            <td>&pound;&nbsp;<g:formatNumber number="${invoiceInstance?.discount}" format="0.00"/></td>
		                       	</tr>
	                       	</g:if>    
	                       	<tr>
	                       		<td><strong>VAT: </strong></td>
	                       		<td>${setting?.VAT}&nbsp;%</td>
	                       	</tr>    
							<tr>
	                       		<td><strong>TOTAL:</strong></td>
	                            <td>&pound;&nbsp;<g:formatNumber number="${invoiceInstance?.totalPrice}" format="0.00"/></td>
	                      	</tr>
	                  	</tbody>
	             	</table>
	         	</div>            
			</div>
		</div>
	</div>
</div>
<div class="modal-footer"></div>