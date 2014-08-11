angular.module('senseItWeb', null, null).directive('backImg', function(){
    return function(scope, element, attrs){
        attrs.$observe('backImg', function(value) {
            element.css({
                'background-image': value ? 'url("files/image/' + value +'")' : null,
                'background-size' : 'cover'
            });
        });
    };
});
