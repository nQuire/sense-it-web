'use strict';

angular.module('senseItServices', null, null).factory('ProjectService', ['$rootScope', 'RestService', function ($rootScope, RestService) {

    var service = {
        _projectData: {}
    };

    service._load = function (id) {
        RestService.get('api/project/' + id).then(function (response) {
            service._projectData[id].project = response.data;
            service._projectData[id].ready = true;
        });
    };

    service.get = function (projectId) {
        if (!(projectId in service._projectData)) {
            service._projectData[projectId] = {
                ready: false,
                project: null
            };
            service._load(projectId);
        }

        return service._projectData[projectId];
    };

    service.createProject = function (data) {
        return RestService.post('api/projects', data).then(function (response) {
            return response.data;
        });
    };

    service._updateProjectAction = function(projectId, method, url, data) {
        return RestService[method](url, data).then(function(response) {
            service._projectData[projectId].project = response.data;
            return true;
        });
    };

    service.saveMetadata = function (projectId) {
        var project = service._projectData[projectId].project;

        return service._updateProjectAction(projectId, 'put', 'api/project/' + projectId + '/metadata', {
            title: project.title,
            description: project.description
        });
    };

    service.createProfile = function (projectId, profile) {
        return service._updateProjectAction(projectId, 'post', 'api/project/' + projectId + '/senseit/profiles', {
            title: profile.title
        });
    };

    service.updateProfile = function (projectId, profile) {
        return service._updateProjectAction(projectId, 'put', 'api/project/' + projectId + '/senseit/profile/' + profile.id, {
            title: profile.title
        });
    };

    return service;
}]);
