'use strict';

angular.module('senseItServices', null, null).factory('ProjectSenseItService', ['RestService', 'ProjectService', function (RestService, ProjectService) {

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
        return 'api/project/' + projectId + '/senseit' + (suffix ? '/' + suffix : '');
    };

    /**
     *
     * @param {string} projectId
     * @param {string} profileId
     * @param {string} [suffix=null]
     * @returns {string}
     * @private
     */
    service._profilePath = function (projectId, profileId, suffix) {
        var _suffix = 'profile/' + profileId + (suffix ? '/' + suffix : '');
        return service._path(projectId, _suffix);
    };

    /**
     *
     * @param {string} projectId
     * @param {string} profileId
     * @param {string} inputId
     * @returns {string}
     * @private
     */
    service._inputPath = function (projectId, profileId, inputId) {
        return service._profilePath(projectId, profileId, 'input/' + inputId);
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
            ProjectService._projectData[projectId].project = response.data;
            return true;
        });
    };


    service.createProfile = function (projectId, profile) {
        var path = service._path(projectId, 'profiles');
        return service._updateProjectAction(projectId, 'post', path, {
            title: profile.title
        });
    };

    service.updateProfile = function (projectId, profile) {
        var path = service._profilePath(projectId, profile.id);
        return service._updateProjectAction(projectId, 'put', path, {
            title: profile.title
        });
    };

    service.deleteProfile = function (projectId, profileId) {
        var path = service._profilePath(projectId, profileId);
        return service._updateProjectAction(projectId, 'delete', path);
    };

    service.createInput = function (projectId, profileId, input) {
        var path = service._profilePath(projectId, profileId, 'inputs');
        return service._updateProjectAction(projectId, 'post', path, {
            sensor: input.sensor,
            rate: input.rate
        });
    };

    service.updateInput = function (projectId, profileId, input) {
        var path = service._inputPath(projectId, profileId, input.id);
        return service._updateProjectAction(projectId, 'put', path, {
            sensor: input.sensor,
            rate: input.rate
        });
    };

    service.deleteInput = function (projectId, profileId, inputId) {
        var path = service._inputPath(projectId, profileId, inputId);
        return service._updateProjectAction(projectId, 'delete', path);
    };

    return service;
}]);
