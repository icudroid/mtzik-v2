ADBEBACK.jsPackage("adbeback.role");

adbeback.role.RoleModal = function(options){
    this._init(options);
};

adbeback.role.RoleModal.prototype = {

    options : null,
    datatable : null,

    _init : function(options){

        $('[data-toggle="tooltip"]').tooltip()

        var that = this;
        this.options = options;

        if(options.roleId != null){
            $('#rName').editable({
                type: 'text',
                title: 'Entrer le nouveau nom du role',
                success: function(response, newValue) {
                    $('#name').val(newValue);
                },
                validate: function(value) {
                    if($.trim(value) == '') {
                        return 'Le nom du role est requis.';
                    }else{

                        var result;
                        $.ajax({
                            async : false,
                            url : options.existRoleIdUrl+options.roleId+"/"+value,
                            dataType: "json",
                            success : function(data){
                                if(data.data){
                                    result =  'Le nom du role "'+value+'" existe';
                                }
                            }
                        });

                        return result;

                    }
                }
            });

            $('#rDescription').editable({
                type: 'textarea',
                title: 'Entrer la description du role',
                success: function(response, newValue) {
                    $('#description').val(newValue);
                }
            });
        }


        this.datatable = $('#table-role-permissions').DataTable();
        $('#btn-save-role').click(function(e){
            e.preventDefault();
            that._onSave(that);
        });

    },


    _onSave : function(context){
        var $initialForm = $("#save-role-form");

        var form = $initialForm.find("input, select, textarea").serialize();
        form+="&";
        form+=context.datatable.$('input').serialize();


        $.ajax({
            type : 'POST',
            url : $initialForm.attr("action"),
            data : form,
            success : function(data){
                $( "#profileModal").find(".modal-content").html(data);
            }
        });
    }

};