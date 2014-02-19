angular.module('senseItWeb', null, null).controller('ProjectViewChallengeAnswersItemCtrl', function ($scope, ProjectChallengeParticipantService) {
    if ($scope.answerData.editable) {

        var editCallback = function () {
            if ($scope.itemView.isNew) {
                ProjectChallengeParticipantService.newAnswer($scope.project.id, $scope.itemView.answer).then(function (data) {
                    if (data.newAnswer >= 0) {
                        $scope.itemView.updateAnswers(data.answers);
                        $scope.itemView.openById(data.newAnswer);
                    } else {
                        $scope.itemView.close();
                    }
                });
            } else {
                ProjectChallengeParticipantService.updateAnswer($scope.project.id, $scope.itemView.answer).then(function (answers) {
                    $scope.itemView.updateAnswers(answers);
                });
            }
        };

        var cancelCallback = function () {
            if ($scope.itemView.isNew) {
                $scope.itemView.close();
            }
        }
        $scope.form = new siwFormManager($scope.itemView.answer, ['fieldValues', 'published'], editCallback, cancelCallback);
    }

    if ($scope.itemView.isNew) {
        $scope.form.open();
    }

    $scope.cancelForm = function () {
        $scope.form.cancel();
        if ($scope.itemView.isNew) {
            $scope.itemView.close();
        }
    };

});

