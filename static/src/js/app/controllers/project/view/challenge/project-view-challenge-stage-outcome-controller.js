angular.module('senseItWeb', null, null).controller('ProjectViewChallengeStageOutcomeCtrl', function ($scope, ProjectChallengeParticipantService) {

    $scope.outcomeData = {
        editable: false,
        selectedAnswer: null,
        updateCallback: function () {
            $scope.answerData.selectedAnswer = $scope.outcomeData.selectedAnswer;
        }
    };

    $scope.answerData = {
        answersReady: false,
        answers: [],
        editable: false,
        showVoting: true,
        votingEnabled: false,
        showAuthor: true,
        showFilter: true,
        showPublished: false
    };

    $scope.challengeParticipant.getVotedAnswers().then(function (answers) {
        $scope.answerData.answers = answers;
        $scope.answerData.answersReady = true;
    });
});

