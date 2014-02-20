'use strict';

angular.module('senseItServices', null, null).factory('VoteService', ['RestService', function (RestService) {

    var service = {
        vote: function(path, vote) {
            return RestService.post(path, {value: vote.value, comment: vote.comment ? vote.comment : ''});
        }
    };

    return service;
}]);
