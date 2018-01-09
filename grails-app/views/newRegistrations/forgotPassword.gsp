<html>
<head>
	<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trader Ref | Reset Password</title>
    <asset:link rel="icon" href="TR Logo_blue.JPG" type="image/x-ico" />
    <asset:stylesheet src="css/bootstrap.min.css" />
    <asset:stylesheet src="font-awesome/css/font-awesome.css" />
    <asset:stylesheet src="css/plugins/iCheck/custom.css" />
    <asset:stylesheet src="css/animate.css" />
    <asset:stylesheet src="css/style.css" />
 	<asset:stylesheet src="css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" />
	<asset:stylesheet src="css/plugins/footable/footable.core.css"/>
	<asset:stylesheet src="traderapp.css"/>
</head>
<body class="gray-bg">
<div class="middle-box wrapper wrapper-content animated fadeInRight">
	<div class="row">
    	<div class="col-lg-12">
			<a href="/"><asset:image src="TradeReferLogo.jpg" style="border: 1px solid white; margin-top: 2%; margin-bottom: -6%;"/></a>
		</div>	
    	<div class="col-lg-12" style="margin-top: 8%;">
    		<div class="ibox float-e-margins">
				<div class="ibox-content">
					<div class="row" style="padding: 4%;">
						<h2 class="m-t-none m-b alignCenter">Reset Your Password</h2>
						<g:if test="${flash.message}">
							<g:if test="${flash.message == 'Success'}">
								<div class="alert alert-success alignCenter" role="alert" style="padding: 0px;">Email is sent to your registered email address. Please check your mail and follow the steps to reset your TradeRefer password.</div>
							</g:if>
							<g:else>
								<div class="alert alert-danger alignCenter" role="alert" style="padding: 0px;">${flash.message}</div>
							</g:else>
            			</g:if>
            			<div class="alert alert-danger alignCenter" role="alert" id="errorAlert" style="padding: 0px; display: none;">${flash.message}</div>
           				<div class="col-md-12" style=" padding-left: 17%; padding-right: 16%;">
           					<form role="form" id="forgotPassowrd" name="customerForm" action="${postUrl ?: '/newRegistrations/sendPasswordRecovery'}" method="POST"  autocomplete="off">
				            	<div class="form-group required">
									<label class='control-label'>Email Address</label>
						    		<input type="email" class="form-control" id="email" name="email" autocomplete="off" value="${contactEmail}" placeholder="Contact Email" onkeyup="validateEmail('email');" onmouseup="validateEmail('email');" required>
								</div>
								<button type="submit" class="btn btn-primary block full-width m-b">Send Password Change Email</button>
							</form>
           				</div>
						<div class="col-md-12">
							<h4 style="text-align: center;">- OR -</h4>
						</div>	
						<div class="col-md-12" style=" padding-left: 17%; padding-right: 16%;">
                			<p class="text-muted text-center"><small><a href="/">Login</a></small></p>
						</div>
           			</div>
           		</div>
           	</div>
		</div>
	</div>
</div>
	<asset:javascript src="js/jquery-3.1.1.min.js" />
    <asset:javascript src="js/bootstrap.min.js" />
    <asset:javascript src="application.js" />
    <!-- iCheck -->
    <asset:javascript src="js/plugins/iCheck/icheck.min.js" />
    <asset:javascript src="js/plugins/pwstrength/pwstrength-bootstrap.min.js" />
    <asset:javascript src="js/plugins/footable/footable.all.min.js" />
    <script>
        $(document).ready(function(){
            $('.i-checks').iCheck({
                checkboxClass: 'icheckbox_square-green',
                radioClass: 'iradio_square-green',
            });

            var options1 = {};
            options1.ui = {
                container: "#pwd-container",
                showVerdictsInsideProgressBar: true,
                viewports: {
                    progress: ".pwstrength_viewport_progress"
                }
            };
            options1.common = {
                debug: false
            };
            $('.example1').pwstrength(options1);
        });
    </script>
</body>
</html>