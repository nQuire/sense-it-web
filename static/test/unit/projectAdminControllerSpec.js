'use strict';

describe('Comments Controller tests', function () {
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
            "profile": {"id": 1, "username": "me", "openIds": [
                {"id": 2, "openId": "https://example.com/id?id=me", "email": "me@example.com"}
            ]},
            "token": token
        });

        httpMock.expectGET('partials/projects.html').respond(200);

        parentScope = rootScope.$new();
        parentCtrl = controllerService('MainCtrl', {$scope: parentScope});
        parentScope.commentThread = {type: 'project', id: 1000};

        httpMock.flush();
        timeout.flush();

        httpMock.expectGET("api/project/1000").respond({
            project: {id: 1000, title: "title", description: {}, type: "", activity: null, open: false},
            access: {member: true, admin: true, author: true}
        });

        scope = parentScope.$new();
        ctrl = controllerService('ProjectAdminCtrl', {$scope: scope, $state: {params: {'projectId': 1000}}});

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

    it('should have project data', function() {
        expect(scope.project).toBeDefined();
        expect(scope.access).toBeDefined();
    });

    it('should open/close project', function() {
        httpMock.expectPUT("api/project/1000/admin/open").respond({id: 1000, title: "title", description: {}, type: "", activity: null, open: true});

        scope.openProject();
        httpMock.flush();
        timeout.flush();

        expect(scope.project.open).toBe(true);

        httpMock.expectPUT("api/project/1000/admin/close").respond({id: 1000, title: "title", description: {}, type: "", activity: null, open: false});

        scope.closeProject();
        httpMock.flush();
        timeout.flush();

        expect(scope.project.open).toBe(false);
    });

});

