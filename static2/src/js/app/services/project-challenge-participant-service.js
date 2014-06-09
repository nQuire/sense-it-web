'use strict';

angular.module('senseItServices', null, null).factory('ProjectChallengeParticipantService', [function () {

    var ProjectChallengeParticipant = function (projectWatcher) {
        this._answersPath = 'challenge/answers';
        this.projectWatcher = projectWatcher;
    };

    ProjectChallengeParticipant.prototype._answerPath = function (answerId) {
        return this._answersPath + '/' + answerId;
    };

    ProjectChallengeParticipant.prototype._answerData = function (answer) {
        return {
            fieldValues: answer.fieldValues,
            published: answer.published
        };
    };


    ProjectChallengeParticipant.prototype.getAnswers = function () {
        return this.projectWatcher.projectRequest('get', this._answersPath);
    };


    ProjectChallengeParticipant.prototype.newAnswer = function (answer) {
        return this.projectWatcher.projectRequest('post', this._answersPath, this._answerData(answer));
    };

    ProjectChallengeParticipant.prototype.updateAnswer = function (answer) {
        return this.projectWatcher.projectRequest('put', this._answerPath(answer.id), this._answerData(answer));
    };

    ProjectChallengeParticipant.prototype.deleteAnswer = function (answerId) {
        return this.projectWatcher.projectRequest('delete', this._answerPath(answerId));
    };

    ProjectChallengeParticipant.prototype.getVotedAnswers = function () {
        return this.projectWatcher.projectRequest('get', 'challenge/votes');
    };


    return {
        challengeParticipant: function (projectWatcher) {
            return new ProjectChallengeParticipant(projectWatcher);
        }
    };

}]);
