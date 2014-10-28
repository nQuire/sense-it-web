angular.module('senseItWeb', null, null).controller('AdminTextItemCtrl', function ($scope) {

    $scope.form = new SiwFormManager($scope.txt, [$scope.item.id], function() {
        $scope.admin.setText($scope.item.id, $scope.txt[$scope.item.id]);
    });

});