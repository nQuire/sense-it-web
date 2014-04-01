angular.module('senseItWeb', null, null).controller('ProjectEditSenseItSensorCtrl', function ($scope, ProjectSenseItService) {
    $scope.isNew = typeof $scope.sensorInput === 'undefined';
    if ($scope.isNew) {
        $scope.sensorInput = {sensor: "", rate: 10};
    }

    $scope.form = new SiwFormManager($scope.sensorInput, ['rate', 'sensor'], function () {
        var method = $scope.isNew ? 'createInput' : 'updateInput';
        ProjectSenseItService[method]($scope.project.id, $scope.sensorInput);
    });

    $scope.deleteInput = function() {
        ProjectSenseItService.deleteInput($scope.project.id, $scope.sensorInput.id);
    };

});

