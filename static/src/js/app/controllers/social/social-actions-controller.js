angular.module('senseItWeb', null, null).controller('SocialActionsCtrl', function ($scope, $state, ModalService, OpenIdService, RestService) {

    $scope.providers = {
        //'google': 'Google',
        'twitter': 'Twitter'
        //'facebook': 'Facebook'
    };



    $scope.actions = {
        post: function (provider) {
            $scope.result = {
                state: 'idle'
            };

            var posting = {
                title: $scope.socialPosting.title(provider, $scope.providers[provider]),
                content: $scope.socialPosting.template(provider, $scope.providers[provider])
            };

            ModalService.open({
                bodyTemplate: 'partials/social/social-post-dialog.html',
                title: 'Post on ' + $scope.providers[provider],
                size: 'md',
                status: $scope.status,
                result: $scope.result,
                provider: {
                    id: provider,
                    name: $scope.providers[provider],
                    connected: function () {
                        return $scope.status.logged && $scope.status.connections[provider];
                    },
                    login: function () {
                        OpenIdService.loginAndComeBack();
                    },
                    link: function () {
                        OpenIdService.providerLogin(provider);
                    }
                },
                posting: posting,
                okLabel: function () {
                    return $scope.result.state == 'completed' ? 'Close' : 'Post';
                },
                ok: function () {
                    if ($scope.result.state == 'idle') {
                        $scope.result.state = 'posting';
                        RestService.post('api/social/' + provider + '/post', {title: null, content: posting.content}).then(function (response) {
                            if (response && response.url) {
                                $scope.result.state = 'completed';
                                $scope.result.url = response.url;
                                $scope.result.error = null;
                            } else {
                                $scope.result.state = 'idle';
                                $scope.result.url = null;
                                $scope.result.error = response.error || 'unknown';
                            }
                        });
                        return false;
                    } else if ($scope.result.state == 'completed') {
                        return true;
                    }
                    return false;
                },
                okDisabled: function () {
                    return !($scope.status.logged && $scope.status.connections[provider]) || $scope.result.state == 'posting';
                },
                editDisabled: function () {
                    return $scope.result.state != 'idle';
                }
            });
        }
    };
});