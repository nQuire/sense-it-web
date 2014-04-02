'use strict';

/* App Module */

angular.module('senseItWeb', ['ngSanitize', 'ui.router', 'senseItServices'], null).config([
    '$stateProvider',
    '$urlRouterProvider',
    function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('home', {
                url: '/home',
                templateUrl: 'partials/projects.html',
                controller: 'ProjectListCtrl'
            })
            .state('about', {
                url: '/about',
                templateUrl: 'partials/about.html',
                controller: 'HomeCtrl'
            })
            .state('create', {
                url: '/create',
                templateUrl: 'partials/create.html',
                controller: 'CreateCtrl'
            })
            .state('project-view', {
                abstract: true,
                url: '/project/{projectId}',
                templateUrl: 'partials/project-view.html',
                controller: 'ProjectViewCtrl'
            })
            .state('project-view.home', {
                url: '',
                templateUrl: 'partials/project-view-home.html'
            })
            .state('project-view.data-list', {
                url: '/data',
                templateUrl: 'partials/project-view-data-table.html'
            })
            .state('project-view.analysis-list', {
                url: '/analysis',
                templateUrl: 'partials/project-view-data-analysis-table.html'
            })
            .state('project-view.analysis-view', {
                url: '/analysis/{analysisId}',
                templateUrl: 'partials/project-view-data-analysis-view.html'
            })
            .state('project-view.data-map', {
                url: '/map',
                templateUrl: 'partials/project-view-data-map.html'
            })
            .state('project-view.challenge', {
                url: '/challenge',
                templateUrl: 'partials/project-view-challenge-work.html'
            })
            .state('project-admin', {
                url: '/project/{projectId}/admin',
                templateUrl: 'partials/project-admin.html',
                controller: 'ProjectAdminCtrl'
            })
            .state('project-edit', {
                abstract: true,
                url: '/project/{projectId}/edit',
                templateUrl: 'partials/project-edit.html',
                controller: 'ProjectEditCtrl'
            })
            .state('project-edit.home', {
                url: '',
                templateUrl: 'partials/project-edit-metadata.html',
                controller: 'ProjectEditMetadataCtrl'
            })
            .state('project-edit.senseit', {
                url: '/senseit',
                templateUrl: 'partials/project-edit-senseit.html',
                controller: 'ProjectEditSenseItCtrl'
            })
            .state('project-edit.challenge', {
                url: '/challenge',
                templateUrl: 'partials/project-edit-challenge.html',
                controller: 'ProjectEditChallengeCtrl'
            })

            .state('profile', {
                url: '/profile',
                templateUrl: 'partials/profile.html',
                controller: 'ProfileCtrl'
            });

        $urlRouterProvider.otherwise('/home');
    }
]);
