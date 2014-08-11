angular.module('senseItWeb', null, null).controller('ProjectViewSpotItUploadCtrl', function ($scope, fileReader) {

    $scope.formData = {
        imageSrc: null,
        title: ""
    };

    $scope.getPreviewFile = function () {
        fileReader.readAsDataUrl($scope.filelistener.file, $scope).then(function (result) {
            $scope.formData.imageSrc = result;
        });
    };

    $scope.resetPreview = function () {
        $scope.formData.imageSrc = null;
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
        $scope.createData({title: $scope.formData.title}, {image: $scope.filelistener.file}, true);
    };
    $scope.reset = function () {
        $scope.filelistener.reset();
        $scope.formData.title = "";
    };
});