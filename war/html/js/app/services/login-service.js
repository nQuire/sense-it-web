'use strict';

angular.module('senseItServices', null, null).factory('LoginService', ['RestService', function (RestService) {

    var service = {
        status: {
            ready: false,
            logged: false,
            profile: null,
            url: null
        }
    };

    service.update = function () {
        RestService.get('api/user').then(function (response) {
            service.status = {
                ready: true,
                logged: response.data.logged,
                profile: response.data.profile,
                url: response.data.url,
                newUser: response.data.newUser,
                email: response.data.email
            };
        });
    };

    service.update();

    return service;
}]);

