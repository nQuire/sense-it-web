angular.module('senseItWeb', null, null).controller('LoginCtrl', function ($scope) {
    $scope.menuItemLabel = function () {
        return $scope.status.ready ? ($scope.status.logged ? ($scope.status.profile.username ? $scope.status.profile.username
            : 'My profile')
            : "Login")
            : "...";
    };
});
