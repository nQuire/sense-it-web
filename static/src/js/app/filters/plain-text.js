angular.module('senseItWeb', null, null).filter('plainText', function () {
    return function(text) {
        return String(text).replace(/<[^>]+>/gm, '');
    };
});
