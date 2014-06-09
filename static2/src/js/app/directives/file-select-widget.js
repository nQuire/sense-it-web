angular.module('senseItWeb', null, null).directive('siwFileSelectWidget', function () {
    return {
        templateUrl: 'partials/widgets/file-select-widget.html',

        link: function ($scope, element, params) {
            $scope.originalPath = params['original'];
            $scope.hasOriginal = !!$scope.originalPath;
            $scope.original = $scope.hasOriginal ? $scope.originalPath.substr(Math.max(0, $scope.originalPath.lastIndexOf('/') + 1)) : '';
            $scope.filekey = params['filekey'];
            $scope.selectedFile = null;

            $scope.hasFile = $scope.hasOriginal;

            $scope.originalModified = false;
            $scope.updateNewFile = false;

            $scope.actions = {
                setFile: function (file) {
                    $scope.filelistener.set($scope.filekey, file);
                    $scope.originalModified = true;
                    $scope.hasFile = true;
                    $scope.selectedFile = file;
                },
                cancel: function () {
                    angular.element(element).find('input[type="file"]').val(null);
                    $scope.filelistener.clear($scope.filekey);
                    $scope.originalModified = false;
                    $scope.hasFile = $scope.hasOriginal;
                    $scope.selectedFile = null;
                },
                deleteFile: function () {
                    angular.element(element).find('input[type="file"]').val(null);
                    $scope.originalModified = true;
                    $scope.filelistener.deleteFile($scope.filekey);
                    $scope.hasFile = false;
                    $scope.selectedFile = null;
                }
            };

            $scope.fileChosen = function(file) {
                $scope.$apply(function() {
                    $scope.actions.setFile(file);
                });
            };

            $scope.originalClass = function () {
                return $scope.originalModified ? 'file-widget-original-deleted' : '';
            };
        }
    };
});
