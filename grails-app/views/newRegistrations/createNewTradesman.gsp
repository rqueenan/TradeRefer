<html>
<head>
	<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trader Ref | Register New Tradesman</title>
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
                        <div class="row">
                        	<h2 class="m-t-none m-b alignCenter">Tradesman Registration</h2>
                        	<g:if test="${flash.message}">
            					<div class="alert alert-danger alignCenter" role="alert" style="padding: 0px;">${flash.message}</div>
            				</g:if>
            				<div class="alert alert-danger alignCenter" role="alert" id="errorAlert" style="padding: 0px; display: none;">${flash.message}</div>
            				<form role="form" id="tradesmanForm" name="tradesmanForm" action="${postUrl ?: '/newRegistrations/saveNewTradesman'}" method="POST"  autocomplete="off">
            					<div id="pwd-container">
            						<div class="col-sm-6 b-r">
            							<div class="form-group required">
            								<label class='control-label'>Contact Name</label>
							    			<input type="text" class="form-control" id="name" name="name" value="${contactName}" placeholder="Contact Name" onkeyup="validateTextBox('name');" onmouseup="validateTextBox('name');" required>
										</div>
										<div class="form-group required">
											<label class='control-label'>Company Email</label>
							    			<input type="email" class="form-control" id="email" value="${companyEmail}" name="email" autocomplete="off" placeholder="Company Email" onkeyup="validateEmail('email');" onmouseup="validateEmail('email');" required>
										</div>
										<div class="form-group required">
											<label class='control-label'>Password</label>
							    			<input type="password" class="form-control example1"  autocomplete="off" id="password" name="password" placeholder="Password" onkeyup="validatePassword('password');" onmouseup="validatePassword('password');" title="Your password must contain 
- atleast 8 characters 
- atleast one numeric character
- atleast one lowercase letter 
- atleast one uppercase letter" required><%--<i class="fa fa-question-circle"></i>
										--%></div>
										<div class="form-group">
						                    <div class="pwstrength_viewport_progress"></div>
						                </div>
										<div class="form-group required">
											<label class='control-label'>Re-Enter Password</label>
							    			<input type="password" class="form-control" id="reEnteredPassword" autocomplete="off" name="reEnteredPassword" onkeyup="validateReEnteredPassword('reEnteredPassword','password');" onmouseup="validateReEnteredPassword('reEnteredPassword','password');" placeholder="Re-enter password" required>
										</div>
            						</div>
            						<div class="col-sm-6">
            							<div class="form-group required">
											<label class='control-label'>Company Name</label>
							    			<input type="text" class="form-control" id="companyName" value="${companyName}" name="companyName" placeholder="Company Name" onkeyup="validateTextBox('companyName');" onmouseup="validateTextBox('companyName');" required>
										</div>
										<input type="hidden" name="jobId" value="${jobId}">
										<div class="form-group col-md-12">
											<p class="alignLeft">Please select the work type you wish to be contacted for : </p>
											<div class="i-checks col-md-6 alignLeft"><label> <input id="workType_1" type="checkbox" name="workType_Electrical" > <i></i> Electrical </label></div>
											<div class="i-checks col-md-6 alignLeft"><label> <input id="workType_2" type="checkbox" name="workType_Plumbing" > <i></i> Plumbing </label></div>
											<div class="i-checks col-md-6 alignLeft"><label> <input id="workType_4" type="checkbox" name="workType_Gas" > <i></i> Gas </label></div>
											<div class="i-checks col-md-6 alignLeft"><label> <input id="workType_5" type="checkbox" name="workType_Plastering" > <i></i> Plastering </label></div>
											<div class="i-checks col-md-12 alignLeft"><label> <input id="workType_6" type="checkbox" name="workType_Tiling" > <i></i> Tiling </label></div>
											<div class="i-checks col-md-12 alignLeft"><label> <input id="workType_3" type="checkbox" name="workType_Painting_Decorating" > <i></i> Painting / Decorating </label></div>
										</div>
										<div class="form-group">
											<button type="submit" class="btn btn-primary block full-width m-b">Register</button>
											</br>
											<p class="text-muted text-center"><small>Already have an account?  <a href="/">Login</a></small></p>
										</div>
            						</div>
            					</div>
            				</form>
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