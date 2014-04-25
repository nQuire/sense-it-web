angular.module('senseItWeb', null, null).controller('ProjectAdminUsersCtrl', function ($scope, ProjectService) {


    $scope.tableData = {
        items: null,
        selectCallback: function (user) {
            if ($scope.activityUsersManagement && $scope.activityUsersManagement.userSelectCallback) {
                $scope.activityUsersManagement.userSelectCallback(user);
            }
        },
        sort: {
            'username': function (a, b) {
                return siwCompare.string(a.username, b.username);
            }
        }
    };

    if ($scope.activityUsersManagement) {
        for (var i = 0; i < $scope.activityUsersManagement.columns.length; i++) {
            var column = $scope.activityUsersManagement.columns[i];
            if (column.sort) {
                $scope.tableData.sort[column.id] = column.sort;
            }
        }
    }

    $scope.projectWatcher.getUsers().then(function (users) {
        $scope.tableData.items = users;
        $scope.usersReady = true;
    });

});

