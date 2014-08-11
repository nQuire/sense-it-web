angular.module('senseItWeb', null, null).controller('ProjectEditSenseItSensorCtrl', function ($scope, ModalService) {
    $scope.isNew = typeof $scope.sensorInput === 'undefined';
    if ($scope.isNew) {
        $scope.sensorInput = {sensor: "", rate: 10};
    }

    $scope.form = new SiwFormManager($scope.sensorInput, ['rate', 'sensor'], function () {
        var method = $scope.isNew ? 'createInput' : 'updateInput';
        $scope.senseitEditor[method]($scope.sensorInput);
    });

    $scope.deleteInput = function () {

        ModalService.open({
            body: 'Are you sure you want to delete this sensor input?',
            title: 'Delete sensor',
            ok: function () {
                $scope.senseitEditor.deleteInput($scope.sensorInput.id);
                return true;
            }
        });
    };

});

