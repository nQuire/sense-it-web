

angular.module('senseItWeb', null, null).controller('LoginCtrl', function ($scope, $state, LoginService) {
    $scope.loginService = LoginService;

    $scope.$watch('loginService.status', function() {
        $scope.status = $scope.loginService.status;
        if ($scope.status.logged && $scope.status.newUser) {
            $state.go('profile');
        }
    }, true);

    $scope.menuItemNickLabel = function() {
        return $scope.status.profile.name ? $scope.status.profile.name : 'My profile';
    };

});

