'use strict';

angular.module('senseItServices', null, null).factory('ProjectService', ['RestService', 'OpenIdService', function (RestService, OpenIdService) {

    var service = {
        _projectData: {}
    };

    service._composePath = function(path, suffix) {
        return path + (suffix ? '/' + suffix : '');
    };

    service._request = function(method, path, data) {
        return data ? RestService[method](path, data) : RestService[method](path);
    };

    service.projectRequest = function(method, projectId, path, data) {
        var _path = service._composePath('api/project/' + projectId, path);
        return service._request(method, _path, data);
    };

    service.projectsRequest = function(method, path, data) {
        var _path = service._composePath('api/projects', path);
        return service._request(method, _path, data);
    };


    service._reload = function() {
        var ids = [];
        for (var id in service._projectData) {
            ids.push(id);
        }

        if (ids.length > 0) {
            service.projectsRequest('post', 'reload', ids).then(function(data) {
                for (var id in service._projectData) {
                    service._projectData[id].project = null;
                    service._projectData[id].access = null;
                    service._projectData[id].ready = true;
                }

                for (var id in data) {
                    service._projectData[id].access = data[id].access;
                    service._projectData[id].project = data[id].project;
                }
            });
        }
    }

    service._load = function (id) {
        service.projectRequest('get', id).then(function(data) {
            service._projectData[id].project = data.project;
            service._projectData[id].access = data.access;
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
            service.projectsRequest('post', 'access', ids).then(function(data) {
                for (var id in data) {
                    service._projectData[id].access = data[id];
                }
            });
        }
    };

    service.registerGet = function (scope, projectId, callback) {
        if (!(projectId in service._projectData)) {
            service._projectData[projectId] = {
                ready: false,
                project: null,
                access: null
            };
            service._load(projectId);
        }

        scope.projectServiceData = service._projectData[projectId];

        var listener = scope.$watch('projectServiceData', function () {
            scope.project = scope.projectServiceData.project;
            scope.access = scope.projectServiceData.access;

            if (callback) {
                callback();
            }
        }, true);

        scope.$on('$destroy', function () {
            listener();
        });

        return listener;
    };



    service.createProject = function (data) {
        return service.projectsRequest('post', false, data);
    };

    service.deleteProject = function (projectId) {
        return service.projectRequest('delete', projectId).then(function(deleted) {
            if (deleted) {
                service._projectData[projectId].project = null;
                service._projectData[projectId].access = null;
                service._projectData[projectId].ready = true;
            }
            return deleted;
        });
    };

    service._subscriptionAction = function (projectId, action) {
        return service.projectRequest('post', projectId, action).then(function (data) {
            if (data) {
                service._projectData[projectId].access = data;
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
    service.updateProjectAction = function (method, projectId, path, data) {
        return service.projectRequest(method, projectId, path, data).then(function(data) {
            service._projectData[projectId].project = data;
            return true;
        });
    };

    service.saveMetadata = function (projectId) {
        var project = service._projectData[projectId].project;

        return service.updateProjectAction('put', projectId, 'metadata', {
            title: project.title,
            description: project.description
        });
    };

    service.openProject = function(projectId) {
        return service.updateProjectAction('put', projectId, 'admin/open');
    };
    service.closeProject = function(projectId) {
        return service.updateProjectAction('put', projectId, 'admin/close');
    };

    service.getUsers = function(projectId) {
        return service.projectRequest('get', projectId, 'admin/users');
    };

    OpenIdService.registerListener(function (logged) {
        if (logged) {
            service._updateAccess();
        } else {
            service._resetAccess();
        }
    });

    RestService.registerErrorListener(service._reload);

    return service;
}]);
