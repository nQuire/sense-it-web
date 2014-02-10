'use strict';

angular.module('senseItServices', null, null).factory('ProjectService', ['RestService', function (RestService) {

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

    service.deleteProject = function(projectId) {
        return RestService.delete('api/project/' + projectId).then(function (response) {
            return response.data;
        });
    };

    /**
     *
     * @param {string} projectId
     * @param {string} method
     * @param {object} url
     * @param {object} [data=null]
     * @returns {object}
     * @private
     */
    service._updateProjectAction = function (projectId, method, url, data) {
        var promise = data ? RestService[method](url, data) : RestService[method](url);
        return promise.then(function (response) {
            console.log(response.data.title);
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

    return service;
}]);
