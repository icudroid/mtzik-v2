'use strict';

angular.module('registrationApp', ['ui.bootstrap'], function(){

});

var registrationCtrl = function ($scope, $timeout,$http) {


    $scope.format="dd/MM/yyyy";

    $scope.today = function() {
        $scope.dt = new Date();
    };
    $scope.today();

    $scope.city = "";

    $scope.showWeeks = true;
    $scope.toggleWeeks = function () {
        $scope.showWeeks = ! $scope.showWeeks;
    };

    $scope.clear = function () {
        $scope.dt = null;
    };

    // Disable weekend selection
    $scope.disabled = function(date, mode) {
        return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
    };

    $scope.toggleMin = function() {
        $scope.minDate = ( $scope.minDate ) ? null : new Date();
    };
    $scope.toggleMin();

    $scope.getCitiesByName = function(val){
        return $http.get(addonf.base+'getTownsByName/'+val ,{
            params: {

            }
        }).then(function(data){
                var res = [];
                angular.forEach(data.data, function(item){
                    item.label =item.zipcode+" "+item.city+", "+item.country.code;
                    res.push(item);
                });
                return res;
            });
    };

    $scope.open = function() {
        $timeout(function() {
            $scope.opened = true;
        });
    };

};
