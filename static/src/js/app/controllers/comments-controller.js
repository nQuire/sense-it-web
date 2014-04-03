


angular.module('senseItWeb', null, null).controller('CommentsCtrl', function ($scope, CommentService) {
    CommentService.get($scope.commentThread.type, $scope.commentThread.id, $scope, function() {});

    $scope.commentById = function(id) {
        for (var i = 0; i < $scope.comments.list.length; i++) {
            if ($scope.comments.list[i].id == id) {
                return $scope.comments.list[i];
            }
        }

        return false;
    };

    $scope.canDelete = function(id) {
        var comment = $scope.commentById(id);
        return comment && $scope.status && $scope.status.logged && ($scope.status.profile.id == comment.author.id);
    };

    $scope.postComment = function(comment) {
        $scope.comments.post(comment);
    }

});