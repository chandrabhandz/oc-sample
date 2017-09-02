/**
 * Created on 31/8/17 2:46 PM by Raja Dushyant Vashishtha
 * Sr. Software Engineer
 * rajad@decipherzone.com
 * Decipher Zone Softwares
 * www.decipherzone.com
 */


var bSubmit = false;

// Disable dropzone autodiscover to initialize the dropzone with custom configuration
Dropzone.autoDiscover = false;

// Initialize tinymce editor
tinymce.init(
    {
        selector: '#description',
        plugins: "lists link",
        menubar: false,
        toolbar: "undo redo | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link",
        setup: function (editor) {
            editor.on('mousedown', function (e) {
                if ($("textarea#description").parents(".form-group").hasClass("error")) {
                    $("textarea#description").parents(".form-group").removeClass("error");
                    $("textarea#description").parents(".form-group").find(".help-block").text("");
                }
            });
        }
    }
);

// Create or update app
function submitApp(obj, publish) {
    bSubmit = true;
    $('#publish').val(publish);
    $(obj).find('.fa-spinner').removeClass('hidden');
    $(obj).prop('disabled', true);
    $('form').submit();
}

$(function() {

    // Initialize dropzones
    initializeCropperDropzone("#upload-icon", {
        fileType: "icon",
        bCropper: true,
        maxFiles: 1,
        autoProcessQueue: false,
        minWidth: 100,
        minHeight: 100
    });

    initializeCropperDropzone("#upload-images", {
        fileType: "images",
        maxFiles: 10,
        autoProcessQueue: true
    });

    initializeCropperDropzone("#upload-files", {
        fileType: "files",
        bFile: true,
        maxFiles: 10,
        autoProcessQueue: true
    });

    // Initialize category select dropdown
    $(".categorizer").select2();

    // Set video preview function
    $(".video-url").change(function () {
        $(".video-preview iframe").remove();
        $(".video-preview").text('');
        $(".video-preview").append(getEmbedVideoCode($(".video-url").val(), 390, 220));
    });

    // Display video when the url is preset
    if ($(".video-url").val()) {
        $(".video-preview").append(getEmbedVideoCode($(".video-url").val(), 390, 220));
    }

    // Initialize bootstrap tooltip
    $("[data-toggle='tooltip']").tooltip();

    // Initialize form validation
    $("form").find("input,select,textarea").not("[type='submit']").jqBootstrapValidation({
        preventSubmit: true,
        submitSuccess: function () {

            if (typeof app != 'undefined') {
                var status = JSON.parse(app).status.value;

                // If app status is pending, submit the form without publish modal dialog.
                if (status == 'pending') {
                    bSubmit = true;
                }
            }

            console.log(bSubmit);
            // When save button is clicked
            if (bSubmit == false) {
                var modal =
                    '<div id="modal_publish" class="modal fade" role="dialog">' +
                    '	<div class="modal-dialog">' +
                    '		<div class="modal-content">' +
                    '			<div class="modal-header">' +
                    '				<button class="close" data-dismiss="modal"> &times; </button>' +
                    '				<h4 class="modal-title"> Publish App </h4>' +
                    '			</div>' +
                    '			<div class="modal-body">' +
                    '				<p> Do you also want to submit this app to the marketplace right now? </p>' +
                    '			</div>' +
                    '			<div class="modal-footer">' +
                    '				<button class="btn btn-default" onclick="submitApp(this, \'false\');"> <i class="fa fa-spinner hidden"> </i> Later </button>' +
                    '				<button class="btn btn-primary" onclick="submitApp(this, \'true\');"> <i class="fa fa-spinner hidden"> </i> Yes </button>' +
                    '			</div>' +
                    '		</div>' +
                    '	</div>' +
                    '</div>';

                $(modal).modal();
            }
        },
        submitError: function ($form, event, errors) {
            console.log($form);
            console.log(event);
            console.log(errors);
        }
    });

    $("form").submit(function (e) {
        return bSubmit;
    });
});