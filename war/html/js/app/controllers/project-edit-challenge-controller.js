


angular.module('senseItWeb', null, null).controller('ProjectEditChallengeCtrl', function ($scope) {
    $scope.$watch('project', function() {
        $scope.activity = $scope.project.activity;
    });
});

