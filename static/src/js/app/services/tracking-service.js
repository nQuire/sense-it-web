/*global dataLayer*/

angular.module('senseItServices', null, null).factory('TrackingService', ['$rootScope', '$location',
  function ($rootScope, $location) {

    return {
      registerGA: function () {
        if (dataLayer) {
          $rootScope.$on('$stateChangeSuccess', function () {
            dataLayer.push({
              'event': 'gaPageView',
              'gaPagePath': $location.path()
            });
          });
        }
      }
    };
  }]);
