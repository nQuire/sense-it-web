'use strict';

angular.module('senseItServices', null, null).factory('OpenIdService', ['RestService', function (RestService) {

    var service = {
        status: {
            ready: false
        },
        listeners: []
    };

    service.registerListener = function (listener) {
        service.listeners.push(listener);
    };

    service._fireLoginEvent = function (logged) {
        for (var i = 0; i < service.listeners.length; i++) {
            service.listeners[i](logged);
        }
    };

    service._openIdRequest = function (path, logged, notify) {
        return RestService.get(path).then(function (data) {
            service.status = {
                logged: data.logged,
                profile: data.profile,
                ready: true
            };

            if (data && data.token) {
                RestService.setToken(data.token);
            }

            if (notify) {
                service._fireLoginEvent(logged);
            }
        });
    };


    service.update = function () {
        return service._openIdRequest('api/openid/profile', true, true);
    };

    service.logout = function () {
        return service._openIdRequest('api/openid/logout', false, true);
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
    service.registerWatcher = function (scope, callback) {
        scope.openIdService = this;
        scope.status = scope.openIdService.status;

        var listener = scope.$watch('openIdService.status', function () {
            scope.status = scope.openIdService.status;
            if (callback) {
                callback();
            }
        }, true);

        scope.$on('$destroy', function () {
            listener();
        });
    };


    service.update();

    RestService.registerErrorListener(function () {
        return service._openIdRequest('api/openid/profile', true, false);
    });

    return service;
}]);

