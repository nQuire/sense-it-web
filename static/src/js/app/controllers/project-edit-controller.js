angular.module('senseItWeb', null, null).controller('ProjectEditCtrl', function ($scope, $state, ProjectService) {
    ProjectService.registerGet($scope, $state.params['projectId']);

    $scope.deleteProject = function () {
        ProjectService.deleteProject($scope.project.id).then(function (deleted) {
            if (deleted) {
                $state.go('home');
            }
        });
    };

});

