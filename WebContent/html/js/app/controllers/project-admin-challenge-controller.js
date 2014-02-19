angular.module('senseItWeb', null, null).controller('ProjectAdminChallengeCtrl', function ($scope, ProjectChallengeAdminService) {

    $scope.answerData = {
        answersReady: false,
        answers: [],
        editable: false,
        showVoting: true,
        votingEnabled: false,
        showAuthor: true,
        showFilter: false,
        showPublished: false,
        filterByUser: null
    };

    $scope.userAnswerCount = {
        data: null,
        count: function (user) {
            if (!this.data) {
                if ($scope.answerData.answersReady) {
                    this.data = {};
                    for (var i = 0; i < $scope.answerData.answers.length; i++) {
                        var userId = $scope.answerData.answers[i].author.id;
                        if (userId in this.data) {
                            this.data[userId]++;
                        } else {
                            this.data[userId] = 1;
                        }
                    }
                } else {
                    return 0;
                }
            }

            return user.id in this.data ? this.data[user.id] : 0;
        }
    };

    ProjectChallengeAdminService.getVotedAnswers($scope.project.id).then(function (answers) {
        $scope.answerData.answers = answers;
        $scope.answerData.answersReady = true;
    });

    $scope.activityUsersManagement = {
        columns: [
            {
                id: 'answers',
                label: 'Answers',
                sort: function (a, b) {
                    return $scope.userAnswerCount.count(a) - $scope.userAnswerCount.count(b);
                },
                data: function (user) {
                    return $scope.userAnswerCount.count(user).toString();
                }
            }
        ],
        userSelectCallback: function (user) {
            $scope.answerData.filterByUser = user ? user.id : null;
            console.log($scope.answerData.filterByUser);
        }
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

