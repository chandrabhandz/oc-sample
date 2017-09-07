function showPublishDialog(appId, version) {
    var modal =
        '<div id="modal_publish" class="modal fade" role="dialog">' +
        '	<div class="modal-dialog">' +
        '		<div class="modal-content">' +
        '			<div class="modal-header">' +
        '				<button class="close" data-dismiss="modal"> &times; </button>' +
        '				<h4 class="modal-title"> Publish App? </h4>' +
        '			</div>' +
        '			<div class="modal-body">' +
        '				<p> Are you sure that you want to publish this app to the marketplace? </p>' +
        '			</div>' +
        '			<div class="modal-footer">' +
        '				<button class="btn btn-default" data-dismiss="modal"> Cancel </button>' +
        '				<button class="btn btn-success" onclick="publishApp(this, \'' + appId + '\', \'' + version + '\')"> <i class="fa fa-spinner hidden"> </i> Publish </button>' +
        '			</div>' +
        '		</div>' +
        '	</div>' +
        '</div>';

    $(modal).modal();
}

function showSuspendDialog(appId, status) {
    var modal =
        '<div id="modal_suspend" class="modal fade" role="dialog">' +
        '	<div class="modal-dialog">' +
        '		<div class="modal-content">' +
        '			<div class="modal-header">' +
        '				<button class="close" data-dismiss="modal"> &times; </button>';

    if (status == 'suspend')
        modal += '	<h4 class="modal-title"> Suspend App? </h4>';
    else
        modal += '	<h4 class="modal-title"> Unsuspend App? </h4>';

    modal += '	</div>' +
        '	<div class="modal-body">';

    if (status == 'suspend')
        modal += '	<p> Are you sure that you want to suspend this app in the marketplace? </p>' +
            '	<p><strong>Warning:</strong> This app listing will be hidden from users on the marketplace.</p>';
    else
        modal += '	<p> Are you sure that you want to resume offering this app in the marketplace? </p>';

    modal += '	</div>' +
        '			<div class="modal-footer">' +
        '				<button class="btn btn-default" data-dismiss="modal"> Cancel </button>';

    if (status == 'suspend')
        modal += '	<button class="btn btn-warning" onclick="suspendApp(this, \'' + appId + '\', \'' + status + '\')"> <i class="fa fa-spinner hidden"> </i> Suspend </button>';
    else
        modal += '	<button class="btn btn-success" onclick="suspendApp(this, \'' + appId + '\', \'' + status + '\')"> <i class="fa fa-spinner hidden"> </i> Unsuspend </button>';

    modal += '	</div>' +
        '		</div>' +
        '	</div>' +
        '</div>';

    $(modal).modal();
}

function showDeleteDialog(appId, version, status) {
    var modal =
        '<div id="modal_delete" class="modal fade" role="dialog">' +
        '	<div class="modal-dialog">' +
        '		<div class="modal-content">' +
        '			<div class="modal-header">' +
        '				<button class="close" data-dismiss="modal"> &times; </button>';

    if (status == 'approved')
        modal += '	<h4 class="modal-title"> Delete App? </h4>';
    else
        modal += '	<h4 class="modal-title"> Delete Version? </h4>';

    modal += '	</div>' +
        '			<div class="modal-body">';

    if (status == 'approved')
        modal +='	<p> Are you sure that you want to delete this app? </p>' +
            '	<p><strong>Warning:</strong> This will completely delete all versions of this app including drafts and pending versions.</p>';
    else {
        var version_string = '';

        switch (status) {
            case 'inDevelopment':
                version_string = 'Draft';
                break;
            case 'inReview':
                version_string = 'In Review';
                break;
            case 'pending':
                version_string = 'Pending Approval';
                break;
            case 'rejected':
                version_string = 'Rejected';
                break;
        }
        modal +='	<p> Are you sure that you want to delete this ' +  version_string + ' version of app? </p>';
    }

    modal +=  '	</div>' +
        '			<div class="modal-footer">' +
        '				<button class="btn btn-default" data-dismiss="modal"> Cancel </button>';
    if (status == 'approved')
        modal += '		<button class="btn btn-danger" onclick="deleteApp(this, \'' + appId + '\', \'\')"> <i class="fa fa-spinner hidden"> </i> Delete </button>';
    else
        modal += '		<button class="btn btn-danger" onclick="deleteApp(this, \'' + appId + '\', \'' + version + '\')"> <i class="fa fa-spinner hidden"> </i> Delete </button>';

    modal += '		</div>' +
        '		</div>' +
        '	</div>' +
        '</div>';

    $(modal).modal();
}

function publishApp(obj, appId, version) {
    $(obj).prop('disabled', true);
    $(obj).find('.fa-spinner').removeClass('hidden');
    window.location.href = "/app/publish/" + appId + "/" + version;
}

function deleteApp(obj, appId, version) {
    $(obj).prop('disabled', true);
    $(obj).find('.fa-spinner').removeClass('hidden');
    window.location.href = "/app/delete/" + appId + "/" + version;
}

function suspendApp(obj, appId, status) {
    $(obj).prop('disabled', true);
    $(obj).find('.fa-spinner').removeClass('hidden');
    window.location.href = "/app/status/" + appId + "/" + status;
}