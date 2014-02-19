'use strict';

angular.module('senseItServices', null, null).factory('ProjectChallengeAdminService', ['ProjectService', 'RestService', function (ProjectService, RestService) {

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

    service.setStage = function(projectId, stage) {
        var path = service._path('admin/stage');
        return ProjectService._updateProjectAction(projectId, 'put', path, {
            stage: stage
        });
    };

    service.getVotedAnswers = function(projectId) {
        return RestService.get('api/project/' + projectId + '/challenge/admin/answers').then(function(response) {
            return response.data;
        });
    };

    return service;
}]);
