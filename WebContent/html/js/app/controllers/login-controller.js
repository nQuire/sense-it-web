angular
    .module('senseItWeb', null, null)
    .controller(
    'LoginCtrl',
    function ($scope, $state, OpenIdService) {
        OpenIdService.registerWatcher($scope);

        $scope.menuItemLabel = function () {
            return $scope.status.ready ? ($scope.status.logged ? ($scope.status.profile.name ? $scope.status.profile.name
                : 'My profile')
                : "Login")
                : "...";
        };

    });
