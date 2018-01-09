<meta name="layout" content="admin_main" />
<style type="text/css">
.ibox-content{
	border-style: none;
}
</style>
<div class="row page-wrapper">
	<div class="ibox-content">
		<g:form controller="admin" action="saveAdminSettings" id="${settings?.id}">
			<div class="col-md-3">
				<div class="form-group required">
					<label class="control-label">VAT (%)</label>
					<input type="text" placeholder="VAT" class="form-control" value="${settings?.VAT}" name="vat" required>
				</div>
			</div>
			<div class="col-md-12"></div>
			<div class="col-md-3">
           		<button type="submit" class="btn btn-primary block full-width m-b">Update Settings</button>
           	</div>
		</g:form>                  
	</div>
</div>