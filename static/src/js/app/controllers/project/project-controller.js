angular.module('senseItWeb', null, null).controller('ProjectCtrl', function ($scope, $state, ProjectService) {

    ProjectService.watchProject($scope, $state.params['projectId']);

    $scope.showProjectMenu = function() {
        return $scope.projectData.access.admin;
    };

    $scope.projectMenuIsActive = function(state) {
        return $state.current.name.indexOf('project.' + state) === 0;
    };

});

