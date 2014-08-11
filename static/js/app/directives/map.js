angular.module('senseItWeb').directive('siwMap', [function () {
    return {


        controller: function ($scope, $element) {
            console.log($scope.sortedData);

            $scope.renderer = new SiwMapRenderer($scope, $element, $scope.mapData, $scope.sortedData);

            $scope.$on('$destroy', $scope.$watch('sortedData.data', function () {
                $scope.renderer.update();
            }));

            $scope.$on('$destroy', $scope.$watch('mapData.selected', function () {
                $scope.renderer.updateSelection();
            }));

            $scope.$on('$destroy', $scope.$watch('mapData.textKey', function () {
                $scope.renderer.update();
            }));

        }
    };
}
]);
