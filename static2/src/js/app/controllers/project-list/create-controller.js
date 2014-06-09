angular.module('senseItWeb', null, null).controller('CreateCtrl', function ($scope, ProjectService, $state) {
    $scope.type = false;
    $scope.createDisabled = true;

    $scope.select = function (type) {
        $scope.type = type;
        $scope.createDisabled = false;
    };

    $scope.selected = function (type) {
        return type == $scope.type;
    };

    $scope.create = function () {
        if (!$scope.createDisabled) {
            ProjectService.createProject($scope.type).then(function(projectId) {
                $state.go('project.edit.home', {projectId: projectId});
            });
        }
    };

});

