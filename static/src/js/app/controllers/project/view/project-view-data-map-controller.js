angular.module('senseItWeb', null, null).controller('ProjectViewDataMapCtrl', function ($scope, $state) {

    $scope.zoomToItem = $state.params.item;

});