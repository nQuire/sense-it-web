angular.module('senseItWeb', null, null).controller('ForumListForumCtrl', function ($scope) {

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
    }
});