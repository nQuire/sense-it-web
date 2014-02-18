angular.module('senseItWeb', null, null).controller('ProjectViewChallengeStageVoteCtrl', function ($scope, ProjectChallengeVoteService) {
    $scope.answerVoteManager = {
        getPath: function(target) {
            return 'api/project/' + $scope.project.id + '/challenge/votes/' + target.id;
        },
        getVoteCount: function(target) {
            return target.voteCount;
        },
        setVoteCount: function(target, voteCount) {
            target.voteCount = voteCount;
        }
    };

    $scope.tableView = {
        answersReady: false,
        answers: [],
        showVoting: true,
        voteManager: $scope.answerVoteManager,
        showAuthor: true,
        showFilter: true,
        selectLabel: 'View'
    };

    $scope.itemView = {
        answer: false,
        itemEditable: false,
        showAuthor: true,
        showPublished: false,
        showVoting: true,
        voteManager: $scope.answerVoteManager
    };

    ProjectChallengeVoteService.getAnswers($scope.project.id).then(function (answers) {
        $scope.tableView.answers = answers;
        $scope.tableView.answersReady = true;
    });


});

