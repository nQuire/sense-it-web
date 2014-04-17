angular.module('senseItWeb').directive('siwProjectDescription', [function() {
    return {
        templateUrl: 'partials/description-viewer.html',
        controller: function($scope, $element) {
            $scope.description = $scope.projectData.project.description;
            $scope.title = $scope.projectData.project.title;
        }
    };
  }
]);
