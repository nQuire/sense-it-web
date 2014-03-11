angular.module('senseItWeb', null, null).controller('ProjectEditSenseItProfileCtrl', function ($scope, ProjectSenseItService) {

    $scope.form = new SiwFormManager($scope.project.activity.profile, ['geolocated'], function () {
        var method = $scope.isNew ? 'createInput' : 'updateInput';
        ProjectSenseItService.updateProfile($scope.project.id, $scope.project.activity.profile);
    });

});

