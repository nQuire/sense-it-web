'use strict';

angular.module('senseItServices', null, null).factory('ProjectDataService', ['ProjectService', function (ProjectService) {

    var service = {
    };

    service._itemRequest = function(itemType, method, projectId, type, dataId, data) {
        var path = type + '/' + itemType + (dataId ? '/' + dataId : '');
        return ProjectService.projectRequest(method, projectId, path, data);
    };

    service._dataRequest = function(method, projectId, type, dataId, data) {
        return service._itemRequest('data', method, projectId, type, dataId, data);
    };

    service._analysisRequest = function(method, projectId, type, dataId, data) {
        return service._itemRequest('analysis', method, projectId, type, dataId, data);
    };

    service.loadData = function(projectId, type) {
        return service._dataRequest('get', projectId, type, false, false);
    };

    service.createData = function(projectId, type, data) {
        return service._dataRequest('post', projectId, type, false, data);
    };

    service.loadAnalysis = function(projectId, type) {
        return service._analysisRequest('get', projectId, type, false, false);
    };

    service.createAnalysis = function(projectId, type, data) {
        return service._analysisRequest('post', projectId, type, false, data);
    };

    return service;
}]);
