ADBEBACK.jsPackage("mtzik.music");

mtzik.music.search = function(options){
  this._init(options);
};

mtzik.music.search.prototype = {
    _init : function(options){

        $('#musics').dataTable( {
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
                { title:"Titre", data: "title"},
                { title:"artists", data: "artists", render : function(data, type, full, meta){
                    var html = "";
                    for (var i = 0; i < data.length; i++) {
                        if(i>0){
                            html+=", ";
                        }
                        var artist = data[i];
                        html+=artist.lastName;
                    }
                    return html;
                },orderable:false},
                { title:"Cat.", data: "categories", render : function(data, type, full, meta){
                    var html = "";
                    for (var i = 0; i < data.length; i++) {
                        if(i>0){
                            html+=", ";
                        }
                        var cat = data[i];
                        html+=cat.genre;
                    }
                    return html;
                },orderable:false},
                { title:"Date de sortie", data: "releaseDate", render:function(data, type, full, meta){
                    var date = new Date(data);
                    var pad = function (number) {
                        if (number < 10) {
                            return '0' + number;
                        }
                        return number;
                    };

                    return pad(date.getDay())+"/"+pad(date.getMonth())+"/"+date.getFullYear();
                }},
                { title:"Nb Téléchargement", data: "downloaded", orderable:false},
                { title:"Jacket", data: "jacket", render : function(data, type, full, meta){
                    return "<img src='http://localhost:8280/artist/"+data+"' title='jacket'/>";
                },orderable:false}
            ]
        } );
    }
};