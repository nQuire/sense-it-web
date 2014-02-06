


angular.module('senseItWeb', null, null).controller('ProjectEditCtrl', function ($scope, $state, ProjectService) {

    $scope.$watch('data.project', function() {
        console.log('update')
        $scope.project = $scope.data.project;
    });

    $scope.data = ProjectService.get($state.params['projectId']);

});

