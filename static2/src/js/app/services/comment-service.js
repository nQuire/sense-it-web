'use strict';

angular.module('senseItServices', null, null).factory('CommentService', ['RestService', function (RestService) {


    var CommentManager = function (type, threadId) {
        this.path = 'api/' + (type ? type + '/' : '') + threadId + '/comments';
        this.list = [];
    };

    CommentManager.prototype.init = function () {
        this.list = [];
        var self = this;
        RestService.get(this.path).then(function (data) {
            self.list = data;
        });
    };

    CommentManager.prototype.post = function (comment) {
        var self = this;
        return RestService.post(this.path, {comment: comment}).then(function (data) {
            self.list = data;
        });
    };

    CommentManager.prototype.deleteComment = function (commentId) {
        var self = this;
        return RestService.delete(this.path + '/' + commentId).then(function (data) {
            self.list = data;
        });
    };

    return {
        get: function (type, threadId, scope, updateCallback) {
            var stopWatching = scope.$watch('comments.list', updateCallback);
            scope.comments = new CommentManager(type, threadId);
            scope.$on('$destroy', stopWatching);
            scope.comments.init();
        }
    };
}]);
