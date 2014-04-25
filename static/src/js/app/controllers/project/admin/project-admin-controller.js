


angular.module('senseItWeb', null, null).controller('ProjectAdminCtrl', function ($scope) {

    $scope.openProject = function() {
        $scope.projectWatcher.openProject();
    };

    $scope.closeProject = function() {
        $scope.projectWatcher.closeProject();
    };

});

