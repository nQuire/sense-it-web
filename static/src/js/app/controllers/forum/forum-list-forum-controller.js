angular.module('senseItWeb', null, null).controller('ForumListForumCtrl', function ($scope, ModalService) {

  if ($scope.adminAccess) {

    $scope.isNew = !$scope.forumNode;
    if ($scope.isNew) {
      $scope.forumNode = {};
    }

    $scope.form = new SiwFormManager(function () {
      return $scope.forumNode;
    }, ['title', 'metadata'], function () {
      if ($scope.isNew) {
        $scope.forum.createForum($scope.category.id, $scope.forumNode);
      } else {
        $scope.forum.updateNode($scope.forumNode);
      }
    });

    $scope.deleteForum = function (forum) {
      ModalService.open({
        body: '<p>Are you sure you want to delete forum "' + forum.title + '"?</p>' +
        '<p>This will remove all the threads in it.</p>',
        title: 'Delete forum',
        ok: function () {
          $scope.forum.deleteNode(forum.id);
          return true;
        }
      });
    };
  }
});