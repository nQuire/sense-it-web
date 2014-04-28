angular.module('senseItWeb', null, null).controller('ProjectEditSenseItCtrl', function ($scope, ProjectSenseItEditService) {

    $scope.senseitEditor = ProjectSenseItEditService.senseitEditor($scope.projectWatcher);

    $scope.$watch('projectData.project', function () {
        $scope.transformations = new SiwSenseItTransformations($scope.projectData.project.activity.profile.sensorInputs, $scope.projectData.project.activity.profile.tx);
    });
});

