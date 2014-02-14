angular.module('senseItWeb', null, null).controller('ProjectViewChallengeCtrl', function ($scope, $state, ProjectChallengeService) {

    $scope.setStage = function (stage) {
        if (stage != $scope.project.activity.stage) {
            ProjectChallengeService.setStage($scope.project.id, stage);
        }
    };

    $scope.stageButtonClass = function (stage) {
        return stage == $scope.project.activity.stage ? 'pure-button-active' : 'pure-button-primary';
    };
});

