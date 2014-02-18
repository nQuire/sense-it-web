'use strict';

angular.module('senseItServices', null, null).factory('VoteService', ['RestService', function (RestService) {

    var service = {
        _request: function(promise) {
            return promise.then(function(response) {
                return response.data;
            });
        },
        vote: function(path, vote) {
            return this._request(RestService.post(path, {value: vote.value, comment: vote.comment ? vote.comment : ''}));
        },
        delete: function(path, voteId) {
            return this._request(RestService.post(path + '/' + voteId));
        }
    };

    return service;
}]);
