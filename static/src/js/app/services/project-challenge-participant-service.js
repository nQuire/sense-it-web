'use strict';

angular.module('senseItServices', null, null).factory('ProjectChallengeParticipantService', ['ProjectService', function (ProjectService) {

    var service = {
        _answersPath: 'challenge/answers',
        _answerPath: function (answerId) {
            return 'challenge/answers/' + answerId;
        },
        _answerData: function (answer) {
            return {
                fieldValues: answer.fieldValues,
                published: answer.published
            };
        },
        getAnswers: function (projectId) {
            return ProjectService.projectRequest('get', projectId, this._answersPath);
        },
        newAnswer: function (projectId, answer) {
            return ProjectService.projectRequest('post', projectId, this._answersPath, this._answerData(answer));
        },
        updateAnswer: function (projectId, answer) {
            return ProjectService.projectRequest('put', projectId, this._answerPath(answer.id), this._answerData(answer));
        },
        deleteAnswer: function (projectId, answerId) {
            return ProjectService.projectRequest('delete', projectId, this._answerPath(answerId));
        },
        getVotedAnswers: function (projectId) {
            return ProjectService.projectRequest('get', projectId, 'challenge/votes');
        }
    };

    return service;
}]);
