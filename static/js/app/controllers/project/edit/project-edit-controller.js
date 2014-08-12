angular.module('senseItWeb', null, null).controller('ProjectEditCtrl', function ($scope, $state, ProjectService, ModalService) {

    $scope.templates.menu = null;

    $scope.deleteProject = function () {
        var title = $scope.projectData.project.title;
        ModalService.open({
            body: '<p>Are you sure you want to delete this project?</p>' +
                '<p><b>Please note that participants\' contributions will be permanently lost!</b></p>',
            title: 'Delete project ' + title,
            ok: function () {
                ModalService.open({
                    body: '<p>Sorry to ask again, but please confirm one last time that you want to delete this project.</p>' +
                        '<p><b>This action cannot be undone!</b></p>',
                    title: 'Delete project ' + title,
                    okLabel: function() {
                        return 'Yes, delete project ' + title;
                    },
                    ok: function() {
                        $scope.projectWatcher.deleteProject().then(function() {
                            $state.go('home');
                        });

                        return true;
                    }
                });

                return true;
            }
        });
//        ProjectService.deleteProject($scope.project.id).then(function () {

//        });
        //
    };

    $scope.openProject = function() {
        $scope.projectWatcher.openProject();
    };

    $scope.closeProject = function() {
        $scope.projectWatcher.closeProject();
    };

});

