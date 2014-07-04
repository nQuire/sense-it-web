angular.module('senseItWeb', null, null).controller('ModalCtrl', function ($scope, $modalInstance, data) {

    $scope.data = data;

    $scope.ok = function () {
        if (data.ok) {
            data.ok();
        }
        $modalInstance.close('ok');
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

});