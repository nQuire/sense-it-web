'use strict';

angular.module('senseItServices', null, null).factory('ProjectChallengeParticipantService', ['RestService', function (RestService) {

    var service = {
        _pathRequest: function (path, method, answerData) {
            if (answerData) {
                var promise = RestService[method](path, {
                    fieldValues: answerData.fieldValues,
                    published: answerData.published
                });
            } else {
                var promise = RestService[method](path);
            }

            return promise.then(function (response) {
                return response.data;
            });
        },
        _request: function (method, projectId, answerId, answerData) {
            var path = 'api/project/' + projectId + '/challenge/answers';
            if (answerId) {
                path += '/' + answerId;
            }
            return this._pathRequest(path, method, answerData);
        },
        getAnswers: function (projectId) {
            return this._request('get', projectId, false, false);
        },
        newAnswer: function (projectId, answer) {
            return this._request('post', projectId, false, answer);
        },
        updateAnswer: function (projectId, answer) {
            return this._request('put', projectId, answer.id, answer);
        },
        deleteAnswer: function (projectId, answerId) {
            return this._request('delete', projectId, answerId, false);
        },
        getVotedAnswers: function (projectId) {
            return RestService.get('api/project/' + projectId + '/challenge/votes').then(function (response) {
                return response.data;
            });
        }

    };

    return service;
}]);
