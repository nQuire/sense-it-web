angular.module('senseItWeb', null, null).filter('fuzzyDate', function ($filter) {
    return function(input) {
        var now = new Date();
        var date = new Date(input);

        var format = 'short';
        if (now.getFullYear() == date.getFullYear() && now.getMonth() == date.getMonth()) {
            if (now.getDate() == date.getDate()) {
                format = "'Today' h:mm a";
            } else if (now.getDate() == date.getDate() + 1) {
                format = "'Yesterday' h:mm a";
            }
        }

        return $filter('date')(input, format);
    };
});
