ADBEBACK.jsPackage("mtzik.user")

mtzik.user.ListController = function(options){
    this._init(options);
};


mtzik.user.ListController.showDetail = function(e,elt){
    e.preventDefault();
    $( "#detailModal").find(".modal-content").load( elt.href, function(responseText, status, jqXHR) {
        if (status == "error") {
            $( "#detailModal").find(".modal-content").html(responseText);
            $( "#detailModal").modal();
        }else{
            $( "#detailModal").modal({backdrop: 'static'});
        }
    });
    return false;
}

mtzik.user.ListController.prototype = {
    _init : function(options){
        var that = this;
        var table = $('#users');
        that.datatable = table.dataTable( {
            fnServerParams : function(data){
                data.additionalForm = {
                    identifiant:$("#identifiant").val(),
                    email:$("#email").val(),
                    firstname:$("#firstname").val(),
                    lastname:$("#lastname").val(),
                    userType:$("#userType").val()
                };
            },

            "language": {
                "url": "//cdn.datatables.net/plug-ins/3cfcc339e89/i18n/French.json"
            },
            "processing": true,
            "serverSide": true,
            "bFilter":false,
            "ajax": function(data, fn, oSettings){
                //remove error
                $(".has-error").each(function(i,elt){
                    $(elt).removeClass("has-error");
                    $(".help-block-error").remove();
                });

                ADBEBACK.postJSON(options.searchUrl, data,function(data, textStatus, jqXHR){
                    if(data.errors && data.errors.length>0){
                        FIANET.displayFieldError(data.errors,options.i18n);
                        data.error = "error"
                        fn(data, textStatus, jqXHR);
                    }else{
                        fn(data, textStatus, jqXHR);
                    }
                });
            },


            "columns": [

                { title:"Identifiant", data: "username"},
                { title:"Email", data: "email"},
                { title:"Prénom", data: "identity",render:function(data){
                    return data.firstName;
                }},
                { title:"Nom", data: "identity",render:function(data){
                    return data.lastName;
                }},
                { title:"Profiles", data: "profiles", orderable:false, render:function(data){
                    var profile = "";
                    for (var i = 0; i < data.length; i++) {
                        if(i>0){
                            profile+=" | ";
                        }
                        var obj = data[i];
                        profile+=obj.name;
                    }
                    return profile;
                }},
                { title:"Type d'utilisateur", data: "identity", render : function(data, type, full, meta){
                    return options.i18n[data.type];
                }},
                { title:"Action", data: "id", visible:true, orderable:false, render:function(data, type, full, meta){
                    var href = options.detailUrl+data;
                    var actions = "";
                    if(options.security.authorize.detail){
                        actions+='<a href="'+href+'" onclick="javascript:mtzik.user.ListController.showDetail(event,this)">détail</a>';
                    }
                    return actions;
                }}
            ]
        } );



        $("#advanced-search").submit(function(e){
            e.preventDefault();
            that.datatable.fnDraw();
        });

    }
};