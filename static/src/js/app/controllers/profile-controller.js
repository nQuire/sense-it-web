angular.module('senseItWeb', null, null).controller('ProfileCtrl', function ($scope, $window, OpenIdService) {

    OpenIdService.registerWatcher($scope, function () {
        if ($scope.status.profile) {
            $scope.form.setObject($scope.status.profile);
        }
    });

    $scope.form = new SiwFormManager($scope.status.profile, [ 'username' ], function () {
        $scope.status.newUser = false;
        $scope.openIdService.saveProfile().then(function (data) {
            $scope.formError = data.explanation;
            if (data.explanation) {
                $scope.form.open();
            }
        });
    }, function() {
        $scope.formError = null;
    });


    $scope.logout = function () {
        $scope.openIdService.logout();
    };

    $scope.login = function (provider) {
        window.handleOpenIdResponse = function () {
            $scope.openIdService.update();
        };

        $scope.loginWindow = $window.open('social/' + provider + '/login');
    };

    $scope.formError = null;

    $scope.formErrorText = function() {
        switch ($scope.formError) {
            case 'username_empty':
                return 'Username cannot be empty';
            case 'username_not_available':
                return 'Username not available (already taken)';
            default:
                return '';
        }
    };
});
