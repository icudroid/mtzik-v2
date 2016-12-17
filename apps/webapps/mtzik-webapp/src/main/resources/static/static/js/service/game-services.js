'use strict';

/* Services */

var adgameServices = angular.module('adgameServices', ['ngResource']);


adgameServices.factory('Game', ['$resource',
    function($resource){
        return $resource('/', {}, {
            createGame: {url:addonf.base+'rest/createGame',method:'GET',responseType:"json",isArray: false},
            doResponse: {url:addonf.base+'rest/play/:index/:responseId',method:'GET',responseType:"json",isArray: false},
            noResponse: {url:addonf.base+'rest/noresponse/:index',method:'GET',responseType:"json",isArray: false},
            resultGame: {url:addonf.base+'rest/getResultAdGame',method:'GET',responseType:"json",isArray: false}
        });
    }]);