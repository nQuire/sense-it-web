angular.module('senseItWeb', null, null).filter('uploadedImage', function () {
    return function(input, defaultImage) {
        return input ? 'files/image/' + input : (defaultImage ? defaultImage : '');
    };
});

angular.module('senseItWeb', null, null).filter('uploadedThumb', function () {
    return function(input, defaultImage) {
        return input ? 'files/thumb/' + input : (defaultImage ? defaultImage : '');
    };
});
