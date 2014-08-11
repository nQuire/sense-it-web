'use strict';

angular.module('senseItServices', null, null).factory('OpenIdService', ['RestService', '$state', '$window', function (RestService, $state, $window) {

    var service = {
        status: {
            ready: false
        },
        listeners: []
    };

    service.registerListener = function (listener) {
        service.listeners.push(listener);
    };

    service.removeListener = function (listener) {
        var index = service.listeners.indexOf(listener);
        if (index >= 0) {
            service.listeners.splice(index, 1);
        }
    };


    service._fireLoginEvent = function (logged) {
        for (var i = 0; i < service.listeners.length; i++) {
            service.listeners[i](logged);
        }
    };

    service._openIdRequest = function (path, logged, notify, method, data, files) {
        var _method = method ? method : 'get';
        var promise = (data || files) ? RestService[_method](path, data, files) : RestService[_method](path);

        return promise.then(function (data) {
            service.status = {
                logged: data.logged,
                newUser: data.newUser,
                profile: data.profile,
                connections: data.connections,
                ready: true
            };

            if (data && data.token) {
                RestService.setToken(data.token);
            }

            if (notify) {
                service._fireLoginEvent(logged);
            }

            if (service.destination && service.status.logged) {
                if (!service.status.newUser) {
                    $state.go(service.destination.name, service.destination.params);
                }
                service.destination = null;
            }

            return data;
        });
    };


    service.update = function () {
        return service._openIdRequest('api/security/status', true, true);
    };

    service.login = function (username, password, callback) {
        return RestService.post('api/security/login', {
            username: username,
            password: password
        }).then(function (data) {
            service.update();
            callback(data);
        });
    };

    service.register = function (username, password, email, callback) {
        return service._openIdRequest('api/security/register', true, true, 'post', {
            username: username,
            password: password,
            email: email
        }).then(function (data) {
            service.update();
            return data;
        });
    };

    service.logout = function () {
        return service._openIdRequest('api/security/logout', false, true, 'post');
    };

    service.deleteConnection = function (providerId) {
        return service._openIdRequest('api/security/connection/' + providerId, true, true, 'delete');
    };

    service.saveProfile = function () {
        return service._openIdRequest('api/security/profile', true, false, 'put', {
            username: service.status.profile.username,
            metadata: service.status.profile.metadata
        });
    };

    service.saveProfileImage = function (files) {
        return service._openIdRequest('api/security/profile/image', true, false, 'post', {}, files);
    };


    service.setPassword = function (oldPassword, newPassword) {
        return service._openIdRequest('api/security/password', true, false, 'put', {
            oldPassword: oldPassword,
            newPassword: newPassword
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
        return service._openIdRequest('api/security/status', true, false);
    });

    service.loginAndComeBack = function () {
        if (!service.status.logged) {
            service.destination = {
                name: $state.current.name,
                params: $state.params
            };
            $state.go('profile');
        }
    };

    service.providerLogin = function (provider) {
        window.handleOpenIdResponse = function () {
            service.update();
        };

        service.loginWindow = $window.open('social/' + provider + '/login');
    };


    return service;
}]);

