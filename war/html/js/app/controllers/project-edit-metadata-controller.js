


angular.module('senseItWeb', null, null).controller('ProjectEditMetadataCtrl', function ($scope, $state, ProjectService) {
    $scope.form = new siwFormManager($scope.data.project, ['title', 'description'], function() {
        ProjectService.saveMetadata($scope.data.project);
    });

});

