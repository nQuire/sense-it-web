


angular.module('senseItWeb', null, null).controller('ProjectAdminCtrl', function ($scope) {

    $scope.templates.menu = null;

    $scope.openProject = function() {
        $scope.projectWatcher.openProject();
    };

    $scope.closeProject = function() {
        $scope.projectWatcher.closeProject();
    };

});

