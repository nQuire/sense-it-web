angular.module('senseItWeb', null, null).controller('NavBarCtrl', function ($scope, $state) {

    $scope.active = function (state) {
        return $state.current.name.indexOf(state) === 0;
    };

    $scope.profileItemLabel = function () {
        if (!$scope.status.ready) {
            return '...';
        } else if (!$scope.status.logged ) {
            return 'Sign in';
        } else {
            return $scope.status.profile.username ? $scope.status.profile.username : 'My profile';
        }
    };

});
