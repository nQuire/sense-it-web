'use strict';

angular.module('senseItServices', null, null).factory('ProjectService', ['RestService', 'OpenIdService', function (RestService, OpenIdService) {

    var service = {
        _projectData: {}
    };

    service._load = function (id) {
        RestService.get('api/project/' + id).then(function (response) {
            service._projectData[id].project = response.data.project;
            service._projectData[id].access = response.data.access;
            service._projectData[id].ready = true;
        });
    };

    service._resetAccess = function () {
        for (var id in service._projectData) {
            for (var type in service._projectData[id].access) {
                service._projectData[id].access[type] = false;
            }
        }
    };

    service._updateAccess = function () {
        var ids = [];
        for (var id in service._projectData) {
            ids.push(id);
        }

        if (ids.length > 0) {
            RestService.post('api/projects/access', ids).then(function (response) {
                for (var id in response.data) {
                    service._projectData[id].access = response.data[id];
                }
            });
        }
    };

    service.registerGet = function (scope, projectId) {
        if (!(projectId in service._projectData)) {
            service._projectData[projectId] = {
                ready: false,
                project: null
            };
            service._load(projectId);
        }

        scope.projectServiceData = service._projectData[projectId];

        var listener = scope.$watch('projectServiceData', function () {
            scope.project = scope.projectServiceData.project;
            scope.access = scope.projectServiceData.access;
        }, true);

        scope.$on('$destroy', function () {
            listener();
        });
    };

    service.createProject = function (data) {
        return RestService.post('api/projects', data).then(function (response) {
            return response.data;
        });
    };

    service.deleteProject = function (projectId) {
        return RestService.delete('api/project/' + projectId).then(function (response) {
            return response.data;
        });
    };

    service._subscriptionAction = function (projectId, action) {
        return RestService.post('api/project/' + projectId + '/' + action).then(function (response) {
            console.log(response.data);
            if (response.data) {
                service._projectData[projectId].access = response.data;
            }
            return true;
        });
    };

    service.joinProject = function(projectId) {
        return service._subscriptionAction(projectId, 'join');
    };
    service.leaveProject = function(projectId) {
        return service._subscriptionAction(projectId, 'leave');
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
    service._updateProjectAction = function (projectId, method, path, data) {
        var url = 'api/project/' + projectId + '/' + path;
        var promise = data ? RestService[method](url, data) : RestService[method](url);
        return promise.then(function (response) {
            service._projectData[projectId].project = response.data;
            return true;
        });
    };

    service.saveMetadata = function (projectId) {
        var project = service._projectData[projectId].project;

        return service._updateProjectAction(projectId, 'put', 'metadata', {
            title: project.title,
            description: project.description
        });
    };

    service.openProject = function(projectId) {
        return service._updateProjectAction(projectId, 'put', 'open');
    };
    service.closeProject = function(projectId) {
        return service._updateProjectAction(projectId, 'put', 'close');
    };

    OpenIdService.registerListener(function (logged) {
        if (logged) {
            service._updateAccess();
        } else {
            service._resetAccess();
        }
    });


    return service;
}]);
