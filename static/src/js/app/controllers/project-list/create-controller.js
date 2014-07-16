angular.module('senseItWeb', null, null).controller('CreateCtrl', function ($scope, ProjectService, $state) {
    $scope.type = null;
    $scope.title = '';

    $scope.createDisabled = true;

    $scope.select = function (type) {
        $scope.title = "";
        $scope.type = type;
        $scope.createDisabled = false;
    };

    $scope.typeClass = function (type) {
        return $scope.type && $scope.type != type ? 'project-create-type-hidden' : '';
    };

    $scope.cancel = function() {
        $scope.type = null;
    };

    $scope.create = function () {
        if (this.title && this.title.length > 0) {
            ProjectService.createProject($scope.type, this.title).then(function(projectId) {
                $state.go('project.edit.details', {projectId: projectId});
            });
        }
    };

});

