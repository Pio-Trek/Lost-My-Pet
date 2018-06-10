function delete_oldImage() {
    document.getElementById('oldImage').value = null;

    document.getElementById('buttonDeleteOldImage').remove();

    let element = document.getElementById('showOldImage');
    element.style.opacity = '0.4';
    element.style.filter = 'alpha(opacity=40)'; // IE fallback
    element.style.filter = 'grayscale(70%)';
}