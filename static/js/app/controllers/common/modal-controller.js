angular.module('senseItWeb', null, null).controller('ModalCtrl', function ($scope, $modalInstance, data, OpenIdService) {

    $scope.data = data;

    $scope.ok = function () {
        if (!data.ok || data.ok()) {
            $modalInstance.close('ok');
        }
    };

    $scope.okLabel = function () {
        return data.okLabel ? data.okLabel() : 'Ok';
    };

    $scope.okDisabled = data.okDisabled;

    $scope.editDisabled = data.editDisabled;

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.close = function (callback) {
        $modalInstance.dismiss('cancel');
        if (callback) {
            callback();
        }
    };

    OpenIdService.registerWatcher($scope);

});