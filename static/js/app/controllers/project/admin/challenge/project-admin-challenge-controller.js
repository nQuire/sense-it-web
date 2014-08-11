angular.module('senseItWeb', null, null).controller('ProjectAdminChallengeCtrl', function ($scope, ProjectChallengeAdminService) {

    $scope.templates.menu = 'partials/project/admin/challenge/challenge-admin-menu.html';

    $scope.challengeAdmin = ProjectChallengeAdminService.challengeAdmin($scope.projectWatcher);

    $scope.outcomeData = {
        editable: true,
        selectedAnswer: null,
        selectCallback: function (answer) {
        },
        updateCallback: function () {
            $scope.answerData.selectedAnswer = $scope.outcomeData.selectedAnswer;
        }
    };

    $scope.answerData = {
        answersReady: false,
        answers: [],
        editable: false,
        showVoting: true,
        votingEnabled: function () {
            return false;
        },
        showAuthor: true,
        showFilter: false,
        showPublished: false,
        filterByUser: null,
        selectable: true,
        selectCallback: function (answer) {
            $scope.outcomeData.selectCallback(answer);
        },
        selectedAnswer: null
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
        }
    };

    $scope.setStage = function (stage) {
        if (stage != $scope.projectData.project.activity.stage) {
            $scope.challengeAdmin.setStage(stage);
        }
    };

    $scope.stageButtonClass = function (stage) {
        return stage == $scope.projectData.project.activity.stage ? 'btn-primary active' : 'btn-default';
    };


    $scope.challengeAdmin.getVotedAnswers().then(function (answers) {
        $scope.answerData.answers = answers;
        $scope.answerData.answersReady = true;
    });
});

