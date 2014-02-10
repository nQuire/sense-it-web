


angular.module('senseItWeb', null, null).controller('ProjectViewCtrl', function ($scope, $state, ProjectService) {
    $scope.data = ProjectService.get($state.params['projectId']);
});

