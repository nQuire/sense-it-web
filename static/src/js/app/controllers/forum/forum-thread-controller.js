angular.module('senseItWeb', null, null).controller('ForumThreadCtrl', function ($scope, $state, ModalService) {

  $scope.forum.getThread($state.params.threadId);

  $scope.editingData = {};
  $scope.form = new SiwFormManager(function (opening) {
    if (opening) {
      var m = $scope.form.getMode();
      $scope.editingData.comment = m === 'new' ? '' : $scope.forum.thread.comments[m].comment;
      $scope.editingData.title = m === 0 ? $scope.forum.thread.title : null;
    }
    return $scope.editingData;
  }, ['comment', 'title'], function () {
    var m = $scope.form.getMode();

    if (m === 'new') {
      $scope.forum.postComment($scope.forum.thread.id, $scope.editingData.comment);
    } else if (m === 0) {
      $scope.forum.updateThread($scope.forum.thread.id, $scope.editingData.title, $scope.editingData.comment);
    } else {
      var cid = $scope.forum.thread.comments[m].id;
      $scope.forum.updateThreadComment($scope.forum.thread.id, cid, $scope.editingData.comment);
    }
  });

  $scope.okDisabled = function () {
    return $scope.form.values.comment.replace(/<[^>]+>/gm, '').trim().length == 0;
  };

  $scope.canEdit = function (comment) {
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
})
;