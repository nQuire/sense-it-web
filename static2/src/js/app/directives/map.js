angular.module('senseItWeb').directive('siwMap', [function() {
    return {

      
        controller: function($scope, $element) {
            $scope.renderer = new SiwMapRenderer($element, $scope.mapData, $scope.dataList);

            var listener = $scope.$watch('dataList.items', function() {
                $scope.renderer.update();
            });

            $scope.$on('$destroy', listener);
        }
    };
  }
]);
