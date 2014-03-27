


angular.module('senseItWeb', null, null).controller('ProjectEditMetadataCtrl', function ($scope, $state, ProjectService) {
    $scope.form = new SiwFormManager($scope.project, ['title', 'description'], function() {
        ProjectService.saveMetadata($scope.project.id, $scope.form.files).then(function(data) {
            console.log('updated: ' + data);
        });
    });

    $scope.$watch('project', function() {
        $scope.form.setObject($scope.project);
    });

});

