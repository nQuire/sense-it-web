angular.module('senseItWeb', null, null).controller('ProjectCtrl', function ($scope, $state, ProjectService) {

    $scope.templates = {};

    $scope.projectMenu = {
        isActive: function (state) {
            return $state.current.name.indexOf('project.' + state) === 0;
        }
    };

    ProjectService.watchProject($scope, $state.params['projectId']);


    $scope.commentThread = {
        type: 'project',
        id: $state.params['projectId']
    };
});

