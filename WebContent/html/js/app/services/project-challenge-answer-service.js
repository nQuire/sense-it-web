'use strict';

angular.module('senseItServices', null, null).factory('ProjectChallengeAnswerService', ['RestService', function (RestService) {

    var service = {
        _request: function(method, projectId, answerId, answerData) {
            var path =   'api/project/' + projectId + '/challenge/answers';
            if (answerId) {
                path += '/' + answerId;
            }

            if (answerData) {
                var promise = RestService[method](path, {
                    fieldValues: answerData.fieldValues,
                    published: answerData.published
                });
            } else {
                var promise = RestService[method](path);
            }

            return promise.then(function(response) {
                return response.data;
            });
        },
        getAnswers: function(projectId) {
            return this._request('get', projectId, false, false);
        },
        newAnswer: function(projectId, answer) {
            return this._request('post', projectId, false, answer);
        },
        updateAnswer: function(projectId, answer) {
            return this._request('put', projectId, answer.id, answer);
        },
        deleteAnswer: function(projectId, answerId) {
            return this._request('put', projectId, answerId, false);
        }
    };

    return service;
}]);
