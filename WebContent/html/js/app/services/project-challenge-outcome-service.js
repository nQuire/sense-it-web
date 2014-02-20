'use strict';

angular.module('senseItServices', null, null).factory('ProjectChallengeOutcomeService', ['ProjectService', function (ProjectService) {

    var service = {
        getOutcome: function(projectId) {
            return ProjectService.projectRequest('get', projectId, 'challenge/outcome');
        },
        selectAnswer: function(projectId, answerId) {
            return ProjectService.projectRequest('put', projectId, 'challenge/outcome', {
                answerId: answerId
            });
        },
        setExplanation: function(projectId, explanation) {
            return ProjectService.projectRequest('put', projectId, 'challenge/outcome', {
                explanation: explanation
            });
        }
    };

    return service;
}]);
