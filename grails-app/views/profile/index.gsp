<g:if test="${role == 'ROLE_CUSTOMER'}">
<meta name="layout" content="customer_main" />
</g:if>
<g:elseif test="${role == 'ROLE_FACEBOOK_CUSTOMER'}">
<meta name="layout" content="facebook_main" />
</g:elseif>
<g:elseif test="${role == 'ROLE_LINKEDIN_CUSTOMER'}">
<meta name="layout" content="linkedIn_main" />
</g:elseif>
<div class="col-md-12 page-wrapper">
	<div class="ibox float-e-margins">
		<div class="ibox-content"  style="border: 0px;">
			<g:uploadForm controller='profile' method="post" action='uploadProfilePicture'>
				<div id="showProfilePicture" class="col-md-3" class="no-padding border-left-right col-md-4" style="clear: left; float: left;">
					<g:if test="${customer?.profilePic}">
						<img src="${createLink(controller:'profile', action:'viewProfilePic')}" alt="your image" style="width: 100%; height: 100%;" />
					</g:if>
					<g:else>
						<g:if test="${role == 'ROLE_FACEBOOK_CUSTOMER'}">
							<g:if test="${customer?.UID}">
                				<img src="https://graph.facebook.com/${customer?.UID}/picture?type=large" style="width: 100%; height: 100%;" />
                			</g:if>
						</g:if>
						<g:elseif test="${role == 'ROLE_LINKEDIN_CUSTOMER'}">
							<g:if test="${customer?.originalProPicURL}">
                        		<img src="${customer?.originalProPicURL}" style="width: 100%; height: 100%;" />
                        	</g:if>
						</g:elseif>
					</g:else>
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
			<g:form controller="profile" action="updateProfile" id="${customer?.id}">
				<div class="col-md-4" style="float: right;">
					<button type="submit" class="btn btn-primary block full-width m-b">Update Profile</button>
				</div>
				<h4 class="m-t-none m-b">Personal Details</h4>
				<div class="col-md-4">
					<div class="form-group required">
						<label class="control-label">Name</label>
						<input type="text" placeholder="Contact Name" class="form-control" value="${customer?.name}" name="name" required>
					</div>
                </div>
                <div class="col-md-4">
					<div class="form-group required">
						<label class="control-label">Email</label>
						<input type="email" placeholder="Email" class="form-control" value="${customer?.email}" name="companyEmail" disabled="disabled">
					</div>
                </div>
                <div class="col-md-12"></div>
                <div class="col-md-4">
					<div class="form-group required">
						<label class="control-label">Address Line 1</label>
						<input type="text" placeholder="Address Line 1" class="form-control" value="${customer?.addressLine1}" name="addressLine1" required>
					</div>
				</div>
				<div class="col-md-4">
					<div class="form-group">
						<label>Address Line 2</label>
						<input type="text" placeholder="Address Line 2" class="form-control" value="${customer?.addressLine2}" name="addressLine2">
					</div>
                </div>
                <div class="col-md-4">
					<div class="form-group">
						<label>Address Line 3</label>
						<input type="text" placeholder="Address Line 3" class="form-control" value="${customer?.addressLine3}" name="addressLine3">
					</div>
                </div>
                <div class="col-md-4">
					<div class="form-group required">
						<label class="control-label">Post Code</label>
						<input type="text" placeholder="Post Code" class="form-control" value="${customer?.postcode}" name="postcode" required>
					</div>
                </div>
                <div class="col-md-4">
					<div class="form-group">
						<label>Phone</label>
						<input type="text" placeholder="Phone" class="form-control" value="${customer?.phone}" name="phone">
					</div>
                </div>
                <div class="col-md-12 hr-line-dashed"></div>
                <div class="col-md-12">
                	<div class="form-group">
						<h4 class="m-t-none m-b">Payment Method</h4>
						<g:if test="${customer?.paymentMethod?.contains('PayPal')}">
							<div class="i-checks col-md-2 alignLeft"><label> <input id="paymentMethod_1" type="checkbox" name="paymentMethod_paypal" checked> <i></i> <img src="https://www.paypalobjects.com/webstatic/en_US/i/btn/png/silver-pill-paypal-34px.png" /> </label></div>		
						</g:if>
						<g:else>
							<div class="i-checks col-md-2 alignLeft"><label> <input id="paymentMethod_1" type="checkbox" name="paymentMethod_paypal"> <i></i> <img src="https://www.paypalobjects.com/webstatic/en_US/i/btn/png/silver-pill-paypal-34px.png" /></label></div>
						</g:else>
						<g:if test="${customer?.paymentMethod?.contains('AmazonPay')}">
							<div class="i-checks col-md-3 alignLeft"><label> <input id="paymentMethod_2" type="checkbox" name="paymentMethod_amazonpay" checked> <i></i> <img src="https://images-na.ssl-images-amazon.com/images/G/01/amazonservices/payments/website/Button_gold_S._V526162773_.png"/> </label></div>		
						</g:if>
						<g:else>
							<div class="i-checks col-md-3 alignLeft"><label> <input id="paymentMethod_2" type="checkbox" name="paymentMethod_amazonpay" > <i></i> <img src="https://images-na.ssl-images-amazon.com/images/G/01/amazonservices/payments/website/Button_gold_S._V526162773_.png"/> </label></div>
						</g:else>
					</div>
                </div>
                <div class="col-md-4" style="float: right;">
                	<button type="submit" class="btn btn-primary block full-width m-b">Update Profile</button>
                </div>	
			</g:form>
		</div>
	</div>
</div>