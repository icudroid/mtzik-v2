'use strict';

/* App Module */

var adgameApp = angular.module('adgameApp', [
    'ngRoute',
    'adgameControllers',
    'adgameFilters',
    'adgameServices'
]);

adgameApp.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/start', {templateUrl: addonf.base+'partials/game.html',   controller: 'GameCtrl'}).
            when('/end', {templateUrl: addonf.base+'partials/end.html', controller: 'EndCtrl'}).
            when('/resume', {templateUrl: addonf.base+'partials/resume.html', controller: 'ResumeCtrl'}).

            otherwise({redirectTo: '/start'});
    }]);



