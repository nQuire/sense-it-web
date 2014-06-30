angular.module('senseItWeb', null, null).filter('uploadedImage', function () {
    return function(input, defaultImage) {
        return input ? 'files/image/' + input : (defaultImage ? defaultImage : '');
    };
});
