'use strict';

angular.module('senseItServices', null, null).factory('ProjectDataService', ['ProjectService', function (ProjectService) {

    var ProjectDataService = function (projectWatcher) {
        this.projectWatcher = projectWatcher;
    };


    ProjectDataService.prototype._dataRequest = function (method, dataId, data) {
        var path = this.projectWatcher.data.project.type + '/data' + (dataId ? '/' + dataId : '');
        return this.projectWatcher.projectRequest(method, path, data);
    };


    ProjectDataService.prototype.loadData = function () {
        return this._dataRequest('get', false, false);
    };

    ProjectDataService.prototype.createData = function (data) {
        return this._dataRequest('post', false, data);
    };

    ProjectDataService.prototype.deleteData = function (project, data) {
        return this._dataRequest('delete', data.id, false);
    };


    return {
        dataService: function (projectWatcher) {
            return new ProjectDataService(projectWatcher);
        }
    };
}]);
