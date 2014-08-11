angular.module('senseItWeb', null, null).controller('MainCtrl', function ($scope, OpenIdService, RestService) {
    OpenIdService.registerWatcher($scope);

    RestService.get('api/text').then(function(data) {
        $scope.txt = data;
    });
});
