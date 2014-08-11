angular.module('senseItWeb', null, null).controller('ProjectViewCtrl', function ($scope, $state, ProjectService) {




    $scope.joinProject = function () {
        $scope.projectWatcher.joinProject();
    };

    $scope.leaveProject = function () {
        $scope.projectWatcher.leaveProject();
    };

    $scope.showProjectViewMenu = function() {
        return $scope.status.logged && $scope.projectData.project.open && $scope.projectData.access.member;
    };

    $scope.projectViewMenuIsActive = function(state) {
        return $state.current.name.indexOf('project.view.' + state) === 0;
    };



    $scope.projectTemplate = function () {
        if ($scope.projectData.project) {
            switch ($scope.projectData.project.type) {
                case 'senseit':
                    return 'partials/project/view/home/project-view-senseit.html';
                case 'challenge':
                    return 'partials/project/view/home/project-view-challenge.html';
                case 'spotit':
                    return 'partials/project/view/home/project-view-spotit.html';
            }
        }
        return null;
    };


});

