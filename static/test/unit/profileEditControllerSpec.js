'use strict';

describe('Profile Controller tests', function () {
    var token, rootScope, controllerService, httpMock, timeout;
    var parentScope, parentCtrl, scope, ctrl;

    beforeEach(function () {
        module('senseItWeb');

        inject(function ($rootScope, $controller, $httpBackend, $timeout) {
            token = 'tkn';
            httpMock = $httpBackend;
            rootScope = $rootScope;
            controllerService = $controller;
            timeout = $timeout;
        });


        httpMock.expectGET("api/security/status").respond({
            "logged": true,
            "profile": {"id": 1, "username": "me", "authorities": []},
            "token": token
        });

        httpMock.expectGET('partials/projects.html').respond(200);

        parentScope = rootScope.$new();
        parentCtrl = controllerService('MainCtrl', {$scope: parentScope});

        httpMock.flush();
        timeout.flush();

        scope = parentScope.$new();

        ctrl = controllerService('ProfileCtrl', {$scope: scope});
    });


    afterEach(function () {
        httpMock.verifyNoOutstandingExpectation();
        httpMock.verifyNoOutstandingRequest();
    });


    it('should have login info', function () {
        expect(scope.status).toBeDefined();
        expect(scope.status.logged).toBe(true);
        expect(scope.status.profile.id).toBe(1);
    });

    it('should update profile', function () {
        httpMock.expectPUT("api/security/profile", {username: 'me2'}).respond({
            logged: true,
            profile: {"id": 1, "username": "me2", "authorities": []},
            connections: {},
            responses: {}
        });

        scope.form.open();
        scope.form.values.username = 'me2';
        scope.form.save();


        httpMock.flush();
        timeout.flush();

        expect(scope.status.profile.username).toBe('me2');
        expect(scope.formError).toBeNull();
    });

    it('should logout', function() {
        httpMock.expectPOST("api/security/logout").respond({
            logged: false,
            profile: null,
            token: null
        });

        scope.logout();

        httpMock.flush();
        timeout.flush();

        expect(scope.status.logged).toBe(false);
        expect(scope.formError).toBeNull();
    });

    it('should indicate update error', function() {
        httpMock.expectPUT("api/security/profile", {username: 'me2'}).respond({
            logged: true,
            profile: {"id": 1, "username": "me", "authorities": []},
            connections: {},
            responses: {'username': 'username_not_available'}
        });

        scope.form.open();
        scope.form.values.username = 'me2';
        scope.form.save();


        httpMock.flush();
        timeout.flush();

        expect(scope.status.profile.username).toBe('me');
        expect(scope.formError).toBe('username_not_available');

        httpMock.expectPUT("api/security/profile", {username: 'me3'}).respond({
            logged: true,
            profile: {"id": 1, "username": "me3", "authorities": []},
            connections: {},
            responses: {}

        });

        scope.form.open();
        scope.form.values.username = 'me3';
        scope.form.save();


        httpMock.flush();
        timeout.flush();

        expect(scope.status.profile.username).toBe('me3');
        expect(scope.formError).toBeNull();
    });

});

