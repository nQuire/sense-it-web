


angular.module('senseItWeb', null, null).controller('ProjectEditSenseItSensorCtrl', function ($scope, $state, ProjectService) {
    $scope.isNew = typeof $scope.sensorInput === 'undefined';
    if ($scope.isNew) {
        $scope.sensorInput = {};
    }
    console.log($scope.isNew);
    console.log($scope.sensorInput);
    $scope.form = new siwFormManager($scope.sensorInput, ['rate', 'sensor'], function() {
/*        if ($scope.isNew) {
            ProjectService.putProfile($scope.data.project.id, $scope.profile);
        } else {
            ProjectService.postProfile($scope.profile);
        }
 */   });


});

