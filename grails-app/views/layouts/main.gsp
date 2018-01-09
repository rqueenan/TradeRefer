<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>Trader Ref</title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
	<asset:stylesheet src="css/bootstrap.min.css" />
    <asset:stylesheet src="font-awesome/css/font-awesome.css" />

    <!-- Toastr style -->
    <asset:stylesheet src="css/plugins/toastr/toastr.min.css" />

    <!-- Gritter -->
    <asset:stylesheet src="js/plugins/gritter/jquery.gritter.css" />

    <asset:stylesheet src="css/animate.css" />
    <asset:stylesheet src="css/style.css" />
</head>
<body>
<div id="wrapper">
	<nav class="navbar-default navbar-static-side" role="navigation">
    	<div class="sidebar-collapse">
        	<ul class="nav metismenu" id="side-menu">
            	<li class="nav-header">
                	<div class="dropdown profile-element"> 
                		<span><img alt="image" class="img-circle" src="img/profile_small.jpg" /></span>
                 		<a data-toggle="dropdown" class="dropdown-toggle" href="#">
                        	<span class="clear"> 
                        		<span class="block m-t-xs"> <strong class="font-bold">David Williams</strong></span> 
                        		<span class="text-muted text-xs block">Art Director <b class="caret"></b></span> 
                        	</span> 
                        </a>
                      	<ul class="dropdown-menu animated fadeInRight m-t-xs">
                        	<li><a href="profile.html">Profile</a></li>
                            <li class="divider"></li>
                            <li><a href="login.html">Logout</a></li>
						</ul>
						
                 	</div>
              	</li>
              	<li>
              		<a href="layouts.html"><i class="fa fa-diamond"></i> <span class="nav-label">Layouts</span></a>
            	</li>
        	</ul>
        </div>
   	</nav>
   	<div id="page-wrapper" class="gray-bg dashbard-1">
        <div class="row border-bottom">
        	<nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
            	<ul class="nav navbar-top-links navbar-right">
                	<li>
                    	<span class="m-r-sm text-muted welcome-message">Welcome to Trader Ref</span>
                	</li>
                	<li>
                    	<a href="/logout">
                        	<i class="fa fa-sign-out"></i> Log out
                    	</a>
                	</li>
            	</ul>
        	</nav>
        </div>
        <div class="row  border-bottom white-bg dashboard-header">
			 <g:layoutBody/>
  		</div>
	</div>
</div>


   
	<!-- Mainly scripts -->
    <asset:javascript src="js/jquery-3.1.1.min.js" />
    <asset:javascript src="js/bootstrap.min.js" />
    <asset:javascript src="js/plugins/metisMenu/jquery.metisMenu.js" />
    <asset:javascript src="js/plugins/slimscroll/jquery.slimscroll.min.js" />

    <!-- Flot -->
    <asset:javascript src="js/plugins/flot/jquery.flot.js" />
    <asset:javascript src="js/plugins/flot/jquery.flot.tooltip.min.js" />
    <asset:javascript src="js/plugins/flot/jquery.flot.spline.js" />
    <asset:javascript src="js/plugins/flot/jquery.flot.resize.js" />
    <asset:javascript src="js/plugins/flot/jquery.flot.pie.js" />

    <!-- Peity -->
    <asset:javascript src="js/plugins/peity/jquery.peity.min.js" />
    <asset:javascript src="js/demo/peity-demo.js" />

    <!-- Custom and plugin javascript -->
    <asset:javascript src="js/inspinia.js" />
    <asset:javascript src="js/plugins/pace/pace.min.js" />

    <!-- jQuery UI -->
    <asset:javascript src="js/plugins/jquery-ui/jquery-ui.min.js" />

    <!-- GITTER -->
    <asset:javascript src="js/plugins/gritter/jquery.gritter.min.js" />

    <!-- Sparkline -->
    <asset:javascript src="js/plugins/sparkline/jquery.sparkline.min.js" />

    <!-- Sparkline demo data  -->
    <asset:javascript src="js/demo/sparkline-demo.js" />

    <!-- ChartJS-->
    <asset:javascript src="js/plugins/chartJs/Chart.min.js" />

    <!-- Toastr -->
    <asset:javascript src="js/plugins/toastr/toastr.min.js" />

    <asset:javascript src="application.js"/>

</body>
</html>
