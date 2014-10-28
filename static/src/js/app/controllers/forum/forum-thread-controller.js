angular.module('senseItWeb', null, null).controller('ForumThreadCtrl', function ($scope, $state) {

  $scope.forum.getThread($state.params.threadId);

  $scope.newComment = {comment: ''};
  $scope.form = new SiwFormManager($scope.newComment, ['comment'], function () {
    $scope.forum.postComment($scope.forum.thread.id, $scope.newComment.comment);
    $scope.newComment.comment = '';
  }, function () {
    $scope.newComment.comment = '';
  });

  $scope.okDisabled = function () {
    return $scope.form.values.comment.replace(/<[^>]+>/gm, '').trim().length == 0;
  };

  $scope.dataVoteManager = {
    votingEnabled: function () {
      return $scope.status.logged;
    },
    getPath: function (target) {
      return 'api/forum/' + $scope.forum.thread.id + '/comments/' + target.id + '/vote';
    }
  };
});