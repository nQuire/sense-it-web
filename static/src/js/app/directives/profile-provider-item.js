angular.module('senseItWeb').directive('siwProfileProviderItem', [function () {
    return {
        templateUrl: 'partials/profile-provider-item.html',
        scope: true,
        controller: function($scope, $attrs, OpenIdService) {
            $scope.providerId = $attrs['provider'];

            OpenIdService.registerWatcher($scope, function () {
                $scope.connectionExists = $scope.status.connections[$scope.providerId] ? true : false;
                $scope.profileUrl = $scope.connectionExists ? $scope.status.connections[$scope.providerId].providerProfileUrl : false;

                switch ($scope.providerId) {
                    case 'google':
                        $scope.providerName = 'Google';
                        break;
                    case 'facebook':
                        $scope.providerName = 'Facebook';
                        break;
                }
            });
        }
    };
}]);
