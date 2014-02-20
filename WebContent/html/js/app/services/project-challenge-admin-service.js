'use strict';

angular.module('senseItServices', null, null).factory('ProjectChallengeAdminService', ['ProjectService', 'RestService', function (ProjectService, RestService) {

    var service = {
    };


    service.setStage = function(projectId, stage) {
        return ProjectService.updateProjectAction('put', projectId, 'challenge/admin/stage', {
            stage: stage
        });
    };

    service.getVotedAnswers = function(projectId) {
        return ProjectService.projectRequest('get', projectId, 'challenge/admin/answers');
    };

    return service;
}]);
