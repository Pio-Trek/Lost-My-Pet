$(document).ready(function () {
    $('#image-file').bind('change', function () {
        var fileSize = this.files[0].size;
        if (fileSize > 5000000) {
            var control = $("#image-file");
            control.replaceWith(control = control.clone(true));
            alert('The image file that you are trying to use is too large.');
            $('#upload-file-info').html('')
        } else {
            $('#upload-file-info').html(this.files[0].name)
        }
    });

});


