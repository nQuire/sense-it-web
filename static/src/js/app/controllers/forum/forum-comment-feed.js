
angular.module('senseItWeb', null, null).controller('ForumCommentFeedCtrl', function ($scope, RestService) {
  RestService.get('api/forum/feed').then(function(data) {
    $scope.feed = data;
  });

});

