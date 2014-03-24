angular.module('senseItWeb', null, null).controller(
    'ProfileCtrl',
    function ($scope, $window, OpenIdService) {
        OpenIdService.registerWatcher($scope, function () {
            if ($scope.status.profile) {
                $scope.form.setObject($scope.status.profile);
            }
        });

        $scope.form = new SiwFormManager(null, [ 'name' ], function () {
            $scope.status.newUser = false;
            $scope.openIdService.saveProfile();
        });

        $scope.logout = function () {
            $scope.openIdService.logout();
        };

        $scope.login = function (provider) {
            window.handleOpenIdResponse = function () {
                $scope.openIdService.update();
            };

            $scope.loginWindow = $window.open('login/redirect?p=' + provider);
        };
    });
