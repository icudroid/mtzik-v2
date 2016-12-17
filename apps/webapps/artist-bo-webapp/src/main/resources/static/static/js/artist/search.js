ADBEBACK.jsPackage("mtzik.artist")

mtzik.artist.search = function(options){
  this._init(options);
};


mtzik.artist.search.prototype = {
    _init : function(options){

        $('#artists').dataTable( {
            "language": {
                "url": "//cdn.datatables.net/plug-ins/3cfcc339e89/i18n/French.json"
            },
            "processing": true,
            "serverSide": true,
            "ajax": function(data, fn, oSettings){
                ADBEBACK.postJSON(options.searchUrl, data,fn);
            },
            "columns": [
                { title:"id", data: "id", visible:false,orderable:false},
                { title:"Nom de l'artist", data: "artistName"},
                { title:"biography", data: "biography", render : function(data, type, full, meta){
                    return data;
                },orderable:false},
                { title:"Photo", data: "photo", render : function(data, type, full, meta){
                    return "<img src='"+options.photoBaseUrl+data+"' title='photo'/>";
                },orderable:false}
            ]
        } );
    }
};