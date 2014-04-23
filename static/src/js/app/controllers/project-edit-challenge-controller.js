


angular.module('senseItWeb', null, null).controller('ProjectEditChallengeCtrl', function ($scope, ProjectChallengeEditorService) {

    $scope.$on('$destroy', function () {
        listener();
    });

    $scope.form = new SiwFormManager(function() {
        return $scope.projectData.project.activity;
    }, ['maxAnswers'], function() {
        ProjectChallengeEditorService.updateActivity($scope.project.id, $scope.projectData.project.activity);
    });
});

