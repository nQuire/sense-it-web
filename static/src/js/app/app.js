'use strict';

/* App Module */

angular.module('senseItWeb', ['ngSanitize', 'ui.router', 'senseItServices'], null).config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('home', {
            url: '/home',
            templateUrl: 'partials/projects/projects.html',
            controller: 'ProjectListCtrl'
        })
        .state('about', {
            url: '/about',
            templateUrl: 'partials/about.html'
        })
        .state('create', {
            url: '/create',
            templateUrl: 'partials/projects/create.html',
            controller: 'CreateCtrl'
        })
        .state('project', {
            abstract: true,
            url: '/project/{projectId}',
            templateUrl: 'partials/project/project.html',
            controller: 'ProjectCtrl'
        })
        .state('project.view', {
            abstract: true,
            url: '',
            templateUrl: 'partials/project/view/project-view.html',
            controller: 'ProjectViewCtrl'
        })
        .state('project.view.home', {
            url: '',
            templateUrl: 'partials/project/view/home/project-view-home.html'
        })
        .state('project.view.comments', {
            url: '/comments',
            templateUrl: 'partials/project/view/project-view-comments.html'
        })
        .state('project.view.data-list', {
            url: '/data',
            templateUrl: 'partials/project/view/data/project-view-data-table.html'
        })
        .state('project.view.data-map', {
            url: '/map',
            templateUrl: 'partials/project/view/data/project-view-data-map.html'
        })
        .state('project.view.challenge', {
            url: '/challenge',
            templateUrl: 'partials/project/view/challenge/project-view-challenge-work.html'
        })
        .state('project.admin', {
            url: '/admin',
            templateUrl: 'partials/project/admin/project-admin.html',
            controller: 'ProjectAdminCtrl'
        })
        .state('project.edit', {
            abstract: true,
            url: '/edit',
            templateUrl: 'partials/project/edit/project-edit.html',
            controller: 'ProjectEditCtrl'
        })
        .state('project.edit.home', {
            url: '',
            templateUrl: 'partials/project/edit/project-edit-metadata.html',
            controller: 'ProjectEditMetadataCtrl'
        })
        .state('project.edit.senseit', {
            url: '/senseit',
            templateUrl: 'partials/project/edit/senseit/project-edit-senseit.html',
            controller: 'ProjectEditSenseItCtrl'
        })
        .state('project.edit.challenge', {
            url: '/challenge',
            templateUrl: 'partials/project/edit/challenge/project-edit-challenge.html',
            controller: 'ProjectEditChallengeCtrl'
        })
        .state('profile', {
            url: '/profile',
            templateUrl: 'partials/profile.html',
            controller: 'ProfileCtrl'
        });

    $urlRouterProvider.otherwise('/home');
}]);
