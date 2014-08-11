'use strict';

angular.module('senseItServices', null, null).factory('ForumService', ['RestService', function (RestService) {

    var ForumManager = function () {
        this.data = {};
    };


    ForumManager.prototype.getList = function () {
        var self = this;
        RestService.get('api/forum/list').then(function (data) {
            self.root = data;
        });
    };


    ForumManager.prototype.getNode = function (nodeId) {
        var self = this;
        if (self.node && self.node.id != nodeId) {
            self.node = null;
        }
        RestService.get('api/forum/' + nodeId).then(function (data) {
            self.node = data;
        });
    };


    ForumManager.prototype.getThread = function (threadId) {
        var self = this;
        if (self.thread && self.thread.id != threadId) {
            self.thread = null;
        }
        RestService.get('api/forum/thread/' + threadId).then(function (data) {
            self.thread = data;
        });
    };

    ForumManager.prototype.createCategory = function (data) {
        this.createForum(this.root.id, data);
    };

    ForumManager.prototype.createForum = function (containerId, data) {
        var self = this;
        RestService.post('api/forum/' + containerId + '/children', {
            title: data.title,
            text: data.description
        }).then(function (data) {
                self.root = data;
            });

    };

    ForumManager.prototype.updateNode = function (node) {
        var self = this;
        RestService.put('api/forum/' + node.id, {
            title: node.title,
            text: node.description
        }).then(function (data) {
                self.root = data;
            });
    };

    ForumManager.prototype.postNewThread = function (nodeId, data) {
        return RestService.post('api/forum/' + nodeId + '/threads', {
            title: data.title,
            text: data.message
        }).then(function (newThreadId) {
                return newThreadId;
            });
    };

    ForumManager.prototype.postComment = function (threadId, comment) {
        var self = this;
        return RestService.post('api/forum/' + threadId + '/comments', {
            comment: comment
        }).then(function (data) {
                self.thread = data;
            });
    };


    return {
        get: function (scope, updateCallback) {
            var stopWatching = scope.$watch('forum.data', updateCallback);
            scope.forum = new ForumManager();
            scope.$on('$destroy', stopWatching);
        }
    };
}]);