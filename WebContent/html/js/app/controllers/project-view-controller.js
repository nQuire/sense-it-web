


angular.module('senseItWeb', null, null).controller('ProjectViewCtrl', function ($scope, $state, ProjectService, OpenIdService) {
    OpenIdService.registerWatcher($scope);
    ProjectService.registerGet($scope, $state.params['projectId']);


    $scope.joinProject = function() {
        ProjectService.joinProject($state.params['projectId']);
    };

    $scope.leaveProject = function() {
        ProjectService.leaveProject($state.params['projectId']);
    };

});

