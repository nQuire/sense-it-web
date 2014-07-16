angular.module('senseItWeb', null, null).controller('ProjectViewChallengeStageVoteCtrl', function ($scope, ProjectChallengeParticipantService) {


    $scope.answerData.editable = false;
    $scope.answerData.showVoting = true;
    $scope.answerData.votingEnabled = function() {
        return $scope.status.logged && $scope.projectData.access.member && $scope.projectData.project.open;
    };
    $scope.answerData.showAuthor = true;
    $scope.answerData.showFilter = true;
    $scope.answerData.showPublished = false;

});

