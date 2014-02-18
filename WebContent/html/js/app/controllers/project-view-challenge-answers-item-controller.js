angular.module('senseItWeb', null, null).controller('ProjectViewChallengeAnswersItemCtrl', function ($scope, ProjectChallengeAnswerService) {
    if ($scope.itemView.editable) {
        $scope.isNew = !Boolean($scope.itemView.answer);
        if ($scope.isNew) {
            $scope.itemView.answer = {
                fieldValues: {},
                published: false
            };
        }

        var editCallback = function() {
            if ($scope.isNew) {
                ProjectChallengeAnswerService.newAnswer($scope.project.id, $scope.itemView.answer).then(function (data) {
                    $scope.itemView.updateAnswers(data.answers);
                    $scope.view.openById(data.newAnswer);
                });
            } else {
                ProjectChallengeAnswerService.updateAnswer($scope.project.id, $scope.itemView.answer).then(function (answers) {
                    $scope.itemView.updateAnswers(answers);
                });
            }
        };

        var cancelCallback = function() {
            if ($scope.isNew) {
                $scope.view.close();
            }
        }
        $scope.form = new siwFormManager($scope.itemView.answer, ['fieldValues', 'published'], editCallback, cancelCallback);
    }

    if ($scope.isNew) {
        $scope.form.open();
    }

    $scope.cancelForm = function () {
        $scope.form.cancel();
        if ($scope.itemView.isNew) {
            $scope.view.close();
        }
    };

});

