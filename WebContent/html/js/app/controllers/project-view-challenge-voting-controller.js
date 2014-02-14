


angular.module('senseItWeb', null, null).controller('ProjectViewChallengeAnswerCtrl', function ($scope, $state, ProjectChallengeAnswerService) {
    if ($state.params['answerId']) {
        for (var i = 0; i < $scope.answers.length; i++) {
            if ($scope.answers[i].id == $state.params['answerId']) {
                $scope.answer = $scope.answers[i];
                break;
            }
        }
        $scope.isNew = false;
    } else {
        $scope.answer = {fieldValues: {}};
        $scope.isNew = true;
    }



    $scope.form = new siwFormManager($scope.answer, ['fieldValues', 'published'], function() {
        if ($scope.isNew) {
            ProjectChallengeAnswerService.newAnswer($scope.project.id, $scope.answer).then(function(data) {
                $scope.updateAnswers(data.answers);
                $state.go('project-view.challenge-answer', {projectId: $scope.project.id, answerId: data.newAnswer});
            });
        } else {
            ProjectChallengeAnswerService.updateAnswer($scope.project.id, $scope.answer).then(function(answers) {
                $scope.updateAnswers(answers);
            });
        }
    });

    if ($scope.isNew) {
        $scope.form.open();
    }


    $scope.cancelForm = function() {
        if ($scope.isNew) {
            $state.go('project-view.challenge-list', {projectId: $scope.project.id});
        } else {
            $scope.form.cancel();
        }
    };

});

