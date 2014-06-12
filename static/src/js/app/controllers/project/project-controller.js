angular.module('senseItWeb', null, null).controller('ProjectCtrl', function ($scope, $state, ProjectService) {

    ProjectService.watchProject($scope, $state.params['projectId']);


    $scope.projectMenuIsActive = function(state) {
        return $state.current.name.indexOf('project.' + state) === 0;
    };


    $scope.commentThread = {
        type: 'project',
        id: $state.params['projectId']
    };

});

