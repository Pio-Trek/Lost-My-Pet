window.onload = function () {

    if (document.getElementById('dog-type').checked) {
        document.getElementById('dog-breed').style.display = 'block';
        document.getElementById('cat-breed').style.display = 'none';
    }
    if (document.getElementById('cat-type').checked) {
        document.getElementById('cat-breed').style.display = 'block';
        document.getElementById('dog-breed').style.display = 'none';
    }
};

function showDogBreed() {
    document.getElementById('dog-breed').style.display = 'block';
    document.getElementById('cat-breed').style.display = 'none';
    document.getElementById('pet-breed').style.display = 'none';
    document.getElementById('select-cat').selectedIndex = 0;
}

function showCatBreed() {
    document.getElementById('cat-breed').style.display = 'block';
    document.getElementById('dog-breed').style.display = 'none';
    document.getElementById('pet-breed').style.display = 'none';
    document.getElementById('select-dog').selectedIndex = 0;
}

$('.button-breed').click(function () {
    $('.button-breed').removeClass('btn-primary').addClass('btn-default');
    $(this).addClass('btn-primary').removeClass('btn-default');
});
