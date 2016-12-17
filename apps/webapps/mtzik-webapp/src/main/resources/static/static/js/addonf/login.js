$(function(){

    var loginFailed = function(data, status) {
        $('#login-error').show();
    };

    $("input[name='username']").bind("keyup change",function(){
       var cars = $(this).val().length;
        if(cars>4){
            $("#check-id").show();
            $("#check-id > i").removeClass("fa-times").addClass("fa-check");
        }else if(cars ==0){
            $("#check-id").hide();
        }else{
            $("#check-id").show();
            $("#check-id > i").removeClass("fa-check").addClass("fa-times");
        }
    });


    $("input[name='password']").bind("keyup change",function(){
        var cars = $(this).val().length;

        if(cars>5){
            $("#check-pwd").show();
            $("#check-pwd > i").removeClass("fa-times").addClass("fa-check");
        }else if (cars == 0){
            $("#check-pwd").hide();
        }else{
            $("#check-pwd").show();
            $("#check-pwd > i").removeClass("fa-check").addClass("fa-times");
        }
    });


    $("#form-signin").submit(function(e) {
        $("#login-waiting").show();
        $.ajax({url: addonf.base+"login",
            type: "POST",
            dataType : "json",
            data: $(this).serialize(),
            success: function(data, status) {
                $("#login-waiting").hide();
                if (data.success) {
                    window.location.reload();
                } else {
                    loginFailed(data);
                }
            },
            error: loginFailed
        });
        return false;
    });


});

