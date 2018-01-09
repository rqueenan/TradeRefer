<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="tradesman_main" />
	</head>
    <body>
    	<g:if test="${invoices?.size() > 0}">
    		<div class="row page-wrapper">
	        	<div class="col-lg-12">
	        		<div class="wrapper wrapper-content animated fadeInUp">
						<div class="ibox">
							<div class="ibox-content">
	                            <div class="project-list">
	                            	<div class="table-responsive">
	                            		<table class="footable table table-hover toggle-arrow-tiny">
			                                <thead>
			                                <tr>
			                                    <g:sortableColumn class="headerSort" property="invoiceNumber" title="Invoice ID" data-sort-ignore="true"/>
			                                    <th data-hide="all"></th>
			                                    <g:sortableColumn class="headerSort" property="invoiceDate" title="Invoice Date" data-sort-ignore="true"/>
			                                    <g:sortableColumn class="headerSort" property="totalPrice" title="Amount" data-sort-ignore="true"/>
			                                    <g:sortableColumn class="headerSort" property="status" title="Status" data-sort-ignore="true"/>
			                                    <th data-sort-ignore="true">Action</th>
			                                </tr>
			                                </thead>
		                                    <tbody>
		                                    	<g:each in="${invoices}" var="invoice">
		                                    		<tr>
				                                        <td class="project-title">
				                                            ${invoice?.invoiceNumber}
				                                        </td>
				                                        <td>
				                                        	<div class="col-md-12" style="padding-left: 0px; padding-right: 0px;">
																<div class="col-md-2">
																	<label>Name:</label>
																</div>
																<div class="col-md-10">
																	${invoice?.customerName}				
																</div>
															</div>
															<div class="col-md-12" style="padding-left: 0px; padding-right: 0px;">
																<div class="col-md-2">
																	<label>Address:</label>
																</div>
																<div class="col-md-10">
																	${invoice?.customerAddress1}<br/> 
																	<g:if test="${invoice?.customerAddress2}">
																		${invoice?.customerAddress2}<br/>
																	</g:if>
																	<g:if test="${invoice?.customerAddress3}">
																		${invoice?.customerAddress3}<br/>
																	</g:if>
																	${invoice?.customerPostcode}<br/>				
																</div>
															</div>
															<div class="col-md-12" style="padding-left: 0px; padding-right: 0px;">
																<div class="col-md-2">
																	<label>Job Date:</label>
																</div>
																<div class="col-md-10">
																	<g:formatDate date="${invoice?.jobDate}" format="dd MMM yyyy"/>
																</div>
															</div>
															<div class="col-md-12" style="padding-left: 0px; padding-right: 0px;">
																<div class="col-md-2">
																	<label>Email:</label>
																</div>
																<div class="col-md-10">
																	${invoice?.customerEmail}				
																</div>
															</div>
				                                        	<div class="col-md-12" style="padding-left: 0px; padding-right: 0px;">
																<div class="col-md-2">
																	<label>Job Type:</label>
																</div>
																<div class="col-md-10">
																	${invoice?.jobType}
																</div>
															</div>
				                                        </td>
				                                        <td>
				                                        	<g:formatDate date="${invoice?.invoiceDate}" format="dd MMM yyyy"/></label>
				                                        </td>
				                                        <td>
				                                        	&pound;&nbsp;<g:formatNumber number="${invoice?.totalPrice}" format="0.00"/>
				                                        </td>
				                                        <td class="project-status">
				                                        	<g:if test="${invoice?.status == 'PAID' }">
				                                        		<span class="label label-primary">PAID</span>
				                                        	</g:if>
				                                        	<g:elseif test="${invoice?.status == 'SENT'}">
				                                        		<span class="label label-warning">${invoice?.status}</span>
				                                        	</g:elseif>
				                                        	<g:elseif test="${invoice?.status == 'OVERDUE'}">
				                                        		<span class="label label-error">${invoice?.status}</span>
				                                        	</g:elseif>
				                                        	<g:else>
				                                        		<span class="label label-default">${invoice?.status}</span>
				                                        	</g:else>
				                                        </td>
				                                        <td>
				                                        	<g:if test="${invoice?.status == 'PENDING'}">
				                                        		<button type="button" class="btn btn-white btn-sm" onclick="viewInvoice('${invoice?.id}');"><i class="fa fa-money"></i> View </button>
				                                        	</g:if>
				                                        	<g:else>
				                                        		<button type="button" class="btn btn-white btn-sm" onclick="viewInvoice('${invoice?.id}');"><i class="fa fa-money"></i> Sent </button>
				                                        	</g:else>
				                                        </td>
				                                    </tr>
		                                    	</g:each>
		                                    </tbody>
		                                </table>
	                            	</div>
	                                <ul style="float: right;">
						                <g:paginate next="Next" prev="Back" total="${invoice_count ?: 0}" />
						            </ul>
	                            </div>
	                        </div>
						</div>
					</div>
	        	</div>
	        </div>
    	</g:if>
        <g:else>
        	<div class="page-wrapper col-md-12">
	            <div class="wrapper wrapper-content">
	                <div class="middle-box text-center animated fadeInRightBig">
	                    <h3 class="font-bold">No Invoices are generated yet.</h3>
	                    <div class="error-desc">
	                        Click on following link to select the job for Invoice creation
	                        <br/>
	                        <g:link controller="tradesman" action="showJobs" class="btn btn-primary m-t">
                               	Select Job
                           	</g:link>
	                    </div>
	                </div>
	            </div>
	        </div>
        </g:else>
        <div class="modal inmodal" id="viewInvoiceModal" tabindex="-1" role="dialog" aria-hidden="true">
			<div class="modal-dialog modal-lg">
				<div class="modal-content animated bounceInRight">
            		<div class="viewInvoice">
                        <div id="refreshThisDiv_viewInvoice"></div>
            		</div>
        		</div>
			</div>
		</div>
		<div class="modal inmodal" id="editInvoiceModal" tabindex="-1" role="dialog" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content animated bounceInRight">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span>
							<span class="sr-only">Close</span>
						</button>
						<i class="fa fa-money modal-icon"></i>
						<h4 class="modal-title">Invoice</h4>
            		</div>
            		<div class="editInvoiceFormHtml">
                        <div id="refreshThisDiv_editInvoice"></div>
            		</div>
        		</div>
			</div>
		</div>
    </body>
</html>