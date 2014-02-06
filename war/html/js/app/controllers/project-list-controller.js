


angular.module('senseItWeb', null, null).controller('ProjectListCtrl', function ($scope, $state, RestService) {

    $scope.projects = {};
    $scope.loading = false;


    $scope._update = function() {
        $scope.loading = true;
        RestService.get("/api/projects").then(function(response) {
            console.log(response);
            $scope.loading = false;
            $scope.projects= response.data;
        });
    };

    $scope._update();
});

