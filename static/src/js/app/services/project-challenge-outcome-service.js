'use strict';

angular.module('senseItServices', null, null).factory('ProjectChallengeOutcomeService', ['ProjectService', function (ProjectService) {

    var ProjectChallengeOutcome = function (projectWatcher) {
        this.projectWatcher = projectWatcher;
    };


    ProjectChallengeOutcome.prototype.getOutcome = function () {
        return this.projectWatcher.projectRequest('get', 'challenge/outcome');
    };

    ProjectChallengeOutcome.prototype.selectAnswer = function (answerId) {
        return this.projectWatcher.projectRequest('put', 'challenge/outcome', {
            answerId: answerId
        });
    };

    ProjectChallengeOutcome.prototype.setExplanation = function (explanation) {
        return this.projectWatcher.projectRequest('put', 'challenge/outcome', {
            explanation: explanation
        });
    };


    return {
        challengeOutcome: function (projectWatcher) {
            return new ProjectChallengeOutcome(projectWatcher);
        }
    };

}]);
