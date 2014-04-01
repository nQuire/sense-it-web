angular.module('senseItWeb', null, null).controller('ProjectViewChallengeStageProposalCtrl', function ($scope, ProjectChallengeParticipantService) {

    $scope.answerData = {
        answersReady: false,
        answers: [],
        editable: true,
        showVoting: false,
        showAuthor: false,
        showFilter: false,
        showPublished: true
    };


    ProjectChallengeParticipantService.getAnswers($scope.project.id).then(function (answers) {
        $scope.answerData.answers = answers;
        $scope.answerData.answersReady = true;
    });


});

