angular.module('senseItWeb', null, null).controller('ProjectViewChallengeAnswersTableCtrl', function ($scope) {

    if ($scope.projectData.project.activity.fields.length > 0) {
        $scope.titleField = $scope.projectData.project.activity.fields[0].id;
        $scope.titleLabel = $scope.projectData.project.activity.fields[0].label;
    } else {
        $scope.titleField = -1;
        $scope.titleLabel = 'Title';
    }

    $scope.answerTeaser = function (answer) {
        return $scope.titleField < 0 ? answer.id : answer.fieldValues[$scope.titleField];
    };

    $scope.answerTeaserLabel = function (answer) {
        return $scope.titleField < 0 ? answer.id : answer.fieldValues[$scope.titleField];
    };




    $scope.filter = {
        params: {
            search: '',
            type: 'any'
        },
        sort: {
            field: 'author',
            ascending: true
        },
        filter: function (a) {
            if ($scope.filter.params.type === 'mine' && a.author.id !== $scope.status.profile.id) {
                return false;
            }

            if ($scope.filter.params.type === 'others' && a.author.id === $scope.status.profile.id) {
                return false;
            }

            if ($scope.filter.params.search && $scope.answerTeaser(a).indexOf($scope.filter.params.search) < 0) {
                return false;
            }

            if ($scope.answerData.filterByUser && a.author.id !== $scope.answerData.filterByUser) {
                return false;
            }

            return true;
        },
        filterAnswers: function () {
            $scope.tableData.items = $scope.answerData.answers.filter($scope.filter.filter);
        }
    };

    $scope.$on('$destroy', $scope.$watch('filter.params', $scope.filter.filterAnswers, true));
    $scope.$on('$destroy', $scope.$watch('answerData.answers', $scope.filter.filterAnswers));



    if ($scope.answerData.editable) {
        $scope.maxAnswersReached = function () {
            return $scope.projectData.project.activity.maxAnswers <= $scope.answerData.answers.length;
        };
    }

    var sort = {};
    if ($scope.answerData.showAuthor) {
        sort['author'] = function (a, b) {
            return siwCompare.string(a.author.username, b.author.username);
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

