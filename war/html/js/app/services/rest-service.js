'use strict';

angular.module('senseItServices', null, null).factory('RestService', ['$http', function ($http) {

    var service = {
        get: function(path) {
            return $http.get(path);
        },
        post: function(path, data) {
            return $http.post(path, data);
        }
    };

    return service;
}]);
