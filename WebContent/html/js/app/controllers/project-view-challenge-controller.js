angular.module('senseItWeb', null, null).controller('ProjectViewChallengeCtrl', function ($scope, $state, ProjectChallengeService, ProjectChallengeAnswerService) {
    $scope.updateAnswers = function (answers) {
        $scope.answers = answers;
    };

    ProjectChallengeAnswerService.getAnswers($scope.project.id).then(function (answers) {
        $scope.updateAnswers(answers);
        $scope.answersReady = true;
    });

    $state.go('project-view.challenge-list', {projectId: $scope.project.id});

    $scope.setStage = function (stage) {
        if (stage != $scope.project.activity.stage) {
            ProjectChallengeService.setStage($scope.project.id, stage);
        }
    };

    $scope.stageButtonClass = function (stage) {
        return stage == $scope.project.activity.stage ? 'pure-button-active' : 'pure-button-primary';
    };


});

