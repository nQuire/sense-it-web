angular.module('senseItWeb', null, null).controller('ProjectViewChallengeOutcomeCtrl', function ($scope, ProjectChallengeAdminService, ProjectChallengeOutcomeService) {

    $scope.challengeOutcome = ProjectChallengeOutcomeService.challengeOutcome($scope.projectWatcher);

    $scope.voteManager = {
        votingEnabled: function() {return false;}
    };

    if ($scope.outcomeData && $scope.outcomeData.editable) {
        $scope.outcomeData.selectCallback = function (answer) {
            $scope.challengeOutcome.selectAnswer(answer.id).then($scope._setOutcome);
        };
    }

    $scope.outcomeForm = new SiwFormManager(function () {
        return $scope.outcome;
    }, ['explanation'], function () {
        $scope.challengeOutcome.setExplanation($scope.outcome.explanation).then($scope._setOutcome);
    });


    $scope._setOutcome = function (outcome) {
        $scope.outcome = outcome;
        $scope.itemView.answer = outcome.selectedAnswer;
        $scope.outcomeReady = true;

        if ($scope.outcomeData) {
            $scope.outcomeData.selectedAnswer = outcome.selectedAnswer ? outcome.selectedAnswer.id : null;
            $scope.outcomeData.updateCallback();
        }
    };

    $scope.challengeOutcome.getOutcome().then($scope._setOutcome);

    $scope.itemView = {
        answer: null
    };
});

