// Display apps category wise
var $category = $('.category [data-category]');
$category.on('click', function (e) {
    var category = $(this).attr('data-category');
    $.ajax({
        url: "/category/" + category, success: function (result) {
            showContainerApps(result);
        }
    });
});

// show user installed apps
var $collections = $('.collections [data-category]');
$collections.on('click', function (e) {
    $.ajax({
        url: "/ownedapp", success: function (result) {
            showContainerApps(result);
        }
    });
});

//render data in apps section
function showContainerApps(result) {
    var data =
        '<div class="col-sm-12 col-md-8 col-lg-9">' +
        '<div class="main-content">';

    if (result.count == 0)
        data += '<div class="no-results-card">' +
            'You don\'t have any apps yet.' +
            '</div>';

    $.each(result.list, function (i) {
        var appId = result.list[i].appId;
        var icon = result.list[i].customData.icon;
        var appName = result.list[i].name;
        var summary = result.list[i].customData.summary;
        data += '' +
            '<a class="Integration-box" href="/details/' + appId + '">' +
            '<div class="row">';
        if (icon != null) {
            data += '<img class="Integration-Icon" src="' + icon + '" alt="App Image"/>';
        } else {
            data += '<img class="Integration-Icon" src="/assets/images/notavailable.png" alt="App Image"/>';
        }
        data += '<div class="Integration-Detail">' +
            '<h3>' + appName + '</h3>';
        if (summary != null) {
            data += '<p class="IntegrationTagline">' + summary + '</p>';
        }
        data += '</div>' +
            '</div>' +
            '</a>' +
            '';
    });
    data += '</div>' +
        '</div>';

    $('#apps').html(data);
}