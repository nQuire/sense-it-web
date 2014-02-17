angular.module('senseItWeb', null, null).directive('siwSideMenu', function ($state) {
    return {
        templateUrl: 'partials/side-menu.html',

        controller: function ($scope, $state, ProjectService) {
            var elements = $('#menuLink, #menu, #layout');

            $scope.projectId = false;

            $scope.state = $state;
            $scope.$watch('state.current', function () {
                $scope._updateMenu();
            }, true);

            $scope.active = function (state) {
                if (state === 'project-view') {
                    return $scope.state.current.name.indexOf(state) === 0;
                } else {
                    return state === $scope.state.current.name;
                }
            };

            $scope.projectTitle = function() {
                return $scope.project ? $scope.project.title : '...';
            };

            $scope.open = function (state) {
                elements.removeClass('active');
                switch (state) {
                    case 'project-view':
                        if ($scope._viewState) {
                            $state.go($scope._viewState.name, $scope._viewState.params);
                        } else {
                            $state.go(state, {projectId: $scope.projectId});
                        }
                        break;
                    case 'project-edit':
                    case 'project-admin':
                        $state.go(state, {projectId: $scope.projectId});
                        break;
                    default:
                        $state.go(state);
                        break;
                }
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
                var projectPath = false;
                if ($scope.state.current.name.indexOf('project-view') == 0) {
                    $scope._viewState = {name: $scope.state.current.name, params: $scope.state.params};
                    projectPath = true;
                } else {
                    projectPath = $scope.state.current.name.indexOf('project-edit') == 0 || $scope.state.current.name.indexOf('project-admin') == 0;
                }

                if (projectPath) {
                    $scope._setProject($scope.state.params.projectId);
                }
            };

            $scope._setProject = function (projectId) {
                if ($scope.projectId !== projectId) {
                    $scope._closeProjectItems();
                    $scope.projectId = projectId;

                    $scope.cancelProjectGet = ProjectService.registerGet($scope, $scope.projectId);
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

            $scope.separateCreateItem = function() {
                return Boolean($scope.project);
            };
        }
    };
});
