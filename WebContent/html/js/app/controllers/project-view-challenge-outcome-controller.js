angular.module('senseItWeb', null, null).controller('ProjectViewChallengeOutcomeCtrl', function ($scope, ProjectChallengeAdminService, ProjectChallengeOutcomeService) {

    console.log($scope);

    $scope.voteManager = {
        votingEnabled: false
    };

    if ($scope.outcomeData.editable) {
        $scope.outcomeData.selectCallback = function (answer) {
            ProjectChallengeOutcomeService.selectAnswer($scope.project.id, answer.id).then($scope._setOutcome);
        };
    }


    $scope._setOutcome = function (outcome) {
        $scope.outcome = outcome;
        $scope.outcomeData.selectedAnswer = outcome.selectedAnswer ? outcome.selectedAnswer.id : null;
        $scope.itemView.answer = outcome.selectedAnswer;

        if ($scope.outcomeForm) {
            $scope.outcomeForm.setObject(outcome);
        } else {
            $scope.outcomeForm = new siwFormManager($scope.outcome, ['explanation'], function () {
                ProjectChallengeOutcomeService.setExplanation($scope.project.id, $scope.outcome.explanation).then($scope._setOutcome);
            });
        }
        $scope.outcomeReady = true;
        $scope.outcomeData.updateCallback();
    };

    ProjectChallengeOutcomeService.getOutcome($scope.project.id).then($scope._setOutcome);

    $scope.itemView = {
        answer: null
    };
});

