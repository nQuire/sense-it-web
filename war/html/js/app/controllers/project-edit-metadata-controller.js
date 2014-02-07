


angular.module('senseItWeb', null, null).controller('ProjectEditMetadataCtrl', function ($scope, $state, ProjectService) {
    $scope.form = new siwFormManager($scope.project, ['title', 'description'], function() {
        ProjectService.saveMetadata($scope.project.id).then(function(data) {
            console.log('updated: ' + data);
        });
    });

    $scope.$watch('project', function() {
        $scope.form.setObject($scope.project);
    });

});

