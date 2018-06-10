let today = new Date();
$('.date-picker').datepicker({
    startDate: new Date(2018, 1, 1),
    endDate: new Date(today.getFullYear(), today.getMonth(), today.getDate())
})
