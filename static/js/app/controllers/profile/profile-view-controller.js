angular.module('senseItWeb', null, null).controller('ProfileViewCtrl', function ($scope, UsersService, $state) {

  UsersService.getProfile($state.params['uid']).then(function (data) {
    $scope.profile = data;
  });

});
