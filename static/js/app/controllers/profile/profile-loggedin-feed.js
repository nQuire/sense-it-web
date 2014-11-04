
angular.module('senseItWeb', null, null).controller('ProfileLoggedInFeedCtrl', function ($scope, RestService) {
  RestService.get('api/profiles/feed').then(function(data) {
    $scope.feed = data;
  });

});

