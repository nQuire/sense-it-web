angular.module('senseItWeb', null, null).controller('ProjectEditChallengeFieldCtrl', function ($scope, ProjectChallengeEditorService) {
    $scope.isNew = typeof $scope.field === 'undefined';
    if ($scope.isNew) {
        $scope.field = {};
    }

    $scope.form = new siwFormManager($scope.field, ['label', 'type'], function () {
        var method = $scope.isNew ? 'createField' : 'updateField';
        ProjectChallengeEditorService[method]($scope.project.id, $scope.field);
    });

    $scope.deleteProfile = function() {
        ProjectChallengeEditorService.deleteField($scope.project.id, $scope.field.id);
    };

});

