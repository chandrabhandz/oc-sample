var $category = $('.category [data-category]');
$category.on('click', function(e) {
    var category = $(this).attr('data-category');
    $.ajax({url: "/category/"+category, success: function(result){

        var data =
            '<div class="col-sm-12 col-md-8 col-lg-9">' +
                '<div class="main-content">' +
                    '<div class="row">';

        if (result.count == 0)
            data += '<div class="no-results-card">'+
                        'You don\'t have any apps yet.'+
                    '</div>';
        else
            data += '<div>';
                $.each(result.list, function (i) {
                    var appId = result.list[i].appId;
                    var icon = result.list[i].customData.icon;
                    var appName = result.list[i].name;
                    var summary = result.list[i].customData.summary;
                    data += '<div class="col-md-6">'+
                            '<a class="Integration-box" href="/details/'+appId+'">' +
                                '<div class="row">';
                                    if(icon != null) {
                                        data += '<img class="Integration-Icon" src="'+icon+'" alt="App Image"/>';
                                    } else {
                                        data += '<img class="Integration-Icon" src="/assets/images/notavailable.png" alt="App Image"/>';
                                    }
                                    data += '<div class="Integration-Detail">' +
                                        '<h3>'+appName+'</h3>';
                                    if(summary != null) {
                                        data += '<p class="IntegrationTagline">'+summary+'</p>';
                                    }
                                    data += '</div>' +
                                '</div>' +
                            '</a>' +
                        '</div>';
                });
            data += '</div>' +
                '</div>' +
            '</div>' +
            '</div>';

        $('#apps').html(data);
    }});
 });