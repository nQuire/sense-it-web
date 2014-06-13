angular.module('senseItWeb', null, null).directive('siwMenuItem', function () {
    return {
        templateUrl: 'partials/widgets/menu-item.html',
        scope: {
            'activeManager' : '=',
            'linkedState': '=',
            'label': '='
        }
    };
});
