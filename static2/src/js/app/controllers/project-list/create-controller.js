angular.module('senseItWeb', null, null).controller('CreateCtrl', function ($scope, ProjectService, $state) {
    $scope.type = null;

    $scope.createDisabled = true;

    $scope.select = function (type) {
        $scope.type = type;
        $scope.createDisabled = false;
    };

    $scope.isShown= function (type) {
        return !$scope.type || $scope.type == type;
    };

    $scope.cancel = function() {
        $scope.type = null;
    };

    $scope.create = function () {
        if (!$scope.createDisabled) {
            ProjectService.createProject($scope.type).then(function(projectId) {
                $state.go('project.edit.home', {projectId: projectId});
            });
        }
    };

});

