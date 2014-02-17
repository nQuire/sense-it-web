angular.module('senseItWeb', null, null).controller('ProjectViewChallengeVoteCtrl', function ($scope, ProjectChallengeVoteService) {

    $scope.answersReady = false;

    ProjectChallengeVoteService.getAnswers($scope.project.id).then(function (answers) {
        console.log(answers);
        siwVotes.countVotable(answers, 'votes', 'voteCount');
        $scope.answers = answers;
        $scope.answersReady = true;
    });

    var titleField = -1;
    for (var i = 0; i < $scope.project.activity.fields.length; i++) {
        if ($scope.project.activity.fields[i].type === 'title') {
            titleField = $scope.project.activity.fields[i].id;
            break;
        }
    }

    $scope.titleField = titleField;

    $scope.answerTeaser = function (answer) {
        return $scope.titleField < 0 ? answer.id : answer.fieldValues[$scope.titleField];
    };

    $scope.filter = {
        search: '',
        type: 'any',
        sort: {
            field: 'author',
            ascending: true
        },
        strCompare: function(a, b) {
            return a < b ? -1 : (a > b ? 1 : 0);
        },
        compare: function (a, b) {
            switch ($scope.filter.sort.field) {
                case 'author':
                    var c = $scope.filter.strCompare(a.author.name, b.author.name);
                    break;
                case 'votes':
                    var c = a.voteCount.balance - a.voteCount.balance;
                    if (c === 0) {
                        c = a.voteCount.positive - a.voteCount.positive;
                    }
                    break;
                case 'title':
                    var c = $scope.filter.strCompare($scope.answerTeaser(a), $scope.answerTeaser(b));
                    break;
            }
            return $scope.filter.sort.ascending ? c : -c;
        },
        filter: function(a) {
            if ($scope.filter.type === 'mine' && a.author.id !== $scope.status.profile.id) {
                return false;
            }

            if ($scope.filter.type === 'others' && a.author.id === $scope.status.profile.id) {
                return false;
            }

            if ($scope.filter.search && $scope.answerTeaser(a).indexOf($scope.filter.search) < 0) {
                return false;
            }

            return true;
        }
    };

    $scope.answerList = function() {
        var l = $scope.answers.filter($scope.filter.filter);
        l.sort($scope.filter.compare);
        return l;
    };

    $scope.headerClass = function (field) {
        return field === $scope.filter.sort.field ? ($scope.filter.sort.ascending ? 'ascending' : 'descending') : '';
    };

    $scope.headerSort = function (field) {
        if (field === $scope.filter.sort.field) {
            $scope.filter.sort.ascending = !$scope.filter.sort.ascending;
        } else {
            $scope.filter.sort.field = field;
            $scope.filter.sort.ascending = true;
        }
    };
});

