'use strict';

angular.module('senseItServices', null, null).factory('ProjectSenseItService', ['ProjectService', function (ProjectService) {

    var service = {
    };

    /**
     *
     * @param {string} projectId
     * @param {string} [suffix=null]
     * @returns {string}
     * @private
     */
    service._path = function (projectId, suffix) {
        return 'senseit' + (suffix ? '/' + suffix : '');
    };

    /**
     *
     * @param {string} projectId
     * @param {string} inputId
     * @returns {string}
     * @private
     */
    service._inputPath = function (projectId, inputId) {
        return service._path(projectId, 'input/' + inputId);
    };


    service.createInput = function (projectId, input) {
        var path = service._path(projectId, 'inputs');
        return ProjectService.updateProjectAction('post', projectId, path, {
            sensor: input.sensor,
            rate: input.rate
        });
    };

    service.updateInput = function (projectId, input) {
        var path = service._inputPath(projectId, input.id);
        return ProjectService.updateProjectAction('put', projectId, path, {
            sensor: input.sensor,
            rate: input.rate
        });
    };

    service.deleteInput = function (projectId, inputId) {
        var path = service._inputPath(projectId, inputId);
        return ProjectService.updateProjectAction('delete', projectId, path);
    };

    service.updateProfile = function (projectId, profile) {
        var path = service._path(projectId, 'profile');
        return ProjectService.updateProjectAction('put', projectId, path, {
            geolocated: profile.geolocated,
            tx: profile.tx
        });
    };

    return service;
}]);
