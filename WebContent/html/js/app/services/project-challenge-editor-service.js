'use strict';

angular.module('senseItServices', null, null).factory('ProjectChallengeEditorService', ['RestService', 'ProjectService', function (RestService, ProjectService) {

    var service = {
    };


    service.createField = function (projectId, field) {
        return ProjectService.updateProjectAction('post', projectId, 'challenge/fields', {
            label: field.label,
            type: field.type
        });
    };

    service.updateField = function (projectId, field) {
        return ProjectService.updateProjectAction('put', projectId, 'challenge/field/' + field.id, {
            label: field.label,
            type: field.type
        });
    };

    service.deleteField = function (projectId, fieldId) {
        return ProjectService.updateProjectAction('delete', projectId, 'challenge/field/' + fieldId);
    };

    service.updateActivity = function(projectId, activity) {
        return ProjectService.updateProjectAction('put', projectId, 'challenge', {
            maxAnswers: activity.maxAnswers
        });
    };

    return service;
}]);
