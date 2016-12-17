
$(function(){

    var login = function(callback){
        $.ajax({url: addonf.base+"login",
            type: "POST",
            dataType : "json",
            data: $("#login-4-game").serialize(),
            success: callback,
            error: function(){
                $('#login-error').show();
            }
        });
    }

    var submitPaymentForm = function(data){
        var url = addonf.config.paymentUrl;
        var $form = $("<form>").css("display","none").attr("action",url).attr("method","post");

        for (var prop in data) {
            if(typeof data[prop] === 'object'){
                if($.isArray(data[prop])){
                    $.each(data[prop], function(key, value) {
                        for (var d in value) {
                            $form.append("<input type='text' name='"+prop+"["+key+"]."+d+"' value='"+value[d]+"'/>");
                        }
                    });

                }else{
                    for (var o in data[prop]) {
                        if(typeof data[prop][o] === 'object'){
                            for (var d1 in data[prop][o]) {
                                $form.append("<input type='text' name='"+prop+"."+o+"."+d1+"' value='"+data[prop][o][d1]+"'/>");
                            }
                        }else{
                            $form.append("<input type='text' name='"+prop+"."+o+"' value='"+data[prop][o]+"'/>");
                        }

                    }
                }
            }else{
                $form.append("<input type='text' name='"+prop+"' value='"+data[prop]+"'/>");
            }

        }

        $('body').append($form);
        $form.submit();
    }

    $(addonf.config.idBtn).click(function(){
        var logBefore = $(this).data("logBefore");

        if(logBefore==true){
            login(function(){
                $.ajax({
                    'type': 'GET',
                    'url': addonf.base+'rest/createTr',
                    'data': {},
                    'success': submitPaymentForm
                });
            });
        }else{
            $.ajax({
                'type': 'GET',
                'url': addonf.base+'rest/createTr',
                'data': {},
                'success':  submitPaymentForm
            });
        }


    });
});
