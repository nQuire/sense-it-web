angular.module('senseItWeb', null, null).controller('MainCtrl', function ($scope, OpenIdService) {
    OpenIdService.registerWatcher($scope);
});
