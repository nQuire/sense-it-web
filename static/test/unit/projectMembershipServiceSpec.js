'use strict';

describe('Project Membership tests', function () {
    var token, openidService, projectService, httpMock, http, timeout, scope;

    beforeEach(function () {
        module('senseItWeb');

        inject(function ($http, $httpBackend, $timeout, OpenIdService, ProjectService) {
            openidService = OpenIdService;
            projectService = ProjectService;

            token = 'tkn';
            http = $http;
            httpMock = $httpBackend;
            httpMock.whenGET("api/security/status").respond({
                "logged": true,
                "profile": {"id": 1, "username": "evilfer", "openIds": [
                    {"id": 2, "openId": "https://example.com/id?id=evf", "email": "evilfer@gmail.com"}
                ]},
                "token": token
            });

            timeout = $timeout;
        });

        httpMock.expectGET("api/security/status");
        openidService.update();
        httpMock.flush();
        timeout.flush();

        scope = {
            _events: {},
            _watches: {},
            $on: function (event, f) {
                this._events[event] = f;
            },
            $watch: function (watched, f) {
                this._watches[watched] = f;
                return 'stopWatching: ' + watched;
            }
        };

        spyOn(scope, '$on').andCallThrough();
        spyOn(scope, '$watch').andCallThrough();


    });

    afterEach(function () {
        httpMock.verifyNoOutstandingExpectation();
        httpMock.verifyNoOutstandingRequest();
    });


    it('should have token', function () {
        expect(http.defaults.headers.common['nquire-it-token']).toBe(token);
    });

    describe('for non-members', function() {
        beforeEach(function() {
            httpMock.expectGET("api/project/1000").respond({
                project: {id: 1000, title: "title", description: {}, type: "", activity: null, open: false},
                access: {member: false, admin: true, author: true}
            });

            projectService.registerGet(scope, 1000);
            httpMock.flush();
            timeout.flush();
        });

        it('should request join', function () {
            expect(projectService._projectData[1000].access.member).toBe(false);
            httpMock.expectPOST("api/project/1000/join").respond({member: true, admin: true, author: true});

            projectService.joinProject(1000);

            httpMock.flush();
            timeout.flush();

            expect(projectService._projectData[1000].access.member).toBe(true);
        });
    });

    describe('for members', function() {
        beforeEach(function() {
            httpMock.expectGET("api/project/1000").respond({
                project: {id: 1000, title: "title", description: {}, type: "", activity: null, open: false},
                access: {member: true, admin: true, author: true}
            });

            projectService.registerGet(scope, 1000);
            httpMock.flush();
            timeout.flush();
        });

        it('should request join', function () {
            expect(projectService._projectData[1000].access.member).toBe(true);
            httpMock.expectPOST("api/project/1000/leave").respond({member: false, admin: true, author: true});

            projectService.leaveProject(1000);

            httpMock.flush();
            timeout.flush();

            expect(projectService._projectData[1000].access.member).toBe(false);
        });
    });


});

