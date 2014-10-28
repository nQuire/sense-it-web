'use strict';

angular.module('senseItServices', null, null).factory('ProjectChallengeParticipantService', ['OpenIdService', function (OpenIdService) {

    var ProjectChallengeParticipant = function (projectWatcher) {
        this._answersPath = 'challenge/answers';
        this.projectWatcher = projectWatcher;
        this.scope = projectWatcher.scope;

        this.scope.answerData = {
            answersReady: false,
            answers: [],
            editable: false,
            showVoting: true,
            votingEnabled: function () {
                return false;
            },
            showAuthor: false,
            showFilter: false,
            showPublished: false
        };

        var self = this;
        var openIdListener = function () {
            self._reload();
        };

        this.scope.$on('$destroy', function () {
            OpenIdService.removeListener(openIdListener);
        });

        OpenIdService.registerListener(openIdListener);

        this._reload();
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


    ProjectChallengeParticipant.prototype._reload = function () {
        var scope = this.scope;
        this.projectWatcher.projectRequest('get', this._answersPath).then(function (answers) {
            scope.answerData.answers = answers;
            scope.answerData.answersReady = true;
        });
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

    ProjectChallengeParticipant.prototype.submitAnswer = function (answer) {
        return this.projectWatcher.projectRequest('post', this._answerPath(answer.id) + '/' + (answer.published ? 'withdraw' : 'submit'));
    };


    return {
        challengeParticipant: function (projectWatcher) {
            return new ProjectChallengeParticipant(projectWatcher);
        }
    };

}]);
