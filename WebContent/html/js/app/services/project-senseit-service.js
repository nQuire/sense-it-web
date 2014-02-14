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




    service.createProfile = function (projectId, profile) {
        var path = service._path(projectId, 'profiles');
        return ProjectService._updateProjectAction(projectId, 'post', path, {
            title: profile.title
        });
    };

    service.updateProfile = function (projectId, profile) {
        var path = service._profilePath(projectId, profile.id);
        return ProjectService._updateProjectAction(projectId, 'put', path, {
            title: profile.title
        });
    };

    service.deleteProfile = function (projectId, profileId) {
        var path = service._profilePath(projectId, profileId);
        return ProjectService._updateProjectAction(projectId, 'delete', path);
    };

    service.createInput = function (projectId, profileId, input) {
        var path = service._profilePath(projectId, profileId, 'inputs');
        return ProjectService._updateProjectAction(projectId, 'post', path, {
            sensor: input.sensor,
            rate: input.rate
        });
    };

    service.updateInput = function (projectId, profileId, input) {
        var path = service._inputPath(projectId, profileId, input.id);
        return ProjectService._updateProjectAction(projectId, 'put', path, {
            sensor: input.sensor,
            rate: input.rate
        });
    };

    service.deleteInput = function (projectId, profileId, inputId) {
        var path = service._inputPath(projectId, profileId, inputId);
        return ProjectService._updateProjectAction(projectId, 'delete', path);
    };

    return service;
}]);
