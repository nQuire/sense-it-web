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
            httpMock.whenGET("api/openid/profile").respond({
                "logged": true,
                "profile": {"id": 1, "name": "evilfer", "openIds": [
                    {"id": 2, "openId": "https://example.com/id?id=evf", "email": "evilfer@gmail.com"}
                ]},
                "token": token
            });

            timeout = $timeout;
        });

        httpMock.expectGET("api/openid/profile");
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
            project: {id: 1000, title: "title", description: {}, type: "", activity: null, open: false},
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
        expect(http.defaults.headers.common.token).toBe(token);
    });

    it('should delete project', function () {
        expect(projectService._projectData[1000].project).toBeTruthy();
        scope.$watch.reset();

        httpMock.expectDELETE("api/project/1000").respond(true);

        projectService.deleteProject(1000);

        httpMock.flush();
        timeout.flush();

        expect(projectService._projectData[1000].project).toBe(null);
    });


    it('should update project', function () {
        expect(projectService._projectData[1000].project).toBeTruthy();

        projectService._projectData[1000].project.title = 'title!';
        projectService._projectData[1000].project.description = {'k': 'v'};

        httpMock.expectPOST("api/project/1000/metadata", {title: 'title!', description: {'k': 'v'}}).respond(
            {id: 1000, title: "title!", description: {}, type: "", activity: null}
        );


        projectService.saveMetadata(1000);

        httpMock.flush();
        timeout.flush();

        expect(projectService._projectData[1000].project).toBeTruthy();
    });


});

