'use strict';

angular.module('senseItWeb', null, null).config([
    '$stateProvider',
    '$urlRouterProvider',
    function ($stateProvider) {
        $stateProvider
            .state('project-view.challenge-answer', {
                url: '/challenge/answer',
                templateUrl: 'partials/project-view-challenge-answer-list.html',
                controller: 'ProjectViewChallengeAnswerListCtrl'
            })
            .state('project-view.challenge-answer-new', {
                url: '/challenge/new-answer',
                templateUrl: 'partials/project-view-challenge-answer-item.html',
                controller: 'ProjectViewChallengeAnswerItemCtrl'
            })
            .state('project-view.challenge-answer-item', {
                url: '/challenge/answer/{answerId}',
                templateUrl: 'partials/project-view-challenge-answer-item.html',
                controller: 'ProjectViewChallengeAnswerItemCtrl'
            });
    }
]);
