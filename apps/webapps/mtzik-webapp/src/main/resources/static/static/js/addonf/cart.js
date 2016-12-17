

var cartMethode = function(what,music){

    //add to cart

    var url;
    switch (what){
        case "add":
            url='rest/addToCart/'+music;
            break;
        case "remove":
            url='rest/removeFromCart/'+music;
            break;
    }
    $.ajax({
        'type': 'GET',
        'url': addonf.base+url,
        'data': {},
        'success': function(data){
            // refresh cart
            $("#nb-product-in-cart").html(data.nbProduct);
            $("#sum").html(data.minScore);
            $.ajax({
                'type': 'GET',
                'url': addonf.base+'partial/cart.html',
                'data': {},
                'success': function(html){
                    var cart = $(".cart>ul");
                    cart.html(html);
                    $(".cart-total").html(data.error);
                }
            });
            var cart = $("#cartTable");
            if(cart.length==1){
                $.ajax({
                    'type': 'GET',
                    'url': addonf.base+'partial/cartTable.html',
                    'data': {},
                    'success': function(html){
                        cart.html(html);
                    }
                });
            }


        }
    });
};

var addToCartFunction =         function(e){
    cartMethode("add", $(this).data("music"));

    var selector = $(this).data("selector");
    var cart = $('.cart');
    cart.addClass("open");
    var imgtodrag = null;
    if(jQuery.type( selector ) === "undefined"){
        imgtodrag = $(this).parent().parent().find("img").eq(0);
    }else{
        imgtodrag = $(selector);
    }


    if (imgtodrag) {
        var imgclone = imgtodrag.clone()
            .offset({
                top: imgtodrag.offset().top,
                left: imgtodrag.offset().left
            })
            .css({
                'opacity': '0.5',
                'position': 'absolute',
                'height': '150px',
                'width': '150px',
                'z-index': '100'
            })
            .appendTo($('body'))
            .animate({
                'top': cart.offset().top + 10,
                'left': cart.offset().left + 10,
                'width': 75,
                'height': 75
            }, 1000, 'easeInOutExpo',function(){
                cart.effect("shake",200);
            });

        imgclone.animate({
            'width': 0,
            'height': 0
        }, function () {
            $(this).detach()
        });
    }

    return false;
};

$(function(){



    $(".addToCart").unbind("click").click(addToCartFunction);


    $(".removeFromCart").click(function(e){
        cartMethode("remove",$(this).data("music"));
        return false;
    });

    $(".removeFromCartTable").click(function(e){
        cartMethode("remove",$(this).data("music"));
        //cartMethode("removeTable",$(this).data("music"));
        return false;
    });





});
