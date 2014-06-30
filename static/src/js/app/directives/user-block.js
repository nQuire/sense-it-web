angular.module('senseItWeb', null, null).directive('siwUserImage', function () {
    return {
        templateUrl: 'partials/profile/user-image.html',
        scope: {
            'user' : '=',
            'showUsername': '='
        }
    };
});
