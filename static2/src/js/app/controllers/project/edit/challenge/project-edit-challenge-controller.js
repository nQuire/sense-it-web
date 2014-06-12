


angular.module('senseItWeb', null, null).controller('ProjectEditChallengeCtrl', function ($scope, ProjectChallengeEditorService) {

    $scope.challengeEditor = ProjectChallengeEditorService.challengeEditor($scope.projectWatcher);

    $scope.form = new SiwFormManager(function() {
        return $scope.projectData.project.activity;
    }, ['maxAnswers'], function() {
        $scope.challengeEditor.updateActivity($scope.projectData.project.activity);
    });

});

