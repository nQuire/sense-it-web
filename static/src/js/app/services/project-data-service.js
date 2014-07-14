'use strict';

angular.module('senseItServices', null, null).factory('ProjectDataService', ['ProjectService', function (ProjectService) {

    var ProjectDataService = function (projectWatcher) {
        this.projectWatcher = projectWatcher;
    };


    ProjectDataService.prototype._dataRequest = function (method, dataId, data, files, convertToMultipart) {
        var path = this.projectWatcher.data.project.type + '/data' + (dataId ? '/' + dataId : '');
        return this.projectWatcher.projectRequest(method, path, data, files, convertToMultipart);
    };


    ProjectDataService.prototype.loadData = function () {
        return this._dataRequest('get', false, false);
    };


    ProjectDataService.prototype.deleteData = function (dataId) {
        return this._dataRequest('delete', dataId, false);
    };

    ProjectDataService.prototype.updateData = function(dataId, data) {
        return this._dataRequest('put', dataId, data);
    };


    ProjectDataService.prototype.uploadData = function(data, files, convertToMultipart) {
        return this._dataRequest('post', false, data, files, convertToMultipart);
    };


    return {
        dataService: function (projectWatcher) {
            return new ProjectDataService(projectWatcher);
        }
    };
}]);
