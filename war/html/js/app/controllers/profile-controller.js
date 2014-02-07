angular.module('senseItWeb', null, null).controller('ProfileCtrl', function ($scope, $window, LoginService) {
    $scope.loginService = LoginService;

    $scope.$watch('loginService.status', function () {
        $scope.status = $scope.loginService.status;
        if ($scope.status.profile) {
            $scope.form.setObject($scope.status.profile);
        }
    }, true);

    $scope.form = new siwFormManager(null, ['name'], function () {
        $scope.status.newUser = false;
    });

    $scope.newUser = function() {
        return $scope.status.ready && $scope.status.logged && $scope.status.newUser;
    };

    $scope.logout = function() {
        $window.location.href = $scope.status.url;
    };

});

