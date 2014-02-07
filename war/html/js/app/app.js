'use strict';

/* App Module */

angular.module('senseItWeb', ['senseItServices', 'ui.router'], null).config([
    '$stateProvider',
    '$urlRouterProvider',
    function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('home', {
                url: '/home',
                templateUrl: 'html/partials/home.html',
                controller: 'HomeCtrl'
            })
            .state('projects', {
                url: '/projects',
                templateUrl: 'html/partials/projects.html',
                controller: 'ProjectListCtrl'
            })
            .state('create', {
                url: '/create',
                templateUrl: 'html/partials/create.html',
                controller: 'CreateCtrl'
            })
            .state('project-view', {
                url: '/project/{projectId}',
                templateUrl: 'html/partials/project-view.html',
                controller: 'ProjectViewCtrl'
            })
            .state('project-edit', {
                url: '/project/{projectId}/edit',
                templateUrl: 'html/partials/project-edit.html',
                controller: 'ProjectEditCtrl'
            })
        .state('profile', {
            url: '/profile',
            templateUrl: 'html/partials/profile.html',
            controller: 'ProfileCtrl'
        });

        $urlRouterProvider.otherwise('/home');
    }
]);
