/*global $*/

angular.module('senseItWeb', null, null).directive('siwSideMenu', function ($state) {
    return {
        templateUrl: 'partials/side-menu.html',

        controller: function ($scope, $state, ProjectService) {
            var elements = $('#menuLink, #menu, #layout');

            $scope.projectId = false;

            $scope.state = $state;
            var listener = $scope.$watch('state.current', function () {
                $scope._updateMenu();
            }, true);

            $scope.$on('$destroy', listener);

            $scope.active = function (state) {
                return $scope.state.current.name.indexOf(state) === 0;
            };

            $scope.projectTitle = function () {
                return $scope.project ? $scope.project.title : '...';
            };

            $scope.open = function (state) {
                elements.removeClass('active');
                $state.go(state, {projectId: $scope.projectId});
            };

            $scope.projectItemsHidden = function (state) {
                var projectActive = Boolean($scope.project);
                var isAdmin = projectActive && $scope.access && $scope.access.admin;
                var editable = isAdmin && !$scope.project.open;

                switch (state) {
                    case 'project-view':
                        return !projectActive;
                    case 'project-admin':
                        return !isAdmin;
                    case 'project-edit':
                        return !editable;
                    default:
                        return false;
                }
            };


            $scope._updateMenu = function () {
                var projectPath = $scope.state.current.name.indexOf('project-view') == 0
                    || $scope.state.current.name.indexOf('project-edit') == 0
                    || $scope.state.current.name.indexOf('project-admin') == 0;

                console.log(projectPath);
                console.log($scope.state.params.projectId);

                if (projectPath) {
                    console.log('update project path');
                    $scope._setProject($scope.state.params.projectId);
                }
            };

            $scope._setProject = function (projectId) {
                if ($scope.projectId !== projectId) {
                    $scope._closeProjectItems();
                    $scope.projectId = projectId;

                    $scope.cancelProjectGet = ProjectService.registerGet($scope, $scope.projectId, function() {
                        console.log($scope);
                        if ($scope.project == false) {
                            $scope._setProject(null);
                        }
                    });
                }
            };

            $scope._viewState = null;

            $scope._closeProjectItems = function () {
                if ($scope.cancelProjectGet) {
                    $scope.cancelProjectGet();
                    $scope.cancelProjectGet = null;
                }
                $scope.projectId = false;
                $scope.project = null;
                $scope.access = null;
            };

            $scope.separateCreateItem = function () {
                return Boolean($scope.project);
            };
        }
    };
});
