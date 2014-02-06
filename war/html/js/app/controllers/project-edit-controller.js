


angular.module('senseItWeb', null, null).controller('ProjectEditCtrl', function ($scope, $state, ProjectService) {
    $scope.data = ProjectService.get($state.params['projectId']);
});

