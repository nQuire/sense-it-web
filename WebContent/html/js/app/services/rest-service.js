'use strict';

angular.module('senseItServices', null, null).factory('RestService', ['$http', function ($http) {

    var service = {
        get: function(path) {
            return $http.get(path);
        },
        post: function(path, data) {
            return $http.post(path, data);
        },
        put: function(path, data) {
            return $http.put(path, data);
        },
        delete: function(path) {
            return $http.delete(path);
        }
    };

    return service;
}]);
