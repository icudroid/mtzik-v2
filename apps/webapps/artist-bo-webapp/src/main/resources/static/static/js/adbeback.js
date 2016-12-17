/**
 * All common fonctions
 */
var ADBEBACK = function() {


    var pad = function (number,nbDigit) {
        if(!nbDigit){
            nbDigit = 10;
        }else{
            nbDigit = 10 * nbDigit;
        }

        if (number < nbDigit) {
            return '0' + number;
        }
        return number;
    };


    return {
        jsPackage: function(name) {
            var p = name.split(".");
            var currentPackage = window;
            for (var i = 0; i < p.length; i++) {
                var packageName = p[i];
                if(!currentPackage[packageName]){
                    currentPackage[packageName] = {};
                }
                currentPackage = currentPackage[packageName];
            }
        },


        postJSON : function(url, data,success){
            $.ajax({
                url : url,
                type: 'POST',
                contentType : 'application/json',
                data : JSON.stringify(data),
                dataType: "json",
                success : success
            });
        },


        parseDate : function(dateString){
            var frDateReg = /(\d{2})\/(\d{2})\/(\d{4})/;
            var dateArray = frDateReg.exec(dateString);
            if(dateArray==null) return null;
            return new Date(dateArray[3],dateArray[2]-1,dateArray[1]);
        },



        datetimeToString : function(date){
            return pad(date.getDay())+"/"+pad(date.getMonth()+1)+"/"+date.getFullYear()+ " "+pad(date.getHours())+":"+pad(date.getMinutes())+":"+pad(date.getSeconds());
        },


        dateToString : function(date){
            return pad(date.getDay())+"/"+pad(date.getMonth()+1)+"/"+date.getFullYear();
        },


        amountFormat : function(amount,devise,placement){
            if(!devise){
                devise = "&euro;";
            }

            if(!placement){
                placement= 'after';
            }

            switch (placement){
                case 'before' :
                    return devise+"&nbsp;"+amount.toFixed(2);
                case 'after' :
                    return amount.toFixed(2)+"&nbsp;"+devise;
                default :
                    return amount.toFixed(2)+"&nbsp;"+devise;
            }


        }


    }
}();