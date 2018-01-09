<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trader Ref | Login</title>
    <asset:link rel="icon" href="TR Logo_blue.JPG" type="image/x-ico" />
    <asset:stylesheet src="css/bootstrap.min.css" />
    <asset:stylesheet src="font-awesome/css/font-awesome.css" />
    <asset:stylesheet src="css/plugins/iCheck/custom.css" />
    <asset:stylesheet src="css/animate.css" />
    <asset:stylesheet src="css/style.css" />
    <asset:stylesheet src="css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" />
    <asset:stylesheet src="css/plugins/bootstrapSocial/bootstrap-social.css" />
	<style type="text/css">
		.login_message {
			padding: 6px 25px 20px 25px;
			color: #c33;
		}
		
		.middle-box{
			max-width: 800px !Important;
		}
		
		.gray-bg{
			background-color: #2f4050;
    		background-image: url('/assets/header-profile.png');
		}
	</style>
	<%--<script type="text/javascript" src="http://platform.linkedin.com/in.js">
    	api_key: 816bcn2rk1xhxf
    	onLoad: onLoad
    	authorize: true
	</script>
	<script type="text/javascript">
	
	function onLoad(){
		IN.User.logout();
	}
		function onLinkedInLoad() {
			IN.User.authorize(function(){
				getProfileData();
			});
	    }
	
		function onSuccess(data) {
	        console.log(data);
	        var name = data.values[0].firstName + " " + data.values[0].lastName;
	        var email = data.values[0].emailAddress;
	        var userId = data.values[0].id;
	        var proPic = "";
	        var thumb = "";
	        if(data.values[0].pictureUrls._total > 0){
	        	proPic = data.values[0].pictureUrls.values[0];
	        	thumb = data.values[0].pictureUrl;
		    }
	        if(name && email && userId){
	        	var f = document.createElement("form");
				f.setAttribute('method',"post");
				f.setAttribute('id',"linkedInUserCreation");
				f.setAttribute('action',"/newRegistrations/createLinkedInUser");
				f.setAttribute('style','display:none;');

				var name_f = document.createElement("input");
				name_f.type = "text";
				name_f.name = "name";
				name_f.id = "name_li";
				name_f.value = name;

				var email_f = document.createElement("input");
				email_f.type = "text";
				email_f.name = "email";
				email_f.id = "email_li";
				email_f.value = email;

				var userId_f = document.createElement("input");
				userId_f.type = "text";
				userId_f.name = "userId";
				userId_f.id = "userId_li";
				userId_f.value = userId;

				var proPic_f = document.createElement("input");
				proPic_f.type = "text";
				proPic_f.name = "proPic";
				proPic_f.id = "proPic_li";
				proPic_f.value = proPic;

				var thumb_f = document.createElement("input");
				thumb_f.type = "text";
				thumb_f.name = "thumb";
				thumb_f.id = "thumb_li";
				thumb_f.value = thumb;

				f.appendChild(name_f);
				f.appendChild(email_f);
				f.appendChild(userId_f);
				f.appendChild(proPic_f);
				f.appendChild(thumb_f);
				
				document.getElementsByTagName('body')[0].appendChild(f);
				document.getElementById("linkedInUserCreation").submit();
			}
	    }
	
		function onError(error) {
	        console.log(error);
	    }
	    
		function getProfileData() {
	        IN.API.Profile("me").fields(["firstName","lastName", "email-address", "id", "picture-url", "picture-urls::(original)"]).result(onSuccess).error(onError);
	    }
	</script>
--%></head>

<body class="gray-bg">
<script type="text/javascript">
	
	
	var accessToken = "";
	function logoutFb(){
		FB.getLoginStatus(function(response) {
	        if (response.status === 'connected') {
	            FB.logout(function(response) {
	                console.log(response);
	                location.href = "/logout";
	            });
	        }
	    });
	}
	function checkLoginState() {
		FB.getLoginStatus(function(response) {
			statusChangeCallback(response);
    	});
	}

	function statusChangeCallback(response) {
    	//console.log('statusChangeCallback');
    	//console.log(response);
    	if (response.status === 'connected') {
    		accessToken = response.authResponse.accessToken;
			getUserData(response);
    	}
	}

	function getUserData(tokenResponse) {
		//console.log('Welcome!  Fetching your information.... ');
    	FB.api('/me?fields=name,id,email', function(response) {
    		//console.log('Successful login for: ' + response.name);
    		//console.log(response);
    		if(response.name && response.email && response.id && accessToken){
				console.log("logging in using facebook");

				var f = document.createElement("form");
				f.setAttribute('method',"post");
				f.setAttribute('id',"fbUserCreation");
				f.setAttribute('action',"/newRegistrations/createFbUser");
				f.setAttribute('style','display:none;');
				
				var name = document.createElement("input");
				name.type = "text";
				name.name = "name";
				name.id = "name_fb";
				name.value = response.name;

				var email = document.createElement("input");
				email.type = "text";
				email.name = "email";
				email.id = "email_fb";
				email.value = response.email;

				var userId = document.createElement("input");
				userId.type = "text";
				userId.name = "userId";
				userId.id = "userId_fb";
				userId.value = response.id;
				
				var accessToken_fb = document.createElement("input");
				accessToken_fb.type = "text";
				accessToken_fb.name = "accessToken";
				accessToken_fb.id = "accessToken_fb";
				accessToken_fb.value = accessToken;
				f.appendChild(name);
				f.appendChild(email);
				f.appendChild(userId);
				f.appendChild(accessToken_fb);

				document.getElementsByTagName('body')[0].appendChild(f);

				document.getElementById("fbUserCreation").submit();
        	}
    	});
	}

	window.fbAsyncInit = function() {
		FB.init({
			appId      : '275097942995686',
      		xfbml      : true,
      		version    : 'v2.9',
      		cookie	 : true
    	});
	};

	(function(d, s, id) {
		var js, fjs = d.getElementsByTagName(s)[0];
		if (d.getElementById(id)) return;
		js = d.createElement(s); js.id = id;
		js.async=true;    
		js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.9&appId=275097942995686";
		fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));
</script>
    <div class="middle-box wrapper wrapper-content animated fadeInRight">
    	<div class="row">
    		<div class="col-lg-12">
    			<a href="/"><asset:image src="TradeReferLogo.jpg" style="border: 1px solid white; margin-top: 2%; margin-bottom: -6%;"/></a>
    		</div>		
            <div class="col-lg-12" style="margin-top: 8%;">
                <div class="ibox float-e-margins">
                    <div class="ibox-content">
                        <div class="row">
                            <div class="col-sm-6 b-r">
                            	<h2 class="m-t-none m-b">Sign in</h2>
                                <g:if test='${flash.message}'>
									<div class="login_message">${flash.message}</div>
								</g:if>
								<br/>
                                <form class="m-t" role="form" action="${postUrl ?: '/login/authenticate'}" method="POST" id="loginForm" autocomplete="off">
						       		<div class="form-group">
						       			<label>Username</label>
						            	<input type="text" class="form-control" name="${usernameParameter ?: 'username'}" id="username" placeholder="Username" required/>
						          	</div>
						          	<br/>
						           	<div class="form-group">
						           		<label>Password</label>
						            	<input type="password" class="form-control" name="${passwordParameter ?: 'password'}" id="password" placeholder="Password" required/>
						           	</div>
						           	<br/>
						          	<button type="submit" class="btn btn-primary block full-width m-b">Login</button>
						           	<br/>
						           	<div class="form-group">
					                	<label> <input type="checkbox" class="i-checks" name="remember-me"><i></i> Remember Me </label>
					                	<g:link class="btn forget-btn" controller='newRegistrations' action='forgotPassword'>
											Forgot password?
										</g:link>
					                </div>
						      	</form> 
                            </div>
                            <div class="col-sm-6"><h2>Not a member?</h2>
                                <h3>You can create an account now</h3>
                                <br>
                                <p class="text-center">
                                    <g:link class="btn btn-warning btn-lg btn-block" controller="newRegistrations" action="createNewCustomer">Create Customer Account</g:link>
                                    <br>
	           						<g:link class="btn btn-info btn-lg btn-block" controller="newRegistrations" action="createNewTradesman">Create Tradesman Account</g:link>
                                </p>
                                <div class="col-md-12" style="padding-left: 17%; padding-right: 16%;">
	                				<p class="text-muted text-center">- OR -</p>
						      	</div>
						      	<p><fb:login-button size="medium" data-height="45px" data-width="215px"  class=" fb-login-button" scope="email" data-button-type="login_with" onlogin="checkLoginState();" style="padding-left: 18%;" ></fb:login-button></p>
						      	<p style="padding-left: 18%;">
						      		<a style="width: 215px; padding-left: 17%;" class="btn btn-block btn-sm btn-social btn-linkedin" href="https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=816bcn2rk1xhxf&redirect_uri=https://traderefer.co.uk/newRegistrations/loginWithLinkedIn&state=${new Date()?.getTime()}&scope=r_basicprofile r_emailaddress">
                            			<span class="fa fa-linkedin"></span> Sign in with LinkedIn
                        			</a>
                        		</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    	</div>
    </div>
     <!-- Mainly scripts -->
    <asset:javascript src="js/jquery-3.1.1.min.js" />
    <asset:javascript src="js/bootstrap.min.js" />
    <!-- iCheck -->
    <asset:javascript src="js/plugins/iCheck/icheck.min.js" />
    
    <script>
    	
        $(document).ready(function(){
            $('.i-checks').iCheck({
                checkboxClass: 'icheckbox_square-green',
                radioClass: 'iradio_square-green',
            });
        });
    </script>
</body>
</html>
