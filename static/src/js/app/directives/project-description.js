angular.module('senseItWeb').directive('siwProjectDescription', [function() {
    return {
        templateUrl: 'partials/description-viewer.html',
        controller: function($scope, $element) {
            $scope.description = $scope.project.description;
        }
    };
  }
]);
