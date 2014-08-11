'use strict';

angular.module('senseItServices', null, null).factory('CommentService', ['RestService', function (RestService) {


    var CommentManager = function (commentThread) {
        this.path = 'api/' + commentThread.path + '/comments';
        this.list = [];
        this.thread = commentThread.thread;
    };

    CommentManager.prototype.init = function () {
        this.list = [];
        var self = this;
        RestService.get(this.path).then(function (data) {
            self.list = data;
        });
    };

    CommentManager.prototype._update = function (list) {
        this.list = list;
        if (this.thread) {
            this.thread.commentCount = list.length;
        }
    };

    CommentManager.prototype.post = function (comment) {
        var self = this;
        return RestService.post(this.path, {comment: comment}).then(function (data) {
            self._update(data);
        });
    };

    CommentManager.prototype.deleteComment = function (commentId) {
        var self = this;
        return RestService.delete(this.path + '/' + commentId).then(function (data) {
            self._update(data);
        });
    };

    return {
        get: function (path, scope, updateCallback) {
            var stopWatching = scope.$watch('comments.list', updateCallback);
            scope.comments = new CommentManager(path);
            scope.$on('$destroy', stopWatching);
            scope.comments.init();
        }
    };
}]);
