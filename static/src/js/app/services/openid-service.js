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

    service._openIdRequest = function (path, logged, notify, method) {

        return RestService[method ? method : 'get'](path).then(function (data) {
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
        return service._openIdRequest('api/security/status', true, true);
    };

    service.logout = function () {
        return service._openIdRequest('api/security/logout', false, true, 'post');
    };

    service.saveProfile = function () {
        return RestService.put('api/security/profile', {
            username: service.status.profile.username
        }).then(function (data) {
                if (data && data.profile) {
                    service.status = {
                        logged: true,
                        profile: data.profile,
                        ready: true
                    };
                }
                return data;
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
        return service._openIdRequest('api/security/profile', true, false);
    });

    return service;
}]);

