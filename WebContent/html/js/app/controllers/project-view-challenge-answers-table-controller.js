angular.module('senseItWeb', null, null).controller('ProjectViewChallengeAnswersTableCtrl', function ($scope) {

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

    var listener = $scope.$watch('answerData.filterByUser', function() {
        console.log('change');
    }, true);
    $scope.$on('destroy', function() {
        listener();
    });

    $scope.filter = {
        search: '',
        type: 'any',
        sort: {
            field: 'author',
            ascending: true
        },
        filter: function (a) {
            if ($scope.filter.type === 'mine' && a.author.id !== $scope.status.profile.id) {
                return false;
            }

            if ($scope.filter.type === 'others' && a.author.id === $scope.status.profile.id) {
                return false;
            }

            if ($scope.filter.search && $scope.answerTeaser(a).indexOf($scope.filter.search) < 0) {
                return false;
            }

            if ($scope.answerData.filterByUser && a.author.id !== $scope.answerData.filterByUser) {
                return false;
            }

            return true;
        },
        filteredAnswers: function() {
            return $scope.answerData.answers.filter($scope.filter.filter);
        }
    };

    if ($scope.answerData.editable) {
        $scope.maxAnswersReached = function() {
            return $scope.project.activity.maxAnswers <= $scope.answerData.answers.length;
        };
    }

    var sort = {};
    if ($scope.answerData.showAuthor) {
        sort['author'] = function (a, b) {
            return siwCompare.string(a.author.name, b.author.name);
        };
    }
    if ($scope.answerData.showVoting) {
        sort['votes'] = function (a, b) {
            return siwCompare.voteCount(a.voteCount, b.voteCount);
        };
    }
    sort['answer'] = function (a, b) {
            return siwCompare.string($scope.answerTeaser(a), $scope.answerTeaser(b));
        };
    $scope.tableData = {
        sort: sort,
        items: $scope.filter.filteredAnswers
    };
});

