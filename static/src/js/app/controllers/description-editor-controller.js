angular.module('senseItWeb', null, null).controller('DescriptionEditorCtrl', function ($scope, $state) {

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

});
