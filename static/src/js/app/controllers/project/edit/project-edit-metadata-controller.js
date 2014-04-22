angular.module('senseItWeb', null, null).controller('ProjectEditMetadataCtrl', function ($scope, $state, ProjectService) {


    $scope.update = function () {
        if ($scope.project) {
            $scope.form.setObject($scope.project);
        }
    };

    $scope.form = new SiwFormManager($scope.project, ['title', 'description'], function () {
        ProjectService.saveMetadata($scope.project.id, $scope.form.files);
    });

    $scope.filelistener = {
        set: function (key, file) {
            $scope.form.setFile(key, file);
        },
        clear: function (key) {
            $scope.form.clearFile(key);
        },
        deleteFile: function (key) {
            $scope.form.deleteFile(key);
        }
    };


    var listener = $scope.$watch('project', function () {
        $scope.update();
    }, true);

    $scope.$on('$destroy', listener);

    $scope.update();
});

