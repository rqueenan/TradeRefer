<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="customer_main" />
	</head>
	<body>
		<div class="row page-wrapper">
			<div class="middle-box text-center animated fadeInRightBig">
                <h3 class="font-bold">Your Payment for Invoice Number ${invoice?.invoiceNumber} was cancelled.</h3>
                <div class="error-desc">
                    <i class="fa fa-times-circle" aria-hidden="true" style="color: red; font-size: 35px;"></i>
                </div>
                <h3>Total Amount : &pound; ${invoice?.totalPrice}</h3>
            </div>
		</div>
	</body>
</html>