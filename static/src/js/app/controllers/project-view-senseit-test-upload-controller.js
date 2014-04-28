angular.module('senseItWeb', null, null).controller('ProjectViewSenseItTestUpload', function ($scope, $http) {

    $scope.setFile = function (element) {
        $scope.$apply(function (scope) {
            scope.file = element.files.length > 0 ? element.files[0] : null;
        });
    };

    $scope.uploadFile = function () {
        if ($scope.title && $scope.file) {

            var fd = new FormData();
            fd.append("title", $scope.title);
            fd.append('file', $scope.file);

            $http.post('api/project/' + $scope.project.id + '/senseit/data', fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            }).
                success(function (data, status, headers, config) {
                    alert("success!");
                }).
                error(function (data, status, headers, config) {
                    alert("failed!");
                });
        }
    };

});