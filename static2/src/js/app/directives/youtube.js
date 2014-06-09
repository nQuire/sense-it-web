angular.module('senseItWeb').directive('siwYoutube', function ($sce) {
    return {
        restrict: 'EA',
        scope: { code: '=' },
        replace: true,
        template: '<iframe allowfullscreen="" frameborder="0" height="180" width="320" src="{{url}}" ></iframe>',
        link: function (scope) {
            scope.$watch('code', function (code) {
                if (code) {
                    scope.url = $sce.trustAsResourceUrl("http://www.youtube.com/embed/" + code);
                }
            });
        }
    };
});