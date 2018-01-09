<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Trader Ref | Tradesman Dashboard</title>
    <asset:link rel="icon" href="TR Logo_blue.JPG" type="image/x-ico" />
    <asset:stylesheet src="css/bootstrap.min.css" />
    <asset:stylesheet src="font-awesome/css/font-awesome.css" />
    <asset:stylesheet src="css/animate.css" />
    <asset:stylesheet src="css/style.css" />
    <asset:stylesheet src="css/plugins/iCheck/custom.css" />
    <asset:stylesheet src="css/plugins/jQueryUI/jquery-ui.css" />
    <asset:stylesheet src="css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" />
	<asset:stylesheet src="css/plugins/datapicker/datepicker3.css" />
	<asset:stylesheet src="bootstrap-switch.css" />
	<asset:stylesheet src="css/plugins/footable/footable.core.css"/>
	<asset:stylesheet src="traderapp.css"/>
   	<asset:stylesheet src="css/plugins/cropper/cropper.min.css" />
   	<asset:stylesheet src="css/star-rating.css"/>
   	<style type="text/css">
   		@media (min-width: 768px){
			.navbar-right {
				margin-right: 0px !Important;
			}
		}
   	</style>
</head>

<body>
    <div id="wrapper">
        <nav class="navbar-default navbar-static-side" role="navigation">
            <div class="sidebar-collapse">
                <ul class="nav metismenu" id="side-menu">
                	<li class="nav-header logo_li" style="    padding-top: 3%; padding-left: 3.5%; padding-bottom: 3.5%; border-left: none !Important;">
                		<div class="dropdown profile-element">
                			<a href="/"><asset:image src="TradeReferLogo.jpg" style="border:1px solid white"/></a>
                		</div>
                		<div class="logo-element" style="padding-top: 14%; padding-bottom: 23%; padding-right: -4%; margin-right: 4px;">
                            <a href="/"><asset:image src="TR Logo_blue.JPG" style="border:1px solid white" style="width:80%;" /></a>
                        </div>
                	</li>
                	<%--<li class="logo_li" style="    padding-top: 3%; padding-left: 3.5%;">
                		
                	</li>
                    --%><li>&nbsp;</li>
					<li>
                        <g:link controller="tradesman" action="index"><i class="fa fa-user-circle"></i> <span class="nav-label">Profile</span></g:link>
                    </li>
                    <li>
                        <g:link controller="tradesman" action="showJobs"><i class="fa fa-wrench"></i> <span class="nav-label">Jobs</span></g:link>
                    </li>
                    <li>
                        <g:link controller="invoice" action="index"><i class="fa fa-money"></i> <span class="nav-label">Invoices</span></g:link>
                    </li>
                </ul>
            </div>
        </nav>
        <div id="page-wrapper" class="gray-bg dashbard-1" style="min-height: 100% !Important;">
        	<div class="row border-bottom">
        		<nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
        			<div class="navbar-header">
        				<a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>
        			</div>
            		<ul class="nav navbar-top-links navbar-right">
                		<li>
                			<div class="avatar">
                				<g:if test="${tradesmanInstance?.profilePic}">
	                        		<g:link controller="tradesman" action="index"><img class="Photo" style="width: 100%; height: 100%; border-radius: 100%;  border: 2px solid #1ab394;" src="${createLink(controller:'tradesman', action:'viewProfilePic')}" /></g:link>
	                        	</g:if>
	                        	<g:else>
	                        		<g:link controller="tradesman" action="index"><asset:image style="width: 100%; height: 100%; border-radius: 100%; border: 2px solid #1ab394;" src="img/admin_profile.png" /></g:link>
	                        	</g:else>
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
   	<div class="modal inmodal" id="editPictureModal" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content animated bounceInRight">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span>
						<span class="sr-only">Close</span>
					</button>
					<h2>Change Profile Picture</h2>
           		</div>
           		<div class="modal-body">
           			<div class="row">
		            	<div class="col-lg-12">
		                	<div class="ibox float-e-margins">
		                    	<div class="ibox-content">
		                        	<div class="row">
		                        		<div class="col-md-6">
				                        	<div class="image-crop">
				                        		<asset:image src="img/p3.jpg" />
				                        	</div>
				                    	</div>
				                    	<div class="col-md-6">
				                        	<div class="img-preview img-preview-sm"></div>
				                        	<br/>
				                        	<div class="btn-group" style="margin-left: 8.7%;">
				                            	<label title="Select image file" for="inputImage" class="btn btn-primary">
				                                	<input type="file" accept="image/*" name="file" id="inputImage" class="hide">
				                                	Select Image
				                            	</label>
				                            	<label title="Upload Image" id="download" class="btn btn-primary">Upload</label>
				                        	</div>
				                    	</div>
		                        	</div>
		                  		</div>
		              		</div>
		           		</div>
		           	</div>
           		</div>
           		<div class="modal-footer"></div>
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
   	<script type="text/javascript">
	   	$('.metismenu a').each(function() {
	   		var path = window.location.href;
	        if (path.includes(this.href)) {
	
	            $(this).closest('li').addClass('active');
	        } else {
	            $(this).closest('li').removeClass('active');
	        }
	    });
	    $(document).ready(function(){
	    	var $image = $(".image-crop > img")
            $($image).cropper({
                aspectRatio: 1.618,
                preview: ".img-preview",
                done: function(data) {
                    // Output the result data for cropping image.
                }
            });

            var $inputImage = $("#inputImage");
            if (window.FileReader) {
                $inputImage.change(function() {
                    var fileReader = new FileReader(),
                            files = this.files,
                            file;

                    if (!files.length) {
                        return;
                    }

                    file = files[0];

                    if (/^image\/\w+$/.test(file.type)) {
                        fileReader.readAsDataURL(file);
                        fileReader.onload = function () {
                            $inputImage.val("");
                            $image.cropper("reset", true).cropper("replace", this.result);
                        };
                    } else {
                        showMessage("Please choose an image file.");
                    }
                });
            } else {
                $inputImage.addClass("hide");
            }

            $("#download").click(function() {
                window.open($image.cropper("getDataURL"));
            });

            $("#zoomIn").click(function() {
                $image.cropper("zoom", 0.1);
            });

            $("#zoomOut").click(function() {
                $image.cropper("zoom", -0.1);
            });

            $("#rotateLeft").click(function() {
                $image.cropper("rotate", 45);
            });

            $("#rotateRight").click(function() {
                $image.cropper("rotate", -45);
            });

            $("#setDrag").click(function() {
                $image.cropper("setDragMode", "crop");
            });
		});
   	</script>
</body>
</html>
