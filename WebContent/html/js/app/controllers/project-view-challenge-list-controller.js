


angular.module('senseItWeb', null, null).controller('ProjectViewChallengeListCtrl', function ($scope, $state) {

    var titleField = -1;
    for (var i = 0; i < $scope.project.activity.fields.length; i++) {
        if ($scope.project.activity.fields[i].type === 'title') {
            titleField = $scope.project.activity.fields[i].id;
            break;
        }
    }

    $scope.titleField = titleField;

    $scope.answerTeaser = function(answer) {
        return $scope.titleField < 0 ? answer.id : answer.fieldValues[$scope.titleField];
    };

    $scope.maxAnswersReached = function() {
        return $scope.answers.length >= $scope.project.activity.maxAnswers;
    };

});

