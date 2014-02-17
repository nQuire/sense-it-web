'use strict';

angular.module('senseItServices', null, null).factory('ProjectChallengeVoteService', ['RestService', function (RestService) {

    var service = {
        _request: function(method, projectId, answerId, voteData) {
            var path =   'api/project/' + projectId + '/challenge/voting';
            if (answerId) {
                path += '/' + answerId;
            }

            if (voteData) {
                var promise = RestService[method](path, {
                    value: voteData.value,
                    comment: voteData.comment
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
        }
    };

    return service;
}]);
