$(document).ready(function() {
	
	$('.sidebar-toggle button').on('click', function() {
		if( $(this).hasClass('active') ) {
			$('.sidebar').fadeOut(400).css({'left':'-100%'});
			$(this).removeClass('active');
		}
		else {
			$('.sidebar').fadeIn({queue: false, duration: 'slow'});
			$('.sidebar').animate({ left: "-15px" }, 'slow');
		
			$(this).addClass('active');
		}
	});
	
	
	var obj = $("#IntegrationSearch").autocomplete({
		source: function(request, response) {
            $.ajax({
                url: "/searchapp/" + $("#IntegrationSearch").val(),
                type: "GET",
                success: function(data) {
                    response($.map(data.list, function(obj) {
                        return {
                            label: obj.name,
                            value: obj
                        };
                    }));
                }

            });
        },
		select: function( event, ui ) {

			$("#IntegrationSearch").val( ui.item.label );
			var html = '<a class="Integration-box" href="detail-page.html" data-slug="'+ ui.item.label +'">';
				html += '<div class="row">';
				html += '<img class="Integration-Icon" src="'+ ui.item.value.customData.icon +'">';
				html += '<div class="Integration-Detail">';
				html += '<h3>'+ ui.item.label +'</h3>';
				html += '<p class="IntegrationTagline">'+ ui.item.value.customData.summary +'</p></div></div></a>';
				
			$('.main-content').html(html);
			return false;
		}
	}).autocomplete( "instance" );
	
	obj && (obj._renderItem = function( ul, item ) {
      return $( "<li>" )
        .append( "<div>" + item.label + "</div>" )
        .appendTo( ul );
    });
	
	$( "#IntegrationSearch" ).on( "autocompletechange", function( event, ui ) {
		if( $(this).val().length < 1 ) {
            $('.collections [data-category]').trigger('click');
		}
	});
	

	$( "[data-category]").on('click', function(e){
		e.preventDefault();
		var $cat = $(this).attr('data-category');
		
		$('[data-category]').parent().removeClass('active');
		$(this).parent().addClass('active');
		
		if( $cat === '*'){
			$('.main-content').find('[data-context]').fadeIn();
		}
		else {
			$('.main-content').find('[data-context]').hide();
			$('.main-content').find('[data-context="'+$cat+'"]').fadeIn(800);
		}
	});
	
});