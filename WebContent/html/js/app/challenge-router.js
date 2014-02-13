'use strict';

angular.module('senseItWeb', null, null).config([
    '$stateProvider',
    '$urlRouterProvider',
    function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('project-view.challenge-list', {
                url: '/challenge/answers',
                templateUrl: 'html/partials/project-view-challenge-list.html',
                controller: 'ProjectViewChallengeListCtrl'
            })
            .state('project-view.challenge-new', {
                url: '/challenge/new-answer',
                templateUrl: 'html/partials/project-view-challenge-answer.html',
                controller: 'ProjectViewChallengeAnswerCtrl'
            })
            .state('project-view.challenge-answer', {
                url: '/challenge/answer/{answerId}',
                templateUrl: 'html/partials/project-view-challenge-answer.html',
                controller: 'ProjectViewChallengeAnswerCtrl'
            });
    }
]);
