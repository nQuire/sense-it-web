angular.module('senseItWeb', null, null).controller('AdminUsersCtrl', function ($scope) {

    $scope.admin.getUsers();

    $scope.actions = {
        setAdmin: function (userId, isAdmin) {
            $scope.admin.setAdmin(userId, isAdmin);
        }
    };

});