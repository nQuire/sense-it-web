


angular.module('senseItWeb', null, null).controller('ProjectEditChallengeCtrl', function ($scope, ProjectChallengeService) {
    var listener = $scope.$watch('project', function() {
        $scope.activity = $scope.project.activity;
        $scope.form.setObject($scope.activity);
    });

    $scope.$on('$destroy', function () {
        listener();
    });

    $scope.form = new siwFormManager($scope.activity, ['maxAnswers'], function() {
        ProjectChallengeService.updateActivity($scope.project.id, $scope.activity);
    });
});

