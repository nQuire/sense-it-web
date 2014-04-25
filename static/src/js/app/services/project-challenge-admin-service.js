'use strict';

angular.module('senseItServices', null, null).factory('ProjectChallengeAdminService', ['ProjectService', function (ProjectService) {

    var ProjectChallengeAdmin = function(projectWatcher) {
        this.projectWatcher = projectWatcher;
    };


    ProjectChallengeAdmin.prototype.setStage = function(stage) {
        return this.projectWatcher.updateProjectAction('put', 'challenge/admin/stage', {
            stage: stage
        });
    };

    ProjectChallengeAdmin.prototype.getVotedAnswers = function() {
        return this.projectWatcher.projectRequest('get', 'challenge/admin/answers');
    };

    return {
        challengeAdmin: function(projectWatcher) {
            return new ProjectChallengeAdmin(projectWatcher);
        }
    };
}]);
