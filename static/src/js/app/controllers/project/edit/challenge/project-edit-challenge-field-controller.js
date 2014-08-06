angular.module('senseItWeb', null, null).controller('ProjectEditChallengeFieldCtrl', function ($scope, ModalService) {
    $scope.isNew = typeof $scope.field === 'undefined';
    if ($scope.isNew) {
        $scope.field = {};
    }

    $scope.form = new SiwFormManager($scope.field, ['label', 'type'], function () {
        var method = $scope.isNew ? 'createField' : 'updateField';
        $scope.challengeEditor[method]($scope.field);
    });

    $scope.moveField = function (up) {
        $scope.challengeEditor.moveField($scope.field.id, up);
    };

    $scope.deleteField = function () {
        ModalService.open({
            body: '<p>Are you sure you want to delete this field?</p><p><b>Please note that data from participants\' submissions will be lost!</b></p>',
            title: 'Delete field',
            ok: function () {
                $scope.challengeEditor.deleteField($scope.field.id);
                return true;
            }
        });
    };

});

