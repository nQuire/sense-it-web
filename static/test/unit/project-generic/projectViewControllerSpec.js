'use strict';

describe('Project View Controller tests', function () {

    var projectResponse = function (member) {
        return {
            project: {id: 101, title: 'PIRATE Telescope', author: {id: 1, username: 'The Open University'}, type: 'challenge', activity: {}, description: {teaser: 'har har har', image: 'http://pirate.open.ac.uk/PIRATE_files/IMG_2861_CDK17_web.JPG'}, open: true},
            access: {member: member, admin: true, author: true},
            data: {members: 30, responses: 23}
        };
    };


    var token, rootScope, controllerService, httpMock, timeout, state;
    var parentScope, parentCtrl, projectScope, projectCtrl, scope, ctrl;

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
            "profile": {"id": 1, "username": "me", "openIds": [
                {"id": 2, "openId": "https://example.com/id?id=me", "email": "me@example.com"}
            ]},
            "token": token
        });

        parentScope = rootScope.$new();
        parentCtrl = controllerService('MainCtrl', {$scope: parentScope});

        httpMock.flush();
        timeout.flush();

        httpMock.expectGET("api/project/101").respond(projectResponse(false));
        httpMock.expectGET("partials/projects/projects.html").respond(200);

        projectScope = parentScope.$new();

        state = {
            go: function () {
            },
            params: {'projectId': 101}
        };

        spyOn(state, 'go').andCallThrough();

        projectCtrl = controllerService('ProjectCtrl', {$scope: projectScope, $state: state});

        scope = projectScope.$new();
        ctrl = controllerService('ProjectViewCtrl', {$scope: scope});


        httpMock.flush();
        timeout.flush();
    });


    afterEach(function () {
        httpMock.verifyNoOutstandingExpectation();
        httpMock.verifyNoOutstandingRequest();
    });


    it('should have login info', function () {
        expect(parentScope.status).toBeDefined();
        expect(parentScope.status.logged).toBe(true);
        expect(parentScope.status.profile.id).toBe(1);
    });

    it('should have project data', function () {
        expect(scope.projectData.project).toBeDefined();
        expect(scope.projectData.ready).toBe(true);
        expect(scope.projectData.access).toBeDefined();
        expect(scope.projectData.data).toBeDefined();
    });

    it('should try to join', function () {
        scope.joinProject();

        httpMock.expectPOST("api/project/101/join").respond(projectResponse(true).access);

        httpMock.flush();
        timeout.flush();

        expect(scope.projectData.access.member).toBe(true);
    });

    it('should try to leave', function () {
        scope.leaveProject();

        httpMock.expectPOST("api/project/101/leave").respond(projectResponse(false).access);

        httpMock.flush();
        timeout.flush();

        expect(scope.projectData.access.member).toBe(false);
    });

});

