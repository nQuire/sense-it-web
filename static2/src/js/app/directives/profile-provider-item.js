angular.module('senseItWeb').directive('siwProfileProviderItem', [function () {
    return {

        templateUrl: 'partials/profile/profile-provider-item.html',
        scope: true,
        controller: function ($scope, $attrs, OpenIdService) {
            var providerNames = {
                'google': 'Google',
                'facebook': 'Facebook',
                'twitter': 'Twitter'
            };

            $scope.providerId = $attrs['provider'];

            OpenIdService.registerWatcher($scope, function () {
                $scope.connectionExists = $scope.status.connections[$scope.providerId] ? true : false;
                $scope.profileUrl = $scope.connectionExists ? $scope.status.connections[$scope.providerId].providerProfileUrl : false;
                $scope.providerName = $scope.providerId in providerNames ? providerNames[$scope.providerId] : 'unknown';
            });
        }
    };
}]);
