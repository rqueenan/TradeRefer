<meta name="layout" content="tradesman_main" />
<style type="text/css">
	.hideMe {
		display: none;
	}
	.panel-heading .accordion-toggle:after {
    	/* symbol for "opening" panels */
    	font-family: 'Glyphicons Halflings';  /* essential for enabling glyphicon */
    	content: "\e114";/* adjust as needed, taken from bootstrap.css */
    	float: right;        /* adjust as needed */
    	color: grey;         /* adjust as needed */
    	padding-top: 11px;
    	padding-right: 11px;
    	float: left;
	}
	.panel-heading .accordion-toggle.collapsed:after {
    	/* symbol for "collapsed" panels */
    	content: "\e080";    /* adjust as needed, taken from bootstrap.css */
    	padding-top: 11px;
    	padding-right: 11px;
    	float: left;
	}
	.panel-heading{
		padding: 1px 8px 8px 8px !Important;
	}
</style>       
        <div class="col-md-12">
            <div class="ibox float-e-margins">
                <div class="ibox-content" style="border: 0px;">	
                	<g:uploadForm controller='tradesman' method="post" action='uploadProfilePicture'>
						<div id="showProfilePicture" class="col-md-3" class="no-padding border-left-right col-md-4" style="clear: left; float: left;">
                			<g:if test="${tradesmanInstance?.profilePic}">
                				<img src="${createLink(controller:'tradesman', action:'viewProfilePic')}" alt="your image" style="width: 100%; height: 100%;" />
                			</g:if>
                		</div>
                		<div class="col-md-3" id="showPreview" style="clear: left; float: left; display: none;">
                    		<label class="btn btn-default" style="float: right;" onclick="removeUploadedImage();" ><i class="fa fa-times" area-hidden="true"></i></label>
                    			<img id="blah" src="#" alt="your image" />	
                    		<br/>
                    		<button type='submit' class="col-md-4 btn btn-primary" style="float: right; margin-top: 3%;">Upload</button>
                    	</div>
                		<div class="col-md-3" style="clear: left; float: left;">	
                    		<div class="form-group">
                    			<label class="btn btn-primary" style="    border-radius: 0px;">
                    				<input type="file" name="featuredImageFile" id="featuredImageFile" accept="image/gif, image/jpeg, image/png" onchange="readURL(this);" />
                    			</label>
                    		</div>
                    	</div>
                	</g:uploadForm>
                </div>
                <div class="ibox-content">
                    <g:form controller="tradesman" action="updateProfile" id="${tradesmanInstance?.id}">
                    	<div class="col-md-4" style="float: right;">
                       		<button type="submit" class="btn btn-primary block full-width m-b">Update Profile</button>
                       	</div>
                    	<div class="col-md-12">
                       		<h4 class="m-t-none m-b">Company Details</h4>
                       	</div>
                    	<div class="col-md-4">
							<div class="form-group required">
								<label class="control-label">Contact Name</label>
								<input type="text" placeholder="Contact Name" class="form-control" value="${tradesmanInstance?.name}" name="name" required>
							</div>
                       	</div>
                        <div class="col-md-4">
							<div class="form-group required">
								<label class="control-label">Company Name</label>
								<input type="text" placeholder="Company Name" class="form-control" value="${tradesmanInstance?.companyName}" name="companyName" required>
							</div>
                       	</div>
                       	<div class="col-md-4">
							<div class="form-group required">
								<label class="control-label">Company Email</label>
								<input type="email" placeholder="Company Email" class="form-control" value="${tradesmanInstance?.companyEmail}" name="companyEmail" disabled="disabled">
							</div>
                       	</div>
                       	<div class="col-md-4">
							<div class="form-group required">
								<label class="control-label">Address Line 1</label>
								<input type="text" required placeholder="Address Line 1" class="form-control" value="${tradesmanInstance?.addressLine1}" name="addressLine1">
							</div>
                       	</div>
                       	<div class="col-md-4">
							<div class="form-group">
								<label>Address Line 2</label>
								<input type="text" placeholder="Address Line 2" class="form-control" value="${tradesmanInstance?.addressLine2}" name="addressLine2">
							</div>
                       	</div>
                       	<div class="col-md-4">
							<div class="form-group">
								<label>Address Line 3</label>
								<input type="text" placeholder="Address Line 3" class="form-control" value="${tradesmanInstance?.addressLine3}" name="addressLine3">
							</div>
                       	</div>
                       	<div class="col-md-4">
							<div class="form-group required">
								<label class="control-label">Post Code</label>
								<input type="text" required placeholder="Post Code" class="form-control" value="${tradesmanInstance?.postcode}" name="postcode">
							</div>
                       	</div>
                       	<div class="col-md-4">
							<div class="form-group">
								<label>Phone</label>
								<input type="text" placeholder="Phone" class="form-control" value="${tradesmanInstance?.phone}" name="phone">
							</div>
                       	</div>
                       	<div class="col-md-12"></div>
                       	<div class="col-md-4">
							<div class="form-group required">
								<label class="control-label">Company Number</label>
								<input type="text" required placeholder="Company Number" class="form-control" value="${tradesmanInstance?.companyNumber}" name="companyNumber">
							</div>
                       	</div>
                       	<div class="col-md-4">
							<div class="form-group required">
								<label class="control-label">Certification Number</label>
								<input type="text" required placeholder="Certification Number" class="form-control" value="${tradesmanInstance?.certificaionNumber}" name="certificaionNumber">
							</div>
                       	</div>
                       	<div class="col-md-4">
							<div class="form-group">
								<label>Hourly Rate</label><br/>
								<span style="font-size: 16px; font-weight: bold;">&pound;</span>&nbsp;<input style="display: inline; width: 90%;" type="number" placeholder="Hourly Rate" class="form-control" value="${tradesmanInstance?.hourlyRate}" id="hourlyRate" step=".01" onchange="formatThisNumber('hourlyRate');" name="hourlyRate">
							</div>
                       	</div>
                       	<div class="col-md-12 hr-line-dashed"></div>
                       	<div class="col-md-12">
                       		<div class="form-group">
								<h4 class="m-t-none m-b">Work Type</h4>
								<g:if test="${tradesmanInstance?.workType?.contains('Electrical')}">
									<div class="i-checks col-md-6 alignLeft"><label> <input id="workType_1" type="checkbox" name="workType_Electrical" checked> <i></i> Electrical </label></div>		
								</g:if>
								<g:else>
									<div class="i-checks col-md-6 alignLeft"><label> <input id="workType_1" type="checkbox" name="workType_Electrical"> <i></i> Electrical </label></div>
								</g:else>
								<g:if test="${tradesmanInstance?.workType?.contains('Plumbing')}">
									<div class="i-checks col-md-6 alignLeft"><label> <input id="workType_2" type="checkbox" name="workType_Plumbing" checked> <i></i> Plumbing </label></div>		
								</g:if>
								<g:else>
									<div class="i-checks col-md-6 alignLeft"><label> <input id="workType_2" type="checkbox" name="workType_Plumbing" > <i></i> Plumbing </label></div>
								</g:else>
								<g:if test="${tradesmanInstance?.workType?.contains('Gas')}">
									<div class="i-checks col-md-6 alignLeft"><label> <input id="workType_4" type="checkbox" name="workType_Gas" checked> <i></i> Gas </label></div>
								</g:if>
								<g:else>
									<div class="i-checks col-md-6 alignLeft"><label> <input id="workType_4" type="checkbox" name="workType_Gas" > <i></i> Gas </label></div>
								</g:else>
								<g:if test="${tradesmanInstance?.workType?.contains('Plastering')}">
									<div class="i-checks col-md-6 alignLeft"><label> <input id="workType_5" type="checkbox" name="workType_Plastering" checked> <i></i> Plastering </label></div>
								</g:if>
								<g:else>
									<div class="i-checks col-md-6 alignLeft"><label> <input id="workType_5" type="checkbox" name="workType_Plastering" > <i></i> Plastering </label></div>
								</g:else>
								<g:if test="${tradesmanInstance?.workType?.contains('Tiling')}">
									<div class="i-checks col-md-12 alignLeft"><label> <input id="workType_6" type="checkbox" name="workType_Tiling" checked> <i></i> Tiling </label></div>	
								</g:if>
								<g:else>
									<div class="i-checks col-md-12 alignLeft"><label> <input id="workType_6" type="checkbox" name="workType_Tiling" > <i></i> Tiling </label></div>
								</g:else>
								<g:if test="${tradesmanInstance?.workType?.contains('Painting / Decorating')}">
									<div class="i-checks col-md-12 alignLeft"><label> <input id="workType_3" type="checkbox" name="workType_Painting_Decorating" checked> <i></i> Painting / Decorating </label></div>	
								</g:if>
								<g:else>
									<div class="i-checks col-md-12 alignLeft"><label> <input id="workType_3" type="checkbox" name="workType_Painting_Decorating" > <i></i> Painting / Decorating </label></div>
								</g:else>
							</div>
                       	</div>
                       	<div class="col-md-12 hr-line-dashed"></div>
                       	<div class="col-md-12">
                       		<h4 class="m-t-none m-b">Account Details</h4>
                       	</div>
                       	<div class="col-md-4">
							<div class="form-group">
								<label>Account Name</label>
								<input type="text" placeholder="Account Name" class="form-control" value="${tradesmanInstance?.collection_accountName}" name="collection_accountName">
							</div>
                       	</div>
                       	<div class="col-md-4">
							<div class="form-group">
								<label>Sort Code</label>
								<input type="text" placeholder="Sort Code" class="form-control" value="${tradesmanInstance?.collection_sortCode}" name="collection_sortCode">
							</div>
                       	</div>
                       	<div class="col-md-4">
							<div class="form-group">
								<label>Account Number</label>
								<input type="text" placeholder="Account Number" class="form-control" value="${tradesmanInstance?.collection_accountNo}" name="collection_accountNo">
							</div>
                       	</div>
                       	<div class="col-md-12 hr-line-dashed"></div>
                       	<div class="col-md-12">
							<div class="panel-group" id="accordion">
								<div class="panel panel-default">
									<div class="panel-heading">
										<h4 class="panel-title">
											<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">
												<asset:image src="silver-pill-paypal-34px.png" /><i class="fa fa-question-circle" style="vertical-align: top;"></i>
											</a>
										</h4>
									</div>
									<div id="collapseTwo" class="panel-collapse collapse">
										<div class="panel-body">
											<g:if test="${tradesmanInstance?.paypalEnabled == true}">
												<div class="i-checks col-md-12" style="    padding-left: 15px !Important;"><label> <input id="paypalEnabled" type="checkbox" name="paypalEnabled" checked> <i></i> PayPal </label></div>		
											</g:if>
											<g:else>
												<div class="i-checks col-md-12" style="    padding-left: 15px !Important;"><label> <input id="paypalEnabled" type="checkbox" name="paypalEnabled"> <i></i> PayPal </label></div>
											</g:else>
											<div class="col-md-6">
												<div class="form-group">
													<label>Client ID</label>
													<input type="text" placeholder="Client ID" class="form-control" value="${tradesmanInstance?.paypal_clientId}" name="paypal_clientId">
												</div>
					                       	</div>
					                       	<div class="col-md-6">
												<div class="form-group">
													<label>Client Secret</label>
													<input type="text" placeholder="Client Secret" class="form-control" value="${tradesmanInstance?.paypal_clientSecret}" name="paypal_clientSecret">
												</div>
					                       	</div>
										</div>
									</div>
								</div>
								<div class="panel panel-default">
									<div class="panel-heading">
										<h4 class="panel-title">
											<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseAmazon">
												<asset:image src="Button_gold_S._V526162773_.png" /><i class="fa fa-question-circle" style="vertical-align: top; padding-left: 3px;"></i>
											</a>
										</h4>
									</div>
									<div id="collapseAmazon" class="panel-collapse collapse">
										<div class="panel-body">
											<g:if test="${tradesmanInstance?.amazonPayEnabled == true}">
												<div class="i-checks col-md-12" style="    padding-left: 15px !Important;"><label> <input id="amazonPayEnabled" type="checkbox" name="amazonPayEnabled" checked> <i></i> AmazonPay </label></div>		
											</g:if>
											<g:else>
												<div class="i-checks col-md-12" style="    padding-left: 15px !Important;"><label> <input id="amazonPayEnabled" type="checkbox" name="amazonPayEnabled"> <i></i> AmazonPay </label></div>
											</g:else>
											<div class="col-md-6">
												<div class="form-group">
													<label>Merchant ID</label>
													<input type="text" placeholder="Merchant ID" class="form-control" value="${tradesmanInstance?.amazon_merchantId}" name="amazon_merchantId">
												</div>
					                       	</div>
					                       	<div class="col-md-6">
												<div class="form-group">
													<label>Client ID</label>
													<input type="text" placeholder="Client ID" class="form-control" value="${tradesmanInstance?.amazon_clientId}" name="amazon_clientId">
												</div>
					                       	</div>
					                       	<div class="col-md-6">
												<div class="form-group">
													<label>Access Key</label>
													<input type="text" placeholder="Access Key" class="form-control" value="${tradesmanInstance?.amazon_accessKey}" name="amazon_accessKey">
												</div>
					                       	</div>
					                       	<div class="col-md-6">
												<div class="form-group">
													<label>Secret Key</label>
													<input type="text" placeholder="Secret Key" class="form-control" value="${tradesmanInstance?.amazon_secretKey}" name="amazon_secretKey">
												</div>
					                       	</div>
										</div>
									</div>
								</div>
                       		</div>
                       	</div>
                       	<div class="col-md-4" style="float: right;">
                       		<button type="submit" class="btn btn-primary block full-width m-b">Update Profile</button>
                       	</div>
                    </g:form>
                </div>
            </div>
        </div>