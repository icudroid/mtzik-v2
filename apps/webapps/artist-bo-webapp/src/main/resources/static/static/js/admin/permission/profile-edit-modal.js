ADBEBACK.jsPackage("adbeback.profile");

adbeback.profile.ProfileModal = function(options){
    this._init(options);
};

adbeback.profile.ProfileModal.prototype = {

    profile : null,
    allRoles : null,
    options : null,

    _init : function(options){
        var that = this;
        this.options = options;
        this.profile = options.profile;

        this.allRoles = {};
        for (var i = 0; i < options.roles.length; i++) {
            var role = options.roles[i];
            this.allRoles[role.id] = role;
        }

        $('#pName').editable({
            type: 'text',
            title: 'Entrer le nouveau nom du profile',
            success: function(response, newValue) {
                that.profile.name = newValue;
            },
            validate: function(value) {
                if($.trim(value) == '') {
                    return 'Le nom du profile est requis.';
                }else{

                    var result;
                        $.ajax({
                            async : false,
                            url : options.existProfileIdUrl+that.profile.id+"/"+value,
                            dataType: "json",
                            success : function(data){
                                if(data.data){
                                    result =  'Le nom du profile "'+value+'" existe';
                                }
                            }
                        });

                    return result;

                }
            }
        });

        $('#pDescription').editable({
            type: 'textarea',
            title: 'Entrer la description du profile',
            success: function(response, newValue) {
                that.profile.description = newValue;
            }
        });


        $(".btn-add-role").click(function(e){
            e.preventDefault();
            $( "#rolesModal").find(".modal-content").load( this.href, function() {
                $( "#rolesModal").modal({backdrop: 'static'});

                new adbeback.profile.AddRoleModal({
                    profile : that.profile,
                    roles : that.allRoles
                },that._addRoles,that);

            });

        });


        $("#btn-save-profile").click(function(){

            ADBEBACK.postJSON(options.saveProfileUrl,that.profile,function(data){
                if(data.resultCode==0){
                    $("#profileModal").modal('hide');
                    window.location.reload()
                }else{
                    $("#profileError").html(data.errorMessage);
                }
            });

        });



        that._updateTableButtons();


    },

    _updateTableButtons : function(){
        var that = this;
        $(".action-show-role").popover(
            {
                content : function(){
                    return  that._onShowPermission(this,that);
                },
                trigger : 'click',
                html : true,
                placement : 'auto',
                template : '<div class="popover" style="max-width:600px;width: 600px;" role="tooltip"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>'
            }

        ).on("shown.bs.popover",function(e){
                $("#"+$(this).attr("aria-describedby")).find("table").DataTable();
                $("#"+$(this).attr("aria-describedby")).find('[data-toggle="tooltip"]').tooltip()
        });

        $(".action-delete-role").click(function(e){
            e.preventDefault();
            that._deleteRole(this);
        });
    },

    _deleteRole : function(elt){
        var that = this;
        var $thatBtn = $(elt);
        var roleId = $thatBtn.data("id");
        bootbox.confirm("Voulez-vous retirer le role "+ roleId+" du profile "+this.profile.name+" ?", function(result) {
            if(result){
                for (var i = 0; i < that.profile.roles.length; i++) {
                    if(that.profile.roles[i].id==roleId){
                        that.profile.roles.splice(i,1);
                        $thatBtn.parent().parent().remove();
                        break;
                    }
                }
            }
        });
    },

    _onShowPermission : function(elt,context){

        var table;
        $.ajax({
            async : false,
            dataType: "json",
            url: this.options.findPermissionByRoleIdUrl+$(elt).data("role"),
            success: function(data){
                table = '<table class="table">';
                table += '<thead>';
                table += '<tr>';
                table += '<th>';
                table += 'Nom de permission';
                table += '</th>';
                table += '<th>';
                table += 'Droits';
                table += '</th>';
                table += '</tr>';
                table += '</thead>';
                table += '<tbody>';


                for (var i = 0; i < data.length; i++) {
                    var permission = data[i];
                    table += '<tr>';
                    table += '<td>'+permission.permission+'</td>';
                    table += '<td>';
                    for (var j = 0; j < permission.rights.length; j++) {
                        var right = permission.rights[j];
                        table += '<label style="padding-left:15px;" data-toggle="tooltip"  title="'+right.description+'"  data-placement="left"><input checked="true" disabled="true" type="checkbox" alt="'+right.description+'" title="'+right.description+ '" />&nbsp;'
                        +((!right.right)?'&nbsp;':right.right)
                        +'</label>';
                    }
                    table += '</td>';

                    table += '</tr>';
                }

                table += '</tbody>';
                table += '</table>';

                return table;
            }
        });

        return table;

    },

    _refreshTableRoles: function () {
        var table = '';

        for (var i = 0; i < this.profile.roles.length; i++) {
            var role = this.profile.roles[i];
            table += '<tr>';
            table += '<td>'+role.name+'</td>';
            table += '<td>'+role.description+'</td>';
            table += '<td>';

            table +='<a class="btn default btn-xs green-stripe action-show-role" href="#" data-role="'+role.id+'">'+
                        '<i class="fa fa-dashboard"></i>Voir'+
                    '</a>';
            table +='<a class="btn default btn-xs red-stripe action-delete-role" href="#" data-id="'+role.id+'">'+
                        '<i class="fa fa-minus"></i>Supprimer'+
                    '</a>';
            table += '</td>';
            table += '</tr>';
        }


        $("#table-profile-roles").find("tbody").html(table);


        this._updateTableButtons();


        //$("#table-profile-roles").DataTable();
    },

    _addRoles : function(roles){
        for (var i = 0; i < roles.length; i++) {
            var roleId = roles[i];
            this.profile.roles.push(this.allRoles[roleId]);
        }

        this._refreshTableRoles();

    }
};