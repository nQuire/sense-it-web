angular.module('senseItWeb', null, null).controller('AdminProjectsCtrl', function ($scope) {

    $scope.admin.getProjects();

    $scope.actions = {
        toggleFeatured: function (project) {
            $scope.admin.setFeatured(project.id, !project.featured);
        }
    };

});