angular.module('senseItWeb', null, null).controller('ProjectEditSenseItProfileCtrl', function ($scope, ProjectSenseItService) {

    $scope.updateProfile = function() {
        ProjectSenseItService.updateProfile($scope.project.id, $scope.project.activity.profile);
    };

    $scope.form = new SiwFormManager($scope.project.activity.profile, ['geolocated'], $scope.updateProfile);

});

