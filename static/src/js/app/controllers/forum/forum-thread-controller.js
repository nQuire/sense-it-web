angular.module('senseItWeb', null, null).controller('ForumThreadCtrl', function ($scope, $state, ModalService) {

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

  $scope.canDelete = function (comment) {
    return comment && $scope.status && $scope.status.logged && ($scope.status.profile.id == comment.author.id);
  };

  $scope.deleteComment = function (comment) {
    ModalService.open({
      body: 'Are you sure you want to delete this post?',
      title: 'Delete forum post',
      ok: function () {
        $scope.forum.deleteComment($scope.forum.thread.id, comment);
        return true;
      }
    });
  };

  $scope.dataVoteManager = {
    votingEnabled: function () {
      return $scope.status.logged;
    },
    reportingEnabled: function () {
      return $scope.status.logged;
    },
    getPath: function (target) {
      return 'api/forum/' + $scope.forum.thread.id + '/comments/' + target.id + '/vote';
    }
  };
});