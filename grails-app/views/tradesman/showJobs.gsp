<meta name="layout" content="tradesman_main" />
		
<g:hiddenField id="_max" value="${max}" name="max" />
<g:hiddenField id="_offset" value="${offset}" name="offset" />
<g:hiddenField id="_sort" value="${sort}" name="sort" />
<g:hiddenField id="_order" value="${order}" name="order" />
<g:if test="${jobs?.size() > 0}">
	<div class="row page-wrapper">
		<div class="col-lg-12">
	    	<g:message code="${flash.message}" />
	        <div class="wrapper wrapper-content animated fadeInUp">
	        	<div class="ibox">
	            	<div class="ibox-title" style="border: 0px;">
	                	<h5>All Jobs</h5>
	                    <div class="ibox-tools">
	                    	<button type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#myModal">
	                        	Create New Job
							</button>
						</div>
					</div>
					<div class="ibox-content">
						<div class="project-list">
							<div class="table-responsive">
								<table class="footable table table-hover toggle-arrow-tiny" data-sorting="false" style="overflow-x: auto; white-space: normal;">
									<thead>
										<tr>
											<g:sortableColumn property="customerName" class="headerSort" title="Name" data-sort-ignore="true" style="width: 48%;" />
											<th data-hide="all"></th>
											<g:sortableColumn property="dateOfJob" class="headerSort" title="Job Date" data-sort-ignore="true" />
											<g:sortableColumn property="status" class="headerSort" title="Job Status" data-sort-ignore="true" />
											<th data-sort-ignore="true">Invoice</th>
											<th data-sort-ignore="true">Feedback</th>
											<th data-sort-ignore="true">Action</th>
										</tr>
									</thead>
									<tbody>
										<g:each in="${jobs}" var="job" status="i">
											<tr>
												<td class="project-title">
													${job?.customerName}
													<br/>
													<small style="text-align: justify; word-w">${job?.jobDescription}</small>
												</td>
												<td>
													<div class="col-md-12" style="padding-left: 0px; padding-right: 0px;">
														<div class="col-md-2">
															<label>Address:</label>
														</div>
														<div class="col-md-10">
															${job?.customerAddress1}<br/> 
															<g:if test="${job?.customerAddress2}">
																${job?.customerAddress2}<br/>
															</g:if>
															<g:if test="${job?.customerAddress3}">
																${job?.customerAddress3}<br/>
															</g:if>
															${job?.customerPostcode}<br/>				
														</div>
													</div>
													<div class="col-md-12" style="padding-left: 0px; padding-right: 0px;">
														<div class="col-md-2">
															<label>Job Date:</label>
														</div>
														<div class="col-md-10">
															<g:formatDate date="${job?.dateOfJob}" format="dd MMM yyyy"/>
														</div>
													</div>
													<div class="col-md-12" style="padding-left: 0px; padding-right: 0px;">
														<div class="col-md-2">
															<label>Email:</label>
														</div>
														<div class="col-md-10">
															${job?.customerEmail}
														</div>
													</div>
													<div class="col-md-12" style="padding-left: 0px; padding-right: 0px;">
														<div class="col-md-2">
															<label>Job Type:</label>
														</div>
														<div class="col-md-10">
															${job?.jobType}
														</div>
													</div>
												</td>
												<td>
													<g:formatDate date="${job?.dateOfJob}" format="dd MMM yyyy"/>
												</td>
												<td class="project-status">
													<g:if test="${job?.status == 'COMPLETE' }">
														<span class="label label-primary">COMPLETE</span>
													</g:if>
													<g:if test="${job?.status == 'PAID' }">
														<span class="label label-primary">PAID</span>
													</g:if>
													<g:if test="${job?.status == 'PENDING' }">
														<span class="label label-default">PENDING</span>
													</g:if>
												</td>
												<td>
													<g:if test="${job?.invoiceStatus == 'NO_INVOICE_CREATED' || job?.invoiceStatus == '' || job?.invoiceStatus == null}">
														<button type="button" class="btn btn-white btn-sm" onclick="openCreateInvoiceModal('${job?.id}');"><i class="fa fa-plus-circle"></i> Create </button>
													</g:if>
													<g:elseif test="${job?.invoiceStatus == 'INVOICE_SENT' || job?.invoiceStatus == 'INVOICE_PAID'}">
														<button type="button" class="btn btn-white btn-sm" onclick="openInvoiceModal('${job?.id}');"><i class="fa fa-money"></i> Sent </button>
													</g:elseif>
													<g:else>
														<button type="button" class="btn btn-white btn-sm" onclick="openInvoiceModal('${job?.id}');"><i class="fa fa-money"></i> View </button>
													</g:else>
												</td>
												<td>
													<g:if test="${job?.feedback}">
														<button type="button" class="btn btn-white btn-sm" onclick="openFeedbackModal('${job?.id}');"><i class="fa fa-star-half-o"></i>  View </button>
													</g:if>
												</td>
												<td>
													<g:if test="${job?.status == 'PENDING' && job?.invoiceStatus == 'NO_INVOICE_CREATED'}">
														<button type="button" class="btn btn-white btn-sm" onclick="openEditJobModal('${job?.id}');"><i class="fa fa-pencil"></i> Edit </button>
													</g:if>
												</td>
											</tr>
										</g:each>
									</tbody>
								</table>
							</div>
							<ul style="float: right;">
								<g:paginate next="Next" prev="Back" total="${jobsCount ?: 0}" />
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
				<h3 class="font-bold">No Jobs created yet.</h3>
				<div class="error-desc">
					Click on following link to create your first job !
					<br/>
					<button type="button" class="btn btn-primary m-t" data-toggle="modal" data-target="#myModal">
						Create New Job
					</button>
				</div>
			</div>
		</div>
	</div>
</g:else>
<div class="modal inmodal" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span>
					<span class="sr-only">Close</span>
				</button>
				<i class="fa fa-wrench modal-icon"></i>
				<h4 class="modal-title">Create New Job</h4>
			</div>
				<div class="modal-body col-md-12">
					<div class="col-md-12">
						<div class="alert alert-danger alignCenter" role="alert" id="errorAlert" style="padding: 0px; display: none;">${flash.message}</div>
					</div>
					<form name="createNewJob" id="createNewJob" action="/tradesman/createNewJobTradesman/${tradesmanInstance?.id}">
						<g:hiddenField id="create_job_max" value="${max}" name="max" />
						<g:hiddenField id="create_job_offset" value="${offset}" name="offset" />
						<g:hiddenField id="create_job_sort" value="${sort}" name="sort" />
						<g:hiddenField id="create_job_order" value="${order}" name="order" />
						<div class="form-group col-md-6 required">
							<label class='control-label'>Customer Name</label> 
							<input type="text" placeholder="Customer Name" name="customerName" id="customerName_create" class="form-control" required>
						</div>
						<div class="form-group col-md-6 required">
							<label class='control-label'>Customer Email</label> 
							<input type="email" placeholder="Customer Email" name="customerEmail" id="customerEmail_create" class="form-control" required>
						</div>
						<div class="col-md-12 hr-line-dashed" style="margin-top: 0px;"></div>
						<div class="col-md-12">
							<h4 class="m-t-none m-b">Customer Address</h4>
						</div>
						<div class="form-group col-md-6 required">
							<label class='control-label'>Address Line 1</label> 
							<input type="text" placeholder="Address Line 1" name="addressLine1" id="addressLine1_create" class="form-control" required>
						</div>
						<div class="form-group col-md-6">
							<label class='control-label'>Address Line 2</label> 
							<input type="text" placeholder="Address Line 2" name="addressLine2" id="addressLine2_create" class="form-control">
						</div>
						<div class="form-group col-md-6">
							<label class='control-label'>Address Line 3</label> 
							<input type="text" placeholder="Address Line 3" name="addressLine3" id="addressLine3_create" class="form-control">
						</div>
						<div class="form-group col-md-6 required">
							<label class='control-label'>Post Code</label> 
							<input type="text" placeholder="Post Code" name="postcode" id="postcode_create" class="form-control" required>
						</div>
						<div class="col-md-12 hr-line-dashed" style="margin-top: 0px;"></div>
						<div class="form-group col-md-6 " id="date_1">
							<label>Job Date</label>
							<div class="input-group date">
								<span class="input-group-addon"><i class="fa fa-calendar"></i></span><input type="text" name="jobDate" class="form-control">
							</div>
						</div>
						<div class="form-group col-md-6 required">
							<label class='control-label'>Job Type</label> 
							<g:if test="${tradesmanInstance?.workType?.size() > 1}">
								<g:select class="form-control chosen-select" name="jobType" from="${tradesmanInstance?.workType}" noSelection="['':'-Choose Job Type-']" required="required"/>
							</g:if>
							<g:else>
								<g:select class="form-control chosen-select" name="jobType" from="${tradesmanInstance?.workType}" required="required"/>
							</g:else>
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
						<div id="field">
							<div id="field0">
								<div class="form-group col-md-4">
									<input id="part_name0" name="part_name0" type="text" placeholder="Part Name"  class="form-control input-md">
								</div>
								<div class="form-group col-md-3">
									<input id="qty0" name="qty0" min="0" type="number" placeholder="Qty" value="0" class="form-control input-md" onchange="calculateTotal();">
								</div>
								<div class="form-group col-md-4">
									<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input step=".01" style="display: inline; width: 88%;" min="0" type="number" id="unit_price0" name="unit_price0"   placeholder="Unit Price" value="0.00" class="form-control input-md" onchange="formatThisNumber('unit_price0');" >
								</div>
							</div>
						</div>
						<div class="col-md-1">
							<button id="add-more" name="add-more" class="btn btn-primary">+</button>
						</div>	
						<div class="col-md-12 hr-line-dashed"></div>
						<div class="col-md-12">
							<h4 class="m-t-none m-b">Labour</h4>
						</div>
						<div class="form-group col-md-4 required">
							<label class='control-label'>Hours</label>
						</div>
						<div class="form-group col-md-4 required">
							<label class='control-label'>Hourly Rate</label>
						</div>
						<div class="form-group col-md-4 required">
							<label class='control-label'>Labour Cost</label>
						</div>
						<div class="form-group col-md-4">
							<input type="number" min="0" placeholder="No." id="laborHours" name="noOfHours" class="form-control" onchange="calculateTotalLaborCost('${tradesmanInstance?.hourlyRate}','');"" required="required">
						</div>
						<div class="form-group col-md-4">
							<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input step=".01" style="display: inline; width: 90%;" min="0" type="number" placeholder="Per Hour" id="perHour_invoice" value="${hourlRate2D}" name="perHour_invoice" class="form-control" onchange="calculateLabor('${tradesmanInstance?.hourlyRate}');" required="required" disabled="disabled">
						</div>
						<div class="form-group col-md-4"> 
							<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input step=".01" style="display: inline; width: 90%;" min="0" type="number" placeholder="Cost" id="laborTotalCost" name="costOfHours" class="form-control" onchange="formatThisNumber('laborTotalCost');" required="required">
						</div>
						<div class="col-md-12 hr-line-dashed"></div>
						<div class="col-md-12">
							<h4 class="m-t-none m-b">Job Description</h4>
						</div>
						<div class="form-group col-md-12"> 
							<textarea maxlength="255" placeholder="Job Description" name="jobDesc" class="form-control" rows="4" ></textarea>
						</div>
						<input type="hidden" id="count" name="count" value="0">
						<div class="col-md-12" style="text-align: right;">
							<button type="button" class="btn btn-white" data-dismiss="modal">Close</button>
							<button type="button" onclick="validateForTradesman();" class="btn btn-primary">Create Job</button>
						</div>
					</form>
				</div>
			<div class="modal-footer"></div>
		</div>
	</div>
</div>
		
<div class="modal inmodal" id="editModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span>
					<span class="sr-only">Close</span>
				</button>
				<i class="fa fa-pencil-square-o modal-icon"></i>
				<h4 class="modal-title">Edit Job</h4>
          		</div>
          		<div class="editJobFormHtml">
                      <div id="refreshThisDiv"></div>
          		</div>
      		</div>
	</div>
</div>
<div class="modal inmodal" id="invoiceModal" tabindex="-1" role="dialog" aria-hidden="true">
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
          		<div class="invoiceFormHtml">
                      <div id="refreshThisDiv_invoice"></div>
          		</div>
      		</div>
	</div>
</div>
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
<div class="modal inmodal" id="feedbackModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span>
					<span class="sr-only">Close</span>
				</button>
				<i class="fa fa-star-half-o modal-icon"></i>
				<h4 class="modal-title">Feedback</h4>
          		</div>
          		<div class="feedbackFormHtml">
                      <div id="refreshThisDiv_feedback"></div>
          		</div>
      		</div>
	</div>
</div>

