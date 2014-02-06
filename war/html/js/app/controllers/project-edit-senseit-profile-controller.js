angular.module('senseItWeb', null, null).controller('ProjectEditSenseItProfileCtrl', function ($scope, $state, ProjectService) {
    $scope.isNew = typeof $scope.profile === 'undefined';
    if ($scope.isNew) {
        $scope.profile = {};
    }

    $scope.form = new siwFormManager($scope.profile, ['title'], function () {
        var method = $scope.isNew ? 'createProfile' : 'updateProfile';
        ProjectService[method]($scope.project.id, $scope.profile);
    });


});

