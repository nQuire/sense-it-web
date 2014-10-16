/*global dataLayer*/

angular.module('senseItServices', null, null).factory('TrackingService', ['$rootScope', '$location', 'RestService',
  function ($rootScope, $location, RestService) {

    return {

      registerGA: function () {
        var pageView = function () {
          console.log('pageview');
          var path = $location.path();
          if (dataLayer) {
            dataLayer.push({
              'event': 'gaPageView',
              'gaPagePath': path
            });
          }
          RestService.post('api/log', {path: path});
        };

        $rootScope.$on('$stateChangeSuccess', pageView);
      }

    };
  }]);
