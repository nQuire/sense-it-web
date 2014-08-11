'use strict';

angular.module('senseItServices', null, null).factory('ProjectChallengeEditorService', ['RestService', 'ProjectService', function (RestService, ProjectService) {



    var ProjectChallengeEditor = function(projectWatcher) {
        this.projectWatcher = projectWatcher;
    };





    ProjectChallengeEditor.prototype.createField = function (field) {
        return this.projectWatcher.updateProjectAction('post', 'challenge/fields', {
            label: field.label,
            type: field.type
        });
    };

    ProjectChallengeEditor.prototype.updateField = function (field) {
        return this.projectWatcher.updateProjectAction('put', 'challenge/field/' + field.id, {
            label: field.label,
            type: field.type
        });
    };

    ProjectChallengeEditor.prototype.deleteField = function (fieldId) {
        return this.projectWatcher.updateProjectAction('delete', 'challenge/field/' + fieldId);
    };

    ProjectChallengeEditor.prototype.moveField = function (fieldId, up) {
        return this.projectWatcher.updateProjectAction('post', 'challenge/field/' + fieldId + '/move', {up: up});
    };

    ProjectChallengeEditor.prototype.updateActivity = function(activity) {
        return this.projectWatcher.updateProjectAction('put', 'challenge', {
            maxAnswers: activity.maxAnswers
        });
    };

    return {
        challengeEditor: function(projectWatcher) {
            return new ProjectChallengeEditor(projectWatcher);
        }
    };

}]);
