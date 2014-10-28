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

  AdminManager.prototype.setText = function (key, content) {
    RestService.put('api/admin/text', {
      key: key,
      content: content
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

  AdminManager.prototype._reportedContentRequest = function (method, path) {
    var self = this;
    return RestService.request(method, path).then(function (data) {
      if (data) {
        self.data.reported = data;
      }
    });
  };

  AdminManager.prototype.getReportedContent = function () {
    return this._reportedContentRequest('get', 'api/admin/reported');
  };

  AdminManager.prototype.deleteReportedContent = function (id) {
    return this._reportedContentRequest('delete', 'api/admin/reported/' + id);
  };

  AdminManager.prototype.approveReportedContent = function (id) {
    return this._reportedContentRequest('post', 'api/admin/reported/' + id + '/approve');
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