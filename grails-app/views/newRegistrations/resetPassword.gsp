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
						<h2 class="m-t-none m-b alignCenter">Reset Password</h2>
            			<g:if test="${flash.message}">
           					<div class="alert alert-danger alignCenter" role="alert" style="padding: 0px;">${flash.message}</div>
           				</g:if>
           				<div class="alert alert-danger alignCenter" role="alert" id="errorAlert" style="padding: 0px; display: none;">${flash.message}</div>
            			<div class="col-md-12" style=" padding-left: 17%; padding-right: 16%;">
           					<form role="form" id="resetPassword" name="resetPassword" action="${postUrl ?: '/newRegistrations/resetPassword'}" method="POST"  autocomplete="off">
				            	<div id="pwd-container">
				            		<g:hiddenField name='t' value='${token}' />
					            	<div class="form-group required">
											<label class='control-label'>Password</label>
							    			<input type="password" class="form-control example1" id="password" autocomplete="off" name="password" placeholder="Password" onkeyup="validatePassword('password');" onmouseup="validatePassword('password');" title="Your password must contain 
- atleast 8 characters 
- atleast one numeric character
- atleast one lowercase letter 
- atleast one uppercase letter" required>
										</div>
										<div class="form-group">
							            	<div class="pwstrength_viewport_progress"></div>
							            </div>
										<div class="form-group required">
											<label class='control-label'>Confirm New Password</label>
							    			<input type="password" class="form-control" autocomplete="off" id="reEnteredPassword" name="password2" onkeyup="validateReEnteredPassword('reEnteredPassword','password');" onmouseup="validateReEnteredPassword('reEnteredPassword','password');" placeholder="Re-enter new password" required>
										</div>
									<button type="submit" class="btn btn-primary block full-width m-b">Update Password</button>
					            	</div>
							</form>
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