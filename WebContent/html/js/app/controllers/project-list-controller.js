


angular.module('senseItWeb', null, null).controller('ProjectListCtrl', function ($scope, $state, RestService) {

    $scope.projects = {};
    $scope.loading = false;


    $scope._update = function() {
        $scope.loading = true;
        RestService.get("api/projects").then(function(data) {
            $scope.loading = false;
            $scope.projects = data;
        });
    };

    $scope.projectClass = function(project) {
        return 'project-' + project.type;
    };

    $scope.projectTypeLabel = function(project) {
        switch (project.type) {
            case 'senseit':
                return 'Sense-it';
            case 'challenge':
                return 'Challenge';
            default:
                return 'Other';
        }
    };

    $scope._update();
});

