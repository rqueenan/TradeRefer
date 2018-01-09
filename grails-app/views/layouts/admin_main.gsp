<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Trader Ref | Admin Dashboard</title>

    <asset:stylesheet src="css/bootstrap.min.css" />
    <asset:stylesheet src="font-awesome/css/font-awesome.css" />
    <asset:stylesheet src="css/animate.css" />
    <asset:stylesheet src="css/style.css" />
    <asset:stylesheet src="css/plugins/iCheck/custom.css" />
    <asset:stylesheet src="css/plugins/jquery-ui/jquery-ui.min.css" />
    <asset:stylesheet src="css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" />
	<asset:stylesheet src="css/plugins/datapicker/datepicker3.css" />
	<asset:stylesheet src="bootstrap-switch.css" />
	<asset:stylesheet src="css/plugins/footable/footable.core.css"/>
	<asset:stylesheet src="traderapp.css"/>
   	<asset:stylesheet src="css/plugins/cropper/cropper.min.css" />
   	<asset:stylesheet src="css/star-rating.css"/>

</head>

<body>
	<div id="wrapper">
        <nav class="navbar-default navbar-static-side" role="navigation">
            <div class="sidebar-collapse">
                <ul class="nav metismenu" id="side-menu">
                	<li class="logo_li" style="    padding-top: 3%; padding-left: 3.5%;">
                		<asset:image src="TradeReferLogo.jpg" style="border:1px solid white"/>
                	</li>
                    <li>&nbsp;</li>
                    <li class="active">
                    	<g:link controller="admin" action="settings"><i class="fa fa-cog"></i><span class="nav-label">Settings</span></g:link>
                    </li>
                    <li>
                        <g:link controller="admin" action="customerList"><i class="fa fa-users"></i> <span class="nav-label">Customers</span></g:link>
                    </li>
                    <li>
                        <g:link controller="admin" action="tradesmanList"><i class="fa fa-wrench"></i> <span class="nav-label">Tradesmen</span>  </g:link>
                    </li>
                </ul>
            </div>
        </nav>
        <div id="page-wrapper" class="gray-bg dashbard-1">
        	<div class="row border-bottom">
        		<nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
            		<ul class="nav navbar-top-links navbar-right">
                		<li>
                			<div class="avatar">
                				<g:link controller="admin" action="settings"><asset:image style="width: 100%; height: 100%; border-radius: 100%; border: 2px solid #1ab394;" src="img/admin_profile.png" /></g:link>
                			</div>
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
    <asset:javascript src="application.js" />
    <asset:javascript src="js/jquery-3.1.1.min.js" />
    <asset:javascript src="js/bootstrap.min.js" />
   	<asset:javascript src="js/plugins/iCheck/icheck.min.js" />
   	<asset:javascript src="js/plugins/jquery-ui/jquery-ui.min.js" />
   	<asset:javascript src="js/plugins/datapicker/bootstrap-datepicker.js"/>
   	<asset:javascript src="js/plugins/metisMenu/jquery.metisMenu.js" />
   	<asset:javascript src="js/plugins/slimscroll/jquery.slimscroll.min.js" />
   	<asset:javascript src="js/inspinia.js" />
   	<asset:javascript src="js/plugins/pace/pace.min.js" />
   	<asset:javascript src="js/plugins/typehead/bootstrap3-typeahead.js" />
   	<asset:javascript src="bootstrap-switch.js" />
   	<asset:javascript src="js/plugins/footable/footable.all.min.js" />
   	<asset:javascript src="js/plugins/cropper/cropper.min.js" />
   	<asset:javascript src="js/star-rating.js"/>
    <script>
        $(document).ready(function(){
            $('.i-checks').iCheck({
                checkboxClass: 'icheckbox_square-green',
                radioClass: 'iradio_square-green',
            });
        });
        
	   	$('.metismenu a').each(function() {
	   		var path = window.location.href;
	        if (path.includes(this.href)) {
	
	            $(this).closest('li').addClass('active');
	        } else {
	            $(this).closest('li').removeClass('active');
	        }
	    });
    </script>
   
</body>
</html>
