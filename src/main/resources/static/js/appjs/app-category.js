// Display apps category wise
var $category = $('.category [data-category]');
$category.on('click', function (e) {
    var category = $(this).attr('data-category');
    $('#IntegrationSearch').val("");
    $.ajax({
        url: "/category/" + category, success: function (result) {
            showContainerApps(result);
        }
    });
});

// show user installed apps
var $collections = $('.collections [data-category]');
$collections.on('click', function (e) {
    var collections = $(this).attr('data-category');

    var request_url;
    if(collections == "myApps"){
        request_url = "/ownedapp/" + collections;
    } else if (collections == "popular"){
        request_url = "/ownedapp/" + collections;
    } else if (collections == "featured"){
        request_url = "/ownedapp/" + collections;
    } else if (collections == "allApps"){
        request_url = "/ownedapp/" + collections;
    }

    $.ajax({
        url: request_url, success: function (result) {
            showContainerApps(result);
        }
    });
});

/*
$( '#IntegrationSearch' ).keyup(function() {

    var category = $('ul.category').find('li.active').find('a').data('category');

    $('#searchLoader').css("display", "block");

    console.log('category is: '+category);
    console.log('this value is: '+$(this).val());

    if()
    if($(this).val().length == 0 && typeof category == 'undefined'){
        $.ajax({
            url: "/ownedapp/allApps" , success: function (result) {
                showContainerApps(result);
                $('#searchLoader').css("display", "none");
            }
        });

    } else if (typeof category != 'undefined' && $(this).val().length == 0){
        $.ajax({
            url: "/category/" + category, success: function (result) {
                showContainerApps(result);
            }
        });
    } else {
        $.ajax({
            url: "/searchapp/" + $(this).val() + "/" + category, success: function (result) {
                showContainerApps(result);
                $('#searchLoader').css("display", "none");
            }
        });
    }
});
*/

$( '#IntegrationSearchQuery' ).keyup(function() {

    var category = $('ul.category').find('li.active').find('a').data('category');
    var collection = $('ul.collections').find('li.active').find('a').data('category');

    $('#searchLoader').css("display", "block");

    if(typeof category != 'undefined'){

        if($(this).val().length != 0){
            $.ajax({
                url: "/searchapp/"+ $(this).val() +"/" + category, success: function (result) {
                    showContainerApps(result);
                    $('#searchLoader').css("display", "none");
                }
            });
        } else {
            $.ajax({
                url: "/category/" + category , success: function (result) {
                    showContainerApps(result);
                    $('#searchLoader').css("display", "none");
                }
            });

        }

    } else if (typeof collection != 'undefined') {
        if($(this).val().length != 0){
            $.ajax({
                url: "/ownedapp/"+ collection + "/" + $(this).val(), success: function (result) {
                    showContainerApps(result);
                    $('#searchLoader').css("display", "none");
                }
            });
        } else {
            $.ajax({
                url: "/ownedapp/" + collection , success: function (result) {
                    showContainerApps(result);
                    $('#searchLoader').css("display", "none");
                }
            });

        }
    } else {
        if($(this).val().length != 0){
            $.ajax({
                url: "/searchapp/"+ $(this).val(), success: function (result) {
                    showContainerApps(result);
                    $('#searchLoader').css("display", "none");
                }
            });
        } else {
            $.ajax({
                url: "/ownedapp/allApps" , success: function (result) {
                    showContainerApps(result);
                    $('#searchLoader').css("display", "none");
                }
            });

        }

    }


});

//render data in apps section
function showContainerApps(result) {
    var data =
        '<div class="col-sm-12 col-md-8 col-lg-9">' +
        '<div class="main-content">';

    if (result.count == 0)
        data += '<div class="no-results-card">' +
            'There are no apps available' +
            '</div>';

    $.each(result.list, function (i) {
        var safeName = result.list[i].safeName;
        var icon = result.list[i].customData.icon;
        var appName = result.list[i].name;
        var summary = result.list[i].customData.summary;
        data += '' +
            '<a class="Integration-box" href="/details/' + safeName + '">' +
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