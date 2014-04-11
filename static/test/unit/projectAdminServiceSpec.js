'use strict';

describe('Project Service Admin tests', function () {
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


        httpMock.expectGET("api/project/1000").respond({
            project: {id: 1000, title: "title", description: {}, type: "", activity: null},
            access: {member: true, admin: true, author: true}
        });


        projectService.registerGet(scope, 1000);
        httpMock.flush();
        timeout.flush();
    });

    afterEach(function () {
        httpMock.verifyNoOutstandingExpectation();
        httpMock.verifyNoOutstandingRequest();
    });


    it('should have token', function () {
        expect(http.defaults.headers.common['nquire-it-token']).toBe(token);
    });

    it('should receive project', function () {
        expect(scope.projectServiceData.project).toBeDefined();
        expect(scope.projectServiceData.access).toBeDefined();

        expect(scope.$watch).toHaveBeenCalled();
        expect(scope.$on).toHaveBeenCalled();
    });

    it('should request open', function() {
        httpMock.expectPUT("api/project/1000/admin/open").respond({});
        projectService.openProject(1000);

        httpMock.flush();
        timeout.flush();

        httpMock.expectPUT("api/project/1000/admin/close").respond({});
        projectService.closeProject(1000);

        httpMock.flush();
        timeout.flush();
    });

    it('should request users', function() {
        httpMock.expectGET("api/project/1000/admin/users").respond([]);
        projectService.getUsers(1000);

        httpMock.flush();
        timeout.flush();
    });


});

