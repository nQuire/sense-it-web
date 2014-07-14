angular.module('senseItWeb', null, null).controller('ProjectViewSpotItUploadCtrl', function ($scope, fileReader) {

    $scope.imageSrc = null;
    $scope.description = "";

    $scope.getPreviewFile = function () {
        fileReader.readAsDataUrl($scope.filelistener.file, $scope).then(function (result) {
            $scope.imageSrc = result;
        });
    };

    $scope.resetPreview = function () {
        $scope.imageSrc = null;
    };

    $scope.filelistener = {
        file: null,
        set: function (key, file) {
            this.file = file;
            $scope.getPreviewFile();
        },
        reset: function () {
            this.file = null;
            $scope.resetPreview();
        },
        clear: function (key) {
            this.reset();
        },
        deleteFile: function (key) {
            this.reset();
        }
    };

    $scope.upload = function () {
        $scope.createData({description: $scope.description}, {image: $scope.filelistener.file}, true);
    };
    $scope.reset = function () {
        $scope.filelistener.reset();
        $scope.description = "";
    };
});