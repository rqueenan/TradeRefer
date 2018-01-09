<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trader Ref | Login</title>
    <asset:stylesheet src="css/bootstrap.min.css" />
    <asset:stylesheet src="font-awesome/css/font-awesome.css" />
    <asset:stylesheet src="css/plugins/iCheck/custom.css" />
    <asset:stylesheet src="css/animate.css" />
    <asset:stylesheet src="css/style.css" />
    <asset:stylesheet src="css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" />
    <asset:stylesheet src="css/plugins/bootstrapSocial/bootstrap-social.css" />	
    <script type="text/javascript">
	    checkLinkedINLogin();
		function parseQueryString(url) {
		  var urlParams = {};
		  url.replace(
		    new RegExp("([^?=&]+)(=([^&]*))?", "g"),
		    function($0, $1, $2, $3) {
		      urlParams[$1] = $3;
		    }
		  );
		  
		  return urlParams;
		}
		function checkLinkedINLogin(){
			var location = window.location.href;
			console.log(location);
			var result = parseQueryString(location);  
			if(result.code){
				console.log("has code");
				var code = result.code;
				var state = result.state;
				if(code && state){
					window.location.href = "/newRegistrations/getAccessToken?code="+code;		
				} 
			} else{
				console.log("no code");
				window.location.href = "/";
			}
		}
    </script>
</head>

<body class="gray-bg">
    <div class="middle-box wrapper wrapper-content animated fadeInRight">
    	<div class="row">
    		<div class="col-lg-12">
    			<asset:image src="TradeReferLogo.jpg" style="border: 1px solid white; margin-top: 2%; margin-bottom: -6%;"/>
    		</div>		
    	</div>
    </div>
     <!-- Mainly scripts -->
    <asset:javascript src="js/jquery-3.1.1.min.js" />
    <asset:javascript src="js/bootstrap.min.js" />
    <!-- iCheck -->
    <asset:javascript src="js/plugins/iCheck/icheck.min.js" />
</body>
</html>