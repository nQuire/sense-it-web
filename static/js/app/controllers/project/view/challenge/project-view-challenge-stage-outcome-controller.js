angular.module('senseItWeb', null, null).controller('ProjectViewChallengeStageOutcomeCtrl', function ($scope, ProjectChallengeParticipantService) {

    $scope.outcomeData = {
        editable: false,
        selectedAnswer: null,
        updateCallback: function () {
            $scope.answerData.selectedAnswer = $scope.outcomeData.selectedAnswer;
        }
    };

    $scope.answerData.editable = false;
    $scope.answerData.showVoting = true;
    $scope.answerData.votingEnabled = function() {return false;};
    $scope.answerData.showAuthor = true;
    $scope.answerData.showFilter = true;
    $scope.answerData.showPublished = false;

});

