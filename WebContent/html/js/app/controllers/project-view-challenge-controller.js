


angular.module('senseItWeb', null, null).controller('ProjectViewChallengeCtrl', function ($scope, $state, ProjectChallengeAnswerService) {
    $scope.updateAnswers = function(answers) {
        $scope.answers = answers;
    };

    ProjectChallengeAnswerService.getAnswers($scope.project.id).then(function(answers) {
        $scope.updateAnswers(answers);
        $scope.answersReady = true;
    });

    $state.go('project-view.challenge-list', {projectId: $scope.project.id});
});

