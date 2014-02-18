angular.module('senseItWeb', null, null).controller('ProjectViewChallengeStageProposalCtrl', function ($scope, ProjectChallengeAnswerService) {

    $scope.tableView = {
        editable: true,
        answersReady: false,
        answers: [],
        showVoting: false,
        showAuthor: false,
        showFilter: false,
        selectLabel: 'Open'
    };

    $scope.itemView = {
        answer: false,
        editable: true,
        showAuthor: false,
        showPublished: true,
        showVoting: false,
        updateAnswers: function (answers) {
            $scope.tableView.answers = answers;
        }
    };


    ProjectChallengeAnswerService.getAnswers($scope.project.id).then(function (answers) {
        $scope.itemView.updateAnswers(answers);
        $scope.tableView.answersReady = true;
    });


});

