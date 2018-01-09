<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="customer_main" />
	</head>
	<body>
		<div class="row page-wrapper">
			<div class="middle-box text-center animated fadeInRightBig">
				<g:if test="${invoice}">
					<h3 class="font-bold">Your Payment for Invoice Number ${invoice?.invoiceNumber} has failed.</h3>
	                <div class="error-desc">
	                    <i class="fa fa-times-circle" aria-hidden="true" style="color: red; font-size: 35px;"></i>
	                </div>
	                <h3>Total Amount : ${invoice?.totalPrice} &pound;</h3>
				</g:if>
				<g:else>
					<h3>Error occurred while processing your payment</h3>
					<div class="error-desc">
	                    <i class="fa fa-times-circle" aria-hidden="true" style="color: red; font-size: 35px;"></i>
	                </div>
				</g:else>
            </div>
		</div>
	</body>
</html>