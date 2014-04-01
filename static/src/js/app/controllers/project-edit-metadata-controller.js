


angular.module('senseItWeb', null, null).controller('ProjectEditMetadataCtrl', function ($scope, $state, ProjectService) {


    var update = function() {
        $scope.description = $scope.project.description;
        console.log('updated');
        console.log($scope.description);
    };

    $scope.form = new SiwFormManager($scope.project, ['title', 'description'], function() {
        ProjectService.saveMetadata($scope.project.id, $scope.form.files);
    });

    $scope.$watch('project', function() {
        update();
        $scope.form.setObject($scope.project);
    });

    update();
});

