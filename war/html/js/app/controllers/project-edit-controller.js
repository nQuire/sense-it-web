


angular.module('senseItWeb', null, null).controller('ProjectEditCtrl', function ($scope, $state, ProjectService) {

    $scope.$watch('data.project', function() {
        $scope.project = $scope.data.project;
    });

    $scope.deleteProject = function() {
        ProjectService.deleteProject($scope.project.id).then(function(deleted) {
            $state.go('projects');
        });
    };

    $scope.data = ProjectService.get($state.params['projectId']);

});

