'use strict';

describe('Project Admin Controller tests', function () {
    var token, rootScope, controllerService, httpMock, timeout;
    var parentScope, parentCtrl, projectScope, projectCtrl, scope, ctrl;

    var projectResponse = function (open) {
        return {
            project: {id: 101, title: 'PIRATE Telescope', author: {id: 1, username: 'The Open University'}, type: 'challenge', activity: {}, description: {teaser: 'har har har', image: 'http://pirate.open.ac.uk/PIRATE_files/IMG_2861_CDK17_web.JPG'},
                open: open
            },
            access: {member: true, admin: true, author: true},
            data: {members: 30, responses: 23}
        };
    };

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


        httpMock.expectGET("api/project/101").respond(projectResponse(true));

        projectScope = parentScope.$new();

        var state = {
            go: function () {
            },
            params: {'projectId': 101}
        };

        spyOn(state, 'go').andCallThrough();

        projectCtrl = controllerService('ProjectCtrl', {$scope: projectScope, $state: state});

        httpMock.flush();
        timeout.flush();

        scope = projectScope.$new();
        ctrl = controllerService('ProjectAdminCtrl', {$scope: scope});
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

    it('should have project data', function() {
        expect(scope.projectData.project).toBeDefined();
        expect(scope.projectData.access).toBeDefined();
    });

    it('should open/close project', function() {
        httpMock.expectPUT("api/project/101/admin/open").respond(projectResponse(true));

        scope.openProject();
        httpMock.flush();
        timeout.flush();

        expect(scope.projectData.project.open).toBe(true);

        httpMock.expectPUT("api/project/101/admin/close").respond(projectResponse(false));

        scope.closeProject();
        httpMock.flush();
        timeout.flush();

        expect(scope.projectData.project.open).toBe(false);
    });

});

