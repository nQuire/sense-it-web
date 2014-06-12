angular.module('senseItWeb', null, null).controller('ProjectViewChallengeAnswersItemCtrl', function ($scope) {

    if ($scope.answerData.editable && $scope.challengeParticipant) {
        var editCallback = function () {
            if ($scope.itemView.isNew) {
                $scope.challengeParticipant.newAnswer($scope.itemView.answer).then(function (data) {
                    if (data.newAnswer >= 0) {
                        $scope.itemView.updateAnswers(data.answers);
                        $scope.itemView.openById(data.newAnswer);
                    } else {
                        $scope.itemView.close();
                    }
                });
            } else {
                $scope.challengeParticipant.updateAnswer($scope.itemView.answer).then(function (answers) {
                    $scope.itemView.updateAnswers(answers);
                });
            }
        };

        var cancelCallback = function () {
            if ($scope.itemView.isNew) {
                $scope.itemView.close();
            }
        }
        $scope.form = new SiwFormManager($scope.itemView.answer, ['fieldValues', 'published'], editCallback, cancelCallback);
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

