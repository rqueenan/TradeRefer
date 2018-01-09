<div class="modal-body col-md-12" style="box-shadow: 0 1px 3px rgba(0, 0, 0, 0.3); border: 1px solid rgba(0, 0, 0, 0);">
	<g:if test="${job?.feedback}">
		<div style="padding-left: 20%; padding-right: 20%; text-align: center;">
			<h2>${job?.feedback?.comment}</h2>
			<input id="input-3" name="input-3" value="${job?.feedback?.starCounts}" data-readonly="true" class="rating">
		</div>
	</g:if>
	<g:else>
		<g:form controller="jobs" action="saveFeedback" id="${job?.id}" style="padding-left: 20%; padding-right: 20%;">
			<input id="input-id" name="feedbackCount" type="text" class="rating" data-size="lg" data-show-caption="false" >
			<div class="form-group">
				<textarea name="comment" placeholder="Add Comment" rows="3" class="form-control"></textarea>
			</div>
			<g:hiddenField id="feedback_max" value="${max}" name="max" />
			<g:hiddenField id="feedback_offset" value="${offset}" name="offset" />
			<g:hiddenField id="feedback_sort" value="${sort}" name="sort" />
			<g:hiddenField id="feedback_order" value="${order}" name="order" />
			<button type="submit" class="btn btn-success" style="float: right;">Send Feedback</button>
		</g:form>
	</g:else>
</div>
<script type="text/javascript">
$(document).ready(function(){
	$("#input-id").rating();
	// with plugin options (do not attach the CSS class "rating" to your input if using this approach)
	$("#input-id").rating({'size':'lg'});
	$('#input-3').rating({displayOnly: true, step: 0.5});
});
</script>