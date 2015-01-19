angular.module('senseItWeb', null, null).controller('ProjectViewChallengeStageProposalCtrl', function ($scope) {

    $scope.answerData.editable = true;
    $scope.answerData.showVoting = false;
    $scope.answerData.votingEnabled = function () {
        return $scope.status.logged && $scope.projectData.access.member && $scope.projectData.project.open;
    };
    $scope.answerData.showAuthor = false;
    $scope.answerData.showFilter = $scope.projectData.project.activity.answersAlwaysVisible;
    $scope.answerData.showAuthor = $scope.projectData.project.activity.answersAlwaysVisible;
    $scope.answerData.showPublished = true;

});

