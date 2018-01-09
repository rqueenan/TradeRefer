<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="linkedIn_main" />
	</head>
	<body>
		<div class="row page-wrapper">
			<div class="middle-box text-center animated fadeInRightBig">
                <h3 class="font-bold">Your Payment is successful.</h3>
                <div class="error-desc">
                    <i class="fa fa-check-circle" aria-hidden="true" style="color: #06ca06; font-size: 35px;"></i>
                </div>
                <h3>Invoice Number : ${invoice?.invoiceNumber}</h3>
                <h3>Total Amount Paid : ${invoice?.totalPrice} &pound;</h3>
                <h3>Payment Method : ${invoice?.paymentType}</h3>
                <h3>Transaction ID : ${invoice?.transactionID}</h3>
            </div>
		</div>
	</body>
</html>