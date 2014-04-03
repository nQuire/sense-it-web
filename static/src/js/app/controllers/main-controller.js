angular.module('senseItWeb', null, null).controller('MainCtrl', function ($scope, $state, OpenIdService) {
    OpenIdService.registerWatcher($scope);
});
