angular.module('senseItWeb', null, null).controller('ProjectAdminChallengeCtrl', function ($scope, ProjectChallengeAdminService) {

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

    ProjectChallengeAdminService.getVotedAnswers($scope.project.id).then(function(answers) {
        $scope.answerData.answers = answers;
        $scope.answerData.answersReady = true;
    });

    $scope.activityUsersManagement = {
        columns: [{id: 'answers', label: 'Answers'}]
    };

    $scope.setStage = function (stage) {
        if (stage != $scope.project.activity.stage) {
            ProjectChallengeAdminService.setStage($scope.project.id, stage);
        }
    };

    $scope.stageButtonClass = function (stage) {
        return stage == $scope.project.activity.stage ? 'pure-button-active' : 'pure-button-primary';
    };
});

