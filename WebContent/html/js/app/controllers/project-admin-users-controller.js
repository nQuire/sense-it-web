angular.module('senseItWeb', null, null).controller('ProjectAdminUsersCtrl', function ($scope, ProjectService) {

    $scope.tableData = {
        items: null,
        sort: {
            'username': function(a, b) {return siwCompare.string(a.name, b.name);}
        }
    };

    ProjectService.getUsers($scope.project.id).then(function(users) {
        $scope.tableData.items = users;
        $scope.usersReady = true;
    });

});

