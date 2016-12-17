$(function(){

$('#realestate-map-search input[type="text"]').on('focus', function()
{
    $( ".searchInput" ).popover('hide');
    $('#realestate-map-search .search-navbar').addClass('open');
});

$('#close-realestate-map-search')
    .on('click', function()
    {
        $('#realestate-map-search .search-navbar').removeClass('open');
        setTimeout(function(){
            $( ".searchInput" ).popover('show');
        },2000);

    });

});