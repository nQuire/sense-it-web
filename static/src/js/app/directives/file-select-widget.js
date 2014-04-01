angular.module('senseItWeb', null, null).directive('siwFileSelectWidget', function () {
    return {
        templateUrl: 'partials/file-select-widget.html',

        link: function ($scope, element, params) {
            $scope.filekey = params['filekey'];
            $scope.original = params['original'];

            $scope.hasOriginal = !!$scope.original;
            $scope.originalModified = false;
            $scope.updateNewFile = false;

            $scope.actions = {
                setFile: function (file) {
                    $scope.filelistener.set($scope.filekey, file);
                    $scope.originalModified = true;
                },
                cancel: function () {
                    angular.element(element).find('input[type="file"]').val(null);
                    $scope.filelistener.clear($scope.filekey);
                    $scope.originalModified = false;
                },
                deleteFile: function () {
                    angular.element(element).find('input[type="file"]').val(null);
                    $scope.originalModified = true;
                    $scope.filelistener.deleteFile($scope.filekey);
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
