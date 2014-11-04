
angular.module('senseItWeb', null, null).controller('ProfileLoggedInCtrl', function ($scope, RestService) {
  RestService.get('api/profiles/loggedin').then(function(data) {
    $scope.users = data;
  });

});

