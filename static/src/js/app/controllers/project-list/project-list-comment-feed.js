
angular.module('senseItWeb', null, null).controller('ProjectListCommentFeedCtrl', function ($scope, RestService) {
  RestService.get('api/projects/feed').then(function(data) {
    $scope.feed = data;
  });

});

