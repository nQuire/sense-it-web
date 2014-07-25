angular.module('senseItWeb').directive('siwMap', [function() {
    return {


        controller: function($scope, $element) {
            console.log($scope.sortedData);

            $scope.renderer = new SiwMapRenderer($element, $scope.mapData, $scope.sortedData, $scope.zoomToItem);

            var listener = $scope.$watch('sortedData.data', function() {
                $scope.renderer.update();
            });

            $scope.$on('$destroy', listener);
        }
    };
  }
]);
