'use strict';

angular.module('senseItServices', null, null).factory('RestService', ['$http', function ($http) {

    var service = {
        errorListeners: [],
        registerErrorListener: function(listener) {
          this.errorListeners.push(listener);
        },
        _request: function(promise) {
            return promise.then(function(response) {
                if (response.data) {
                    console.log(response.data);
                    return response.data;
                } else {
                    for (var i = 0; i < service.errorListeners.length; i++) {
                        service.errorListeners[i]();
                    }
                }
            });
        },
        get: function(path) {
            return service._request($http.get(path));
        },
        post: function(path, data) {
            return service._request($http.post(path, data));
        },
        put: function(path, data) {
            return service._request($http.put(path, data));
        },
        delete: function(path) {
            return service._request($http.delete(path));
        }
    };

    return service;
}]);
