$(document).ready(function () {
    var text_max = 1000;
    $('#message_feedback').html(text_max + ' characters remaining');

    $('#body').keyup(function () {
        var text_length = $('#body').val().length;
        var text_remaining = text_max - text_length;

        $('#message_feedback').html(text_remaining + ' characters remaining');
    });
});