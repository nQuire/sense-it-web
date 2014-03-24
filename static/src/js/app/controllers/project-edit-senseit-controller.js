


angular.module('senseItWeb', null, null).controller('ProjectEditSenseItCtrl', function ($scope) {
    $scope.$watch('project', function() {
        $scope.activity = $scope.project.activity;
        $scope.transformations = new SiwSenseItTransformations($scope.activity.profile.sensorInputs, $scope.activity.profile.tx);
    });
});

