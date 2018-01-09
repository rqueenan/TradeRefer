 <meta name="layout" content="admin_main" />
 <div class="row page-wrapper">
 	<div class="col-lg-10">
		<div class="ibox float-e-margins">
			<table class="table table-hover">
               	<thead>
                   	<tr>
                    	<th>#</th>
                        <th>Contact Name</th>
                        <th>Contact Email</th>
                        <th>Contact Number</th>
                        <th>Enabled</th>
                  	</tr>
               	</thead>
               	<tbody>
                	<g:each in="${tradesmen}" var="t" status="i">
                		<tr>
	                     	<td>${i+1}</td>
                           	<td>${t?.name}</td>
                           	<td>${t?.companyEmail}</td>
                           	<td>${t?.phone}</td>
                           	<td>
                           		<g:if test="${t?.user?.enabled == true}">
                           			<i class="fa fa-check-circle-o text-navy fontSize_userStatus" style="font-size: 22px;"></i>
                           		</g:if>
                           		<g:else>
                           			<i class="fa fa-times-circle-o text-danger fontSize_userStatus" style="font-size: 22px;"></i>
                           		</g:else>
                           	</td>
	                  	</tr>
	               	</g:each>
              	</tbody>
	       	</table>
     	</div>
     </div>
 </div>