angular.module('senseItWeb', null, null).controller('ProjectEditCtrl', function ($scope, $state, ProjectService) {

    $scope.deleteProject = function () {
        $scope.projectWatcher.deleteProject();
        ProjectService.deleteProject($scope.project.id).then(function () {
            $state.go('home');
        });
    };

});

