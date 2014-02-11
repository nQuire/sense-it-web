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
    	return service._request('api/openid/profile');
    };
    
    service.logout = function() {
    	return service._request('api/openid/logout');
    };
    
    service.saveProfile = function() {
    	return RestService.put('api/openid/profile', {
    		name: service.status.profile.name
    	});
    };
    
    
    
    service.update();

    return service;
}]);

