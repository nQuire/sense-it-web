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
    service._path = function (suffix) {
        return 'challenge' + (suffix ? '/' + suffix : '');
    };

    /**
     *
     * @param {string} projectId
     * @param {string} fieldId
     * @returns {string}
     * @private
     */
    service._fieldPath = function (fieldId) {
        var suffix = 'field/' + fieldId;
        return service._path(suffix);
    };


    service.createField = function (projectId, field) {
        var path = service._path('fields');
        return ProjectService._updateProjectAction(projectId, 'post', path, {
            label: field.label,
            type: field.type
        });
    };

    service.updateField = function (projectId, field) {
        var path = service._fieldPath(field.id);
        return ProjectService._updateProjectAction(projectId, 'put', path, {
            label: field.label,
            type: field.type
        });
    };

    service.deleteField = function (projectId, fieldId) {
        var path = service._fieldPath(fieldId);
        return ProjectService._updateProjectAction(projectId, 'delete', path);
    };

    service.updateActivity = function(projectId, activity) {
        var path = service._path();
        return ProjectService._updateProjectAction(projectId, 'put', path, {
            maxAnswers: activity.maxAnswers
        });
    };

    service.setStage = function(projectId, stage) {
        var path = service._path('stage');
        return ProjectService._updateProjectAction(projectId, 'put', path, {
            stage: stage
        });
    };

    return service;
}]);
