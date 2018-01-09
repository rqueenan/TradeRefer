<g:if test="${role == 'ROLE_CUSTOMER'}">
<meta name="layout" content="customer_main" />
</g:if>
<g:elseif test="${role == 'ROLE_FACEBOOK_CUSTOMER'}">
<meta name="layout" content="facebook_main" />
</g:elseif>
<g:elseif test="${role == 'ROLE_LINKEDIN_CUSTOMER'}">
<meta name="layout" content="linkedIn_main" />
</g:elseif>
<div class="row page-wrapper">
	<g:hiddenField id="_max" value="${max}" name="max" />
	<g:hiddenField id="_offset" value="${offset}" name="offset" />
	<g:hiddenField id="_sort" value="${sort}" name="sort" />
	<g:hiddenField id="_order" value="${order}" name="order" />
   	<g:if test="${jobs?.size() > 0}">
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
								<table class="footable table table-hover toggle-arrow-tiny">
									<thead>
										<tr>
											<g:sortableColumn class="headerSort" property="tradesmanCompanyName" title="Name" data-sort-ignore="true" style="width: 50%;" />
											<th data-hide="all"></th>
											<g:sortableColumn class="headerSort" property="status" title="Job Status" data-sort-ignore="true" />
											<th data-sort-ignore="true">Invoice</th>
											<th style="text-align: right;" data-sort-ignore="true">Feedback</th>
										</tr>
									</thead>
									<tbody>
										<g:each in="${jobs}" var="job" status="i">
											<tr>
												<td class="project-title">
													${job?.tradesmanCompanyName}
													<br/>
													<small style="text-align: justify;">${job?.jobDescription}</small>
												</td>
												<td>
													<div class="col-md-12" style="padding-left: 0px; padding-right: 0px;">
														<div class="col-md-4">
															<label>Job Type:</label>
														</div>
														<div class="col-md-8">
															${job?.jobType}
														</div>
													</div>
													<div class="col-md-12" style="padding-left: 0px; padding-right: 0px;">
														<div class="col-md-4">
															<label>Job Date:</label>
														</div>
														<div class="col-md-8">
															<g:formatDate date="${job?.dateOfJob}" format="dd MMM yyyy"/></label>
														</div>
													</div>
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
													<g:if test="${job?.invoice && (job?.invoiceStatus?.equals('INVOICE_SENT') || job?.invoiceStatus?.equals('INVOICE_PAID'))}">
														<button type="button" class="btn btn-white btn-sm" onclick="openInvoiceModalForCustomer('${job?.id}','${role}');"><i class="fa fa-money"></i> View </button>
													</g:if>
												</td>
												<td style="text-align: right;">
													<g:if test="${job?.feedback}">
														<button type="button" class="btn btn-white btn-sm" onclick="openFeedbackModal('${job?.id}');"><i class="fa fa-star-half-o" style="font-size: 14px;"></i> View </button>
													</g:if>
													<g:elseif test="${job?.invoice && job?.invoiceStatus?.equals('INVOICE_PAID')}">
														<button type="button" class="btn btn-white btn-sm" onclick="openFeedbackModal('${job?.id}');"><i class="fa fa-plus-circle" style="font-size: 14px;"></i> Create </button>
													</g:elseif>
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
</div>
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
            	<form name="createNewJob_customer" action="/jobs/createNewJobCustomer/${customer?.id}" id="createNewJob_customer">
	            	<g:hiddenField id="create_job_max" value="${max}" name="max" />
					<g:hiddenField id="create_job_offset" value="${offset}" name="offset" />
					<g:hiddenField id="create_job_sort" value="${sort}" name="sort" />
					<g:hiddenField id="create_job_order" value="${order}" name="order" />
					<div class="form-group col-md-6 required">
						<label class='control-label'>Address Line 1</label> 
						<input type="text" placeholder="Address Line 1" name="addressLine1" id="addressLine1_create" class="form-control" value="${customer?.addressLine1}" required>
					</div>
					<div class="form-group col-md-6">
						<label class='control-label'>Address Line 2</label> 
						<input type="text" placeholder="Address Line 2" name="addressLine2" id="addressLine2_create" class="form-control" value="${customer?.addressLine2}">
					</div>
					<div class="form-group col-md-6">
						<label class='control-label'>Address Line 3</label> 
						<input type="text" placeholder="Address Line 3" name="addressLine3" id="addressLine3_create" class="form-control" value="${customer?.addressLine3}">
					</div>
					<div class="form-group col-md-6 required">
						<label class='control-label'>Post Code</label> 
						<input type="text" placeholder="Post Code" name="postcode" id="postcode_create" class="form-control" value="${customer?.postcode}" required>
					</div>
					<div class="col-md-12 hr-line-dashed"></div>
					<div class="form-group col-md-6 required">
						<label class='control-label'>Company Name</label> 
						<input type="text" placeholder="Company Name" name="tradesmanCompanyName" id="tradesmanCompanyName_create" class="form-control" required>
					</div>
					<div class="form-group col-md-6 required">
						<label class='control-label'>Company Email</label> 
						<input type="email" placeholder="Company Email" name="tradesmanEmail" id="tradesmanEmail_create" class="form-control" required>
					</div>
					<div class="form-group col-md-6 " id="date_1">
						<label>Job Date</label>
						<div class="input-group date">
							<span class="input-group-addon"><i class="fa fa-calendar"></i></span><input type="text" name="jobDate" class="form-control">
						</div>
					</div>
					<div class="form-group col-md-6 required">
						<label class='control-label'>Job Type</label> 
						<g:select class="form-control chosen-select" name="jobType" id="jobType_create" from="['Electrical','Plumbing', 'Gas', 'Plastering', 'Tiling', 'Painting / Decorating']" noSelection="['':'-Choose Job Type-']" required="required"/>
					</div>
					<div class="col-md-12 hr-line-dashed"></div>
					<div class="col-md-12">
						<h4 class="m-t-none m-b">Job Description</h4>
					</div>
					<div class="form-group col-md-12"> 
						<textarea maxlength="255" placeholder="Job Description" name="jobDesc" class="form-control" rows="4" ></textarea>
					</div>
					<div class="col-md-12" style="text-align: right;">
						<button type="button" class="btn btn-white" data-dismiss="modal">Close</button>
						<button type="button" onclick="validateNotCustomer();" class="btn btn-primary">Request Job</button>
					</div>
            	</form>
            </div>
            <div class="modal-footer"></div>
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