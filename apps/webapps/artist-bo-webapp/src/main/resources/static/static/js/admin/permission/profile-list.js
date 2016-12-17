ADBEBACK.jsPackage("adbeback.profile");


adbeback.profile.ListController = function(){
    this._init();
};

adbeback.profile.ListController.prototype = {
    _init : function(){
        var that = this;

        $(".profile-delete-action,.role-delete-action").click(that._confirmAction);

        $(".btn-action").click(this._ModalAction);

        $("#profiles,#roles").DataTable();

    },


    _ModalAction : function(e){
        e.preventDefault();
        $( "#profileModal").find(".modal-content").load( this.href, function() {
            $( "#profileModal").modal({backdrop: 'static'});
        });
    },

    _confirmAction : function(e){
        e.preventDefault();
        var url = this.href;

        bootbox.confirm($(this).data("delete"), function(result) {
            if(result){
                $.ajax({
                    dataType: "json",
                    url: url,
                    success: function(data){
                        window.location.reload();
                    }
                });
            }
        });
    }

};