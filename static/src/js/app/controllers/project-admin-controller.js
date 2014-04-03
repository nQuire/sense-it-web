


angular.module('senseItWeb', null, null).controller('ProjectAdminCtrl', function ($scope, $state, ProjectService) {
    ProjectService.registerGet($scope, $state.params['projectId']);



    $scope.openProject = function() {
        ProjectService.openProject($state.params['projectId']);
    };

    $scope.closeProject = function() {
        ProjectService.closeProject($state.params['projectId']);
    };

});

