<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
 
 <html>
	<head>
		<style  type="text/css">
			@page {
    			size: 210mm 297mm;
    			counter-increment: page;
    			@bottom-center{ 
    				padding-right:20px; 
    				font-size:11px;
    				content: ""; 
    			}
    			
    			@top-left{
    				content: element(header);
    			}    			
  			}
  			.header { 
  				position: running(header); 
  				margin-top: 15px;
  				margin-left: 5px;
  			}
  			
  			.page_number:before {
				content: "| Page "counter(page); 
			}
    		body {
      			font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
    		}
    		.ncd {
                 background: rgba(204, 204, 204, 0.24);
                 height: 162px;
                 border-radius: 50%;
 			}
 			.ncp {
 			 	text-align: center;
 			    color: #999;
 			    font-size: 16px;
 			    border-radius: 25%;
 			    position: relative;
 			    top: 50%;
 			    transform: translateY(-50%);
 			}
 			
 			address {
				margin-bottom: 20px;
				font-style: normal;
				line-height: 1.42857143;
 			}
 			
 			td{
				border-top: 1px solid #e7eaec;
			    line-height: 1.42857;
			    padding: 8px;
			    vertical-align: top;
 			}
 			
 			th{
 				border-top: 0;
 				line-height: 1.42857;
			    padding: 8px;
			    vertical-align: top;
 			}
		</style>
	</head>
	<body style="font-family: 'open sans', 'Helvetica Neue', Helvetica, Arial, sans-serif;">
		<div>
			<div style="color: inherit;">
				<g:if test="${tradesmanInstance?.profilePic}">
       				<rendering:inlinePng bytes="${tradesmanInstance?.profilePic}" width="15%" />
       			</g:if>
				<div style="margin-left: -15px; margin-right: -15px;">
					<div style="width: 40%;	float: left; position: relative; min-height: 1px; padding-right: 15px; padding-left: 15px;">
						<h5 style="margin-top: 5px; font-weight: 600; font-size: 12px; margin-bottom: 10px; line-height: 1.1;">From:</h5>
						<address>
							<strong>${tradesmanInstance?.name}</strong><br></br>
							<strong>${tradesmanInstance?.companyName}</strong><br></br>
							${tradesmanInstance?.addressLine1}&nbsp;${tradesmanInstance?.addressLine2}<br></br>
							${tradesmanInstance?.addressLine3}&nbsp;${tradesmanInstance?.postcode}<br></br>
							<g:if test="${tradesmanInstance?.phone}">
								<abbr title="Phone">Ph:</abbr>&nbsp;${tradesmanInstance?.phone}<br></br>
							</g:if>
							<g:if test="${invoice?.tradesmanEmail}">
								<abbr title="Email">Email:</abbr>&nbsp;${invoice?.tradesmanEmail}
							</g:if>				
						</address>
					</div>
					<div style="width: 40%;	float: right; position: relative; min-height: 1px; padding-right: 15px; padding-left: 15px; text-align: right;">
						<h4 style="margin-top: 5px;">Invoice No.</h4>
						<h4 style="margin-top: 5px; color: #1ab394;">${invoice?.invoiceNumber}</h4>
						<h5 style="margin-top: 5px; font-weight: 600; font-size: 12px; margin-bottom: 10px; line-height: 1.1;">To:</h5>
						<address>
							<strong>${invoice?.customerName}</strong><br></br>
							${job?.customerAddress1}&nbsp;${job?.customerAddress2}<br></br>
							${job?.customerAddress3}&nbsp;${job?.customerPostcode}<br></br>
							<abbr title="Email">Email:</abbr>&nbsp;${job?.customerEmail}
						</address>			
						<p>
							<span><strong>Invoice Date:</strong> <g:formatDate date="${invoice?.invoiceDate}" format="dd MMM yyyy"/></span><br/>
							<g:if test="${invoice?.jobDate}">
								<span><strong>Job Date:</strong> <g:formatDate date="${invoice?.jobDate}" format="dd MMM yyyy"/></span>
							</g:if>
						</p>			
					</div>
				</div>
				<div style="margin-top: 15px; min-height: 0.01%; overflow-x:auto; float: left; width: 100%;">
					<table style="width: 100%; max-width: 100%; margin-bottom: 20px; border-spacing: 0; border-collapse: collapse;">
						<thead>
                        	<tr>
                            	<th style="width: 40%;">Item List</th>
                                <th style="width: 20%; text-align: right;">Quantity</th>
                                <th style="width: 20%; text-align: right;">Unit Price</th>
                                <th style="width: 20%; text-align: right;">Total Price</th>
                          	</tr>
                         	</thead>
                    	<tbody>
                    		<g:each in="${invoice?.parts}" var="part">
                            	<tr>
		                        	<td>
		                            	<div>
		                                	<strong>${part?.name}</strong>
		                               	</div>
		                           	</td>
		                            <td style="text-align: right;">${part?.quantity}</td>
		                            <td style="text-align: right;">${part?.pricePerUnit}&nbsp; &pound;</td>
		                            <td style="text-align: right;">${part?.quantity * part?.pricePerUnit}&nbsp; &pound;</td>
		                       	</tr>
                           	</g:each>
                    	</tbody>
					</table>
				</div>
				<div style="margin-top: 15px; min-height: 0.01%; overflow-x:auto; width: 100%; float: left;">
					<table style="width: 100%; max-width: 100%; margin-bottom: 20px; border-spacing: 0; border-collapse: collapse;">
						<thead>
                        	<tr>
                            	<th>Labor Hours</th>
		                        <th style="text-align: right;">Total Labor Cost</th>
                          	</tr>
                       	</thead>
                    	<tbody>
                    		<tr>
	                        	<td>${invoice?.laborHours}</td>
	                            <td style="text-align: right;">${invoice?.laborCost}&nbsp; &pound;</td>
	                       	</tr>
                    	</tbody>
					</table>
				</div>
				<table style="width: 100%; max-width: 100%; margin-bottom: 20px; border-spacing: 0; border-collapse: collapse; float: left;">
	               	<tbody>
                   		<g:if test="${invoice?.discount > 0}">
                       		<tr>
                        		<td style="border-top: 0; text-align: right; width: 80%;"><strong>Discount:</strong></td>
                            	<td style="border-top: 0; text-align: right; width: 20%; border-bottom: 1px solid #e7eaec;">- ${invoice?.discount}&nbsp; &pound;</td>
                       		</tr>
                      	</g:if>        
						<tr>
                      		<td style="border-top: 0; text-align: right; width: 80%;"><strong>VAT:</strong></td>
                        	<td style="border-top: 0; text-align: right; width: 20%; border-bottom: 1px solid #e7eaec;">${setting?.VAT}&nbsp; %</td>
                     	</tr>
						<tr>
                      		<td style="border-top: 0; text-align: right; width: 80%;"><strong>TOTAL:</strong></td>
                        	<td style="border-top: 0; text-align: right; width: 20%; border-bottom: 1px solid #e7eaec;">${invoice?.totalPrice}&nbsp; &pound;</td>
                     	</tr>
                 	</tbody>
            	</table>
			</div>
		</div>
	</body>
</html>