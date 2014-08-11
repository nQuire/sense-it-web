angular.module('senseItWeb', null, null).controller('CommentsCtrl', function ($scope, CommentService) {

    CommentService.get($scope.commentThread, $scope, function () {});

    $scope.commentById = function (id) {
        for (var i = 0; i < $scope.comments.list.length; i++) {
            if ($scope.comments.list[i].id == id) {
                return $scope.comments.list[i];
            }
        }

        return false;
    };

    $scope.canDelete = function (id) {
        var comment = $scope.commentById(id);
        return comment && $scope.status && $scope.status.logged && ($scope.status.profile.id == comment.user.id);
    };


    $scope.posting = {
        isOpen: false,
        comment: "",
        postComment: function (comment) {
            $scope.comments.post(comment);
        },
        deleteComment: function(commentId) {
            $scope.comments.deleteComment(commentId);
        },
        open: function () {
            this.comment = "";
            this.isOpen = true;
        },
        cancel: function () {
            this.comment = "";
            this.isOpen = false;
        },
        submit: function () {
            if (this.comment.length > 0 && this.isOpen) {
                $scope.comments.post(this.comment);
                this.cancel();
            }
        }
    };

});