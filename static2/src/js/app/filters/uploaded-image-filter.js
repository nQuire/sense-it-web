angular.module('senseItWeb', null, null).filter('uploadedImage', function () {
    return function(input) {
        return input ? 'files/image/' + input : '';
    };
});
