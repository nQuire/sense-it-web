angular.module('senseItWeb', null, null).controller('ProjectViewChallengeStageProposalCtrl', function ($scope) {

    $scope.answerData = {
        answersReady: false,
        answers: [],
        editable: true,
        showVoting: false,
        showAuthor: false,
        showFilter: false,
        showPublished: true
    };


    $scope.challengeParticipant.getAnswers().then(function (answers) {
        $scope.answerData.answers = answers;
        $scope.answerData.answersReady = true;
    });


});

