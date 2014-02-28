angular.module('senseItWeb', null, null).controller('ProjectViewCtrl', function ($scope, $state, ProjectService, OpenIdService) {
    OpenIdService.registerWatcher($scope);
    ProjectService.registerGet($scope, $state.params['projectId']);


    $scope.joinProject = function () {
        ProjectService.joinProject($state.params['projectId']);
    };

    $scope.leaveProject = function () {
        ProjectService.leaveProject($state.params['projectId']);
    };

    $scope.showDataMenu = function () {
        return $scope.project.type === 'senseit' && $scope.project.open && $scope.access.member;
    };


    $scope.templates = {
        home: null
    };

    $scope.projectTemplate = function () {
        switch ($scope.project.type) {
            case 'senseit':
                return 'partials/project-view-senseit.html';
            case 'challenge':
                return 'partials/project-view-challenge.html';
        }
        return null;
    };

});

