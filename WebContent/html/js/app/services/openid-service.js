'use strict';

angular.module('senseItServices', null, null).factory('OpenIdService', ['RestService', function (RestService) {

    var service = {
        status: {
            ready: false,
        }
    };
    
    service._request = function(path) {
        return RestService.get(path).then(function (response) {
            service.status = response.data;
            service.status.ready = true;
        });
    };
    

    service.update = function() {
    	return service._request('api/openid/status');
    };
    
    service.logout = function() {
    	return service._request('api/openid/logout');
    };
    
    
    service.update();

    return service;
}]);

