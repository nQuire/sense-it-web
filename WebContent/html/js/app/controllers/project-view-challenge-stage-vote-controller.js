angular.module('senseItWeb', null, null).controller('ProjectViewChallengeStageVoteCtrl', function ($scope, ProjectChallengeParticipantService) {


    $scope.answerData = {
        answersReady: false,
        answers: [],
        editable: false,
        showVoting: true,
        votingEnabled: true,
        showAuthor: true,
        showFilter: true,
        showPublished: false
    };

    ProjectChallengeParticipantService.getVotedAnswers($scope.project.id).then(function (answers) {
        $scope.answerData.answers = answers;
        $scope.answerData.answersReady = true;
    });
});

