'use strict';

angular.module('senseItServices', null, null).factory('UsersService', ['RestService', function (RestService) {
  return {
    getProfile: function (uid) {
      return RestService.get('api/profiles/' + uid);
    }
  };
}]);
