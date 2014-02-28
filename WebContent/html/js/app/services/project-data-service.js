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

    service.loadData = function(project) {
        return service._dataRequest('get', project.id, project.type, false, false);
    };

    service.createData = function(project, data) {
        return service._dataRequest('post', project.id, project.type, false, data);
    };

    service.loadAnalysis = function(project) {
        return service._analysisRequest('get', project.id, project.type, false, false);
    };

    service.createAnalysis = function(project, data) {
        return service._analysisRequest('post', project.id, project.type, false, data);
    };

    service.updateAnalysis = function(project, analysisId, data) {
        return service._analysisRequest('put', project.id, project.type, analysisId, data);
    };

    return service;
}]);
