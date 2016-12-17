$(function(){
    $(".genre-select-news").chosen(
        {
            allow_single_deselect: true ,
            no_results_text: "Oops, nothing found!",
            inherit_select_classes: true,
            width:"100%"
        }
    ).change(function(){
            var $loading = $("#loading-news").show();
            var $news = $("#news").hide();
            $.ajax({
                url: addonf.base+"rest/musics/news/"+$(this).val(),
                dataType: "json",
                success: function( data ) {
                    var global="";

                    for (var i = 0; i < data.length; i++){
                        var obj = data[i];
                        var clone = $(".clone-news").clone();

                        var html = clone.html() .split("##ID##").join(obj.id)
                                                .split("ID").join(obj.id)
                                                .split("##JACKET##").join(obj.jacket)
                                                .split("##TITLE##").join(obj.title)
                                                .split("##NB_AD##").join(obj.nbAdsNeeded)
                        global+=html;

                    }

                    var $elt = $news.html(global);

                    $elt.find(".addToCart").bind('click',addToCartFunction);
                    $elt.find(".playMusic").bind('click',playMusic);

                    $loading.hide();
                    $news.show();
                }
            });
        });
});