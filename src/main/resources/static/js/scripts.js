function initialize() {

    if ((typeof toast_type === 'undefined') === true) {
        toast_type = '';
    }

    // Set toast message to be appeared on the top center position
    toastr.options = {
        'positionClass': 'toast-top-center'
    };

    // Display toast message according to various cases
    if (toast_type == 'error') {
        toastr.error(toast_message);
    } else if (toast_type == 'status') {
        toastr.success(toast_message);
    } else if (toast_type == 'create') {
        toastr.success('Your draft has been saved');
    } else if (toast_type == 'update') {
        toastr.success('Your changes have been saved');
    } else if (toast_type == 'delete') {
        toastr.success('App version deleted successfully');
    } else if (toast_type == 'publish') {
        toastr.success('Your app has been submitted and is pending approval');
    }

    // Draw statistics flot graph
    if ((typeof statistics !== 'undefined') === true) {
        var xaxis = [];
        var month = (new Date()).getMonth();

        for (i = 0; i < 13; i++) {
            xaxis.push([i, monthNames[month++ % 12]]);
        }
        flotConfig.xaxis.ticks = xaxis;
        stats = JSON.parse(statistics);
        if(stats != null) {
            for (i = 0; i < stats.length; i++) {
                stats[i][0] = i;
            }
        }

        $("#plot").plot(
            [
                {
                    data: stats
                },
            ], flotConfig
        ).data("plot");
    }
}

$(document).ready(function () {
    initialize();
});