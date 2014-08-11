angular.module('senseItWeb', null, null).controller('ProjectEditSenseItProfileCtrl', function ($scope) {

    $scope.form = new SiwFormManager(function () {
        return $scope.projectData.project.activity.profile;
    }, ['geolocated'], function () {
        $scope.senseitEditor.updateProfile($scope.projectData.project.activity.profile);
    });


});

