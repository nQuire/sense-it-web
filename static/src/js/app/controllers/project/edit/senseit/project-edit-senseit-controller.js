angular.module('senseItWeb', null, null).controller('ProjectEditSenseItCtrl', function ($scope, ProjectSenseItEditService) {

    $scope.templates.menu = 'partials/project/edit/senseit/senseit-edit-menu.html';

    $scope.senseitEditor = ProjectSenseItEditService.senseitEditor($scope.projectWatcher);

    $scope.$watch('projectData.project', function () {
        $scope.transformations = new SiwSenseItTransformations($scope.projectData.project.activity.profile.sensorInputs, $scope.projectData.project.activity.profile.tx);
    });
});

