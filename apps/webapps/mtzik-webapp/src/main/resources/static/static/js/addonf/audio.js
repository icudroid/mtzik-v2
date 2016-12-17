addonf.currentSample = null;

var playMusic = function(e){

    if($(this).find("i").hasClass("fa-pause") && addonf.currentSample!=null){
        $("a.btn > i.fa-pause").removeClass("fa-pause").addClass("fa-play");
        addonf.currentSample.pause();
    }else{
        if(addonf.currentSample!=null){
            addonf.currentSample.removeEventListener('ended',stopMusic);
            $("a.btn > i.fa-pause").removeClass("fa-pause").addClass("fa-play");
            addonf.currentSample.pause();
        }
        var music = $(this).data("music");
        var url;
        $(this).find("i").removeClass("fa-play").addClass("fa-pause");
        url=addonf.base+'sample/'+music;
        addonf.currentSample = new Audio();
        addonf.currentSample.src = url;
        addonf.currentSample.play();
        addonf.currentSample.addEventListener('ended', stopMusic);
    }

    return false;
};

var stopMusic = function(){
    $("a.btn > i.fa-pause").removeClass("fa-pause").addClass("fa-play");
};


$(function(){
    $(".playMusic").click(playMusic);
});




