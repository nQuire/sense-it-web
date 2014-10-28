angular.module('senseItWeb', null, null).controller('ProjectViewChallengeAnswersItemCtrl', function ($scope, ModalService) {

    if ($scope.answerData.editable && $scope.challengeParticipant) {
        $scope.continueEditing = false;

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
                    return true;
                });

                return $scope.continueEditing;
            }
        };

        var cancelCallback = function () {
            if ($scope.itemView.isNew) {
                $scope.itemView.close();
            }
        };

        $scope.form = new SiwFormManager($scope.itemView.answer, ['fieldValues', 'published'], editCallback, cancelCallback);

        $scope.save = function(continueEditing) {
            $scope.continueEditing = continueEditing;
            $scope.form.save();
        }
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

    $scope.deleteAnswer = function () {
        ModalService.open({
            body: 'Are you sure you want to delete this idea?',
            title: 'Delete idea',
            ok: function () {
                $scope.challengeParticipant.deleteAnswer($scope.itemView.answer.id).then(function (data) {
                    if (data) {
                        $scope.itemView.updateAnswers(data);
                        $scope.itemView.close();
                    }
                });
                return true;
            }
        });
    };

    $scope.submitAnswer = function () {
        $scope.challengeParticipant.submitAnswer($scope.itemView.answer).then(function (data) {
            if (data) {
                $scope.itemView.updateAnswers(data);
                $scope.itemView.openById($scope.itemView.answer.id);
            }
        });
    };

});

