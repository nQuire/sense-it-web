'use strict';

angular.module('senseItServices', null, null).factory('ProjectChallengeService', ['RestService', 'ProjectService', function (RestService, ProjectService) {

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
        return 'api/project/' + projectId + '/challenge' + (suffix ? '/' + suffix : '');
    };

    /**
     *
     * @param {string} projectId
     * @param {string} fieldId
     * @returns {string}
     * @private
     */
    service._fieldPath = function (projectId, fieldId) {
        var _suffix = 'field/' + fieldId;
        return service._path(projectId, _suffix);
    };


    service.createField = function (projectId, field) {
        var path = service._path(projectId, 'fields');
        return ProjectService._updateProjectAction(projectId, 'post', path, {
            label: field.label,
            type: field.type
        });
    };

    service.updateField = function (projectId, field) {
        var path = service._fieldPath(projectId, field.id);
        return ProjectService._updateProjectAction(projectId, 'put', path, {
            label: field.label,
            type: field.type
        });
    };

    service.deleteField = function (projectId, fieldId) {
        var path = service._fieldPath(projectId, fieldId);
        return ProjectService._updateProjectAction(projectId, 'delete', path);
    };

    service.updateActivity = function(projectId, activity) {
        var path = service._path(projectId);
        return ProjectService._updateProjectAction(projectId, 'put', path, {
            maxAnswers: activity.maxAnswers
        });
    };

    return service;
}]);
