angular.module('senseItWeb', null, null).controller('ProjectViewSpotItItemCtrl', function ($scope) {
    $scope.form = new SiwFormManager(function () {
        return $scope.item;
    }, ['title'], function () {
        $scope.updateData($scope.item.id, {title: $scope.item.title});
    });

    $scope.deleteImage = function() {

    }
});