'use strict';

angular.module('senseItServices', null, null).factory('AdminService', ['RestService', function (RestService) {

    var AdminManager = function () {
        this.data = {};
    };


    AdminManager.prototype.getUsers = function () {
        var self = this;
        RestService.get('api/admin/users').then(function (data) {
            self.data.users = data;
        });
    };

    AdminManager.prototype.setAdmin = function (userId, isAdmin) {
        var self = this;
        RestService.put('api/admin/user/' + userId + '/admin', {admin: isAdmin}).then(function (data) {
            self.data.users = data;
        });
    };

    AdminManager.prototype.getProjects = function () {
        var self = this;
        RestService.get('api/admin/projects').then(function (data) {
            self.data.projects = data;
        });
    };

    AdminManager.prototype.setFeatured = function (projectId, isFeatured) {
        var self = this;
        RestService.put('api/admin/project/' + projectId + '/featured', {featured: isFeatured}).then(function (data) {
            self.data.projects = data;
        });
    };

    return {
        get: function (scope, updateCallback) {
            var stopWatching = scope.$watch('admin.data', updateCallback);
            scope.admin = new AdminManager();
            scope.$on('$destroy', stopWatching);
        }
    };
}]);