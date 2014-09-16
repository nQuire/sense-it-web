angular.module('senseItWeb', null, null).controller('ProjectViewSpotItItemCtrl', function ($scope) {
    $scope.form = new SiwFormManager(function () {
        return $scope.item;
    }, ['title'], function () {
        $scope.updateData($scope.item.id, {title: $scope.item.title});
    });

    $scope.rotateImage = function(direction) {
        $scope.updateData($scope.item.id, {rotate: direction}).then(function(updateData) {
            console.log(updateData);

            $scope.item = updateData;
            updateData.observation = updateData.observation + '?q=' + (new Date()).getTime();
        });
    };
});