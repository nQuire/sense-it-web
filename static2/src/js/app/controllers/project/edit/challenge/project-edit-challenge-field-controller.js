angular.module('senseItWeb', null, null).controller('ProjectEditChallengeFieldCtrl', function ($scope) {
    $scope.isNew = typeof $scope.field === 'undefined';
    if ($scope.isNew) {
        $scope.field = {};
    }

    $scope.form = new SiwFormManager($scope.field, ['label', 'type'], function () {
        var method = $scope.isNew ? 'createField' : 'updateField';
        $scope.challengeEditor[method]($scope.field);
    });

    $scope.deleteProfile = function() {
        $scope.challengeEditor.deleteField($scope.field.id);
    };

});

