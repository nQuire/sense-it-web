angular.module('senseItWeb', null, null).controller('ProjectEditSenseItProfileCtrl', function ($scope, ProjectSenseItService) {
    $scope.isNew = typeof $scope.profile === 'undefined';
    if ($scope.isNew) {
        $scope.profile = {};
    }

    $scope.form = new siwFormManager($scope.profile, ['title'], function () {
        var method = $scope.isNew ? 'createProfile' : 'updateProfile';
        ProjectSenseItService[method]($scope.project.id, $scope.profile);
    });

    $scope.deleteProfile = function() {
        ProjectSenseItService.deleteProfile($scope.project.id, $scope.profile.id);
    };


});

