'use strict';

describe('Comments Controller tests', function () {
    var token, rootScope, controllerService, httpMock, timeout, state;
    var parentScope, parentCtrl, scope, ctrl, metadataScope, metadataCtrl;

    beforeEach(function () {
        module('senseItWeb');

        inject(function ($rootScope, $controller, $httpBackend, $timeout) {
            token = 'tkn';
            httpMock = $httpBackend;
            rootScope = $rootScope;
            controllerService = $controller;
            timeout = $timeout;
        });


        httpMock.expectGET("api/openid/profile").respond({
            "logged": true,
            "profile": {"id": 1, "name": "me", "openIds": [
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

        state = {
            go: function () {
            },
            params: {'projectId': 1000}
        };

        spyOn(state, 'go').andCallThrough();

        ctrl = controllerService('ProjectEditCtrl', {$scope: scope, $state: state});

        metadataScope = scope.$new();
        metadataCtrl = controllerService('ProjectEditMetadataCtrl', {$scope: metadataScope});

        spyOn(metadataScope, 'update').andCallThrough();

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
        expect(scope.project).toBeDefined();
        expect(scope.access).toBeDefined();
    });

    it('should delete project', function () {
        httpMock.expectDELETE("api/project/1000").respond(true);

        scope.deleteProject();
        httpMock.flush();
        timeout.flush();

        expect(scope.project).toBe(null);
        expect(state.go).toHaveBeenCalledWith('home');
    });

    it('should update project', function () {
        httpMock.expectPOST("api/project/1000/metadata").respond({
            id: 1000, title: "title", description: {'k': 'v2'}, type: "", activity: null, open: false
        });


        metadataScope.form.open();
        metadataScope.project.description = {'k': 'v'};
        metadataScope.form.save();

        httpMock.flush();
        timeout.flush();

        expect(metadataScope.update).toHaveBeenCalled();
    });

});

