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


    return {
        get: function (scope, updateCallback) {
            var stopWatching = scope.$watch('forum.data', updateCallback);
            scope.forum = new ForumManager();
            scope.$on('$destroy', stopWatching);
        }
    };
}]);