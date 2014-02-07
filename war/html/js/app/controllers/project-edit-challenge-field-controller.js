angular.module('senseItWeb', null, null).controller('ProjectEditChallengeFieldCtrl', function ($scope, ProjectChallengeService) {
    $scope.isNew = typeof $scope.field === 'undefined';
    if ($scope.isNew) {
        $scope.field = {};
    }

    $scope.form = new siwFormManager($scope.field, ['label', 'type'], function () {
        var method = $scope.isNew ? 'createField' : 'updateField';
        ProjectChallengeService[method]($scope.project.id, $scope.field);
    });

    $scope.deleteProfile = function() {
        ProjectChallengeService.deleteField($scope.project.id, $scope.field.id);
    };

});

