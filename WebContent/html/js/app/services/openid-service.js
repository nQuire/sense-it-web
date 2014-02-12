'use strict';

angular.module('senseItServices', null, null).factory('OpenIdService', ['RestService', function (RestService) {

    var service = {
        status: {
            ready: false
        },
        listeners: []
    };

    service.registerListener = function(listener) {
        service.listeners.push(listener);
    };

    service._fireLoginEvent = function() {
        for (var i = 0; i < service.listeners.length; i++) {
            service.listeners[i]();
        }
    };

    service._request = function (path, logged) {
        return RestService.get(path).then(function (response) {
            service.status = response.data;
            service.status.ready = true;
            service._fireLoginEvent(logged);
        });
    };


    service.update = function () {
        return service._request('api/openid/profile', true);
    };

    service.logout = function () {
        return service._request('api/openid/logout', false);
    };

    service.saveProfile = function () {
        return RestService.put('api/openid/profile', {
            name: service.status.profile.name
        });
    };

    /**
     *
     * @param scope
     * @param [callback=null]
     */
    service.registerWatcher = function(scope, callback) {
        scope.openIdService = this;
        scope.status = scope.openIdService.status;

        var listener = scope.$watch('openIdService.status', function () {
            scope.status = scope.openIdService.status;
            if (callback) {
                callback();
            }
        }, true);

        scope.$on('$destroy', function() {
            listener();
        });
    };


    service.update();

    return service;
}]);

