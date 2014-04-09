'use strict';

describe('Comments Controller tests', function () {
    var token, rootScope, controllerService, httpMock, timeout, state;
    var parentScope, parentCtrl, scope, ctrl, metadataScope, metadataCtrl;

    var load = function (isMember, projectType) {
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
            project: {id: 1000, title: "title", description: {}, type: projectType, activity: null, open: false},
            access: {member: isMember, admin: true, author: true}
        });

        scope = parentScope.$new();

        state = {
            go: function () {
            },
            params: {'projectId': 1000}
        };

        spyOn(state, 'go');

        ctrl = controllerService('ProjectViewCtrl', {$scope: scope, $state: state});

        httpMock.flush();
        timeout.flush();
    };


    afterEach(function () {
        httpMock.verifyNoOutstandingExpectation();
        httpMock.verifyNoOutstandingRequest();
    });

    describe('as non-member/challenge', function() {
       beforeEach(function() {load(false, 'challenge');});

        it('should have project data', function () {
            expect(scope.project).toBeDefined();
            expect(scope.access).toBeDefined();
        });

        it('should not be member', function() {
            expect(scope.access.member).toBe(false);
        });

        describe('joining', function() {
            beforeEach(function() {
                httpMock.expectPOST("api/project/1000/join").respond({member: true, admin: true, author: true});
                scope.joinProject();

                httpMock.flush();
                timeout.flush();
            });

            it('should have joined project', function() {
                expect(scope.access.member).toBe(true);
            });

            it('should go to responses tab', function() {
                expect(state.go).toHaveBeenCalledWith('project-view.challenge');
            });
        });
    });

    describe('as non-member/senseit', function() {
        beforeEach(function() {load(false, 'senseit');});

        it('should have project data', function () {
            expect(scope.project).toBeDefined();
            expect(scope.access).toBeDefined();
        });

        it('should not be member', function() {
            expect(scope.access.member).toBe(false);
        });

        describe('joining', function() {
            beforeEach(function() {
                httpMock.expectPOST("api/project/1000/join").respond({member: true, admin: true, author: true});
                scope.joinProject();

                httpMock.flush();
                timeout.flush();
            });

            it('should have joined project', function() {
                expect(scope.access.member).toBe(true);
            });

            it('should go to data tab', function() {
                expect(state.go).toHaveBeenCalledWith('project-view.data-list');
            });
        });
    });


    describe('as challenge member', function() {
        beforeEach(function() {load(true, 'challenge');});

        it('should have project data', function () {
            expect(scope.project).toBeDefined();
            expect(scope.access).toBeDefined();
        });

        it('should be member', function() {
            expect(scope.access.member).toBe(true);
        });

        it('should leave project', function() {
            httpMock.expectPOST("api/project/1000/leave").respond({member: false, admin: true, author: true});
            scope.leaveProject();

            httpMock.flush();
            timeout.flush();

            expect(scope.access.member).toBe(false);
        });
    });

});

