angular.module('senseItWeb', null, null).controller('ForumCtrl', function ($scope, ForumService) {

    ForumService.get($scope);

    $scope.adminAccess = function() {
        return $scope.status.logged && $scope.status.profile.admin;
    };

});