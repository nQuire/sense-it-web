angular.module('senseItWeb', null, null).controller('ProfileCtrl', function ($scope, $window, OpenIdService) {

    OpenIdService.registerWatcher($scope, function () {
        console.log('status changed');
        if ($scope.status.profile) {
            $scope.form.setObject($scope.status.profile);
        }
    });

    $scope.form = new SiwFormManager($scope.status.profile, [ 'username' ], function () {
        $scope.status.newUser = false;
        $scope.openIdService.saveProfile().then(function (data) {
            $scope.formError = data.responses.username || null;
            if ($scope.formError) {
                $scope.form.open();
            }
        });
    }, function () {
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

    $scope.deleteConnection = function (providerId) {
        $scope.openIdService.deleteConnection(providerId);
    };

    $scope.formError = null;

    $scope.formErrorText = function () {
        switch ($scope.formError) {
            case 'username_empty':
                return 'Username cannot be empty';
            case 'username_not_available':
                return 'Username not available (already taken)';
            default:
                return '';
        }
    };

    $scope.password = {
        set: function () {
            return $scope.status.profile.passwordSet;
        },
        editing: false,
        error: {
            oldPassword: false,
            newPassword: false,
            repeatPassword: false
        },
        edit: function () {
            this.editing = {oldPassword: '', newPassword: '', repeatPassword: ''};
        },
        close: function () {
            this.editing = false;
        },
        cancel: function () {
            this.close();
        },
        save: function () {
            if (this.editing.newPassword != this.editing.repeatPassword) {
                this.error.repeatPassword = 'Passwords do not match';
            } else {
                var self = this;
                var error = self.error;

                error.repeatPassword = false;
                OpenIdService.setPassword(this.editing.oldPassword, this.editing.newPassword).then(function (data) {
                    switch (data.responses.oldpassword) {
                        case 'bad_password':
                            error.oldPassword = 'Old password is not valid.';
                            break;
                        default:
                            error.oldPassword = false;
                            break;
                    }

                    switch (data.responses.newpassword) {
                        case 'too_short':
                            error.newPassword = 'New password is too short.';
                            break;
                        case 'same_as_username':
                            error.newPassword = 'New password cannot be equal to your username.';
                            break;
                        default:
                            error.newPassword = false;
                            break;
                    }

                    if (!error.repeatPassword && !error.newPassword && !error.oldPassword) {
                        self.close();
                    }
                });
            }
        }
    };
});
