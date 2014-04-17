'use strict';

describe('Project Service Admin tests', function () {
    var token, openidService, projectService, httpMock, http, timeout, scope;


    var projectResponse = function (member) {
        return {
            project: {id: 101, title: 'PIRATE Telescope', author: {id: 1, username: 'The Open University'}, type: 'challenge', activity: {}, description: {teaser: 'har har har', image: 'http://pirate.open.ac.uk/PIRATE_files/IMG_2861_CDK17_web.JPG'}, open: true},
            access: {member: member, admin: true, author: true},
            data: {members: 30, responses: 23}
        };
    };

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


        httpMock.expectGET("api/project/101").respond(projectResponse(true));

        projectService.watchProject(scope, 101);
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

    it('should delete project', function () {
        expect(scope.projectData.project).toBeTruthy();
        scope.$watch.reset();

        httpMock.expectDELETE("api/project/101").respond(true);

        scope.projectWatcher.deleteProject();

        httpMock.flush();
        timeout.flush();

        expect(scope.projectData.project).toBe(null);
    });


    it('should update project', function () {
        expect(scope.projectData.project).toBeTruthy();

        scope.projectData.project.title = 'title!';
        scope.projectData.project.description = {'k': 'v'};

        httpMock.expectPOST("api/project/101/metadata", {title: 'title!', description: {'k': 'v'}}).respond(projectResponse(true));

        scope.projectWatcher.saveMetadata();

        httpMock.flush();
        timeout.flush();

        expect(scope.projectData.project).toBeTruthy();
    });


});

