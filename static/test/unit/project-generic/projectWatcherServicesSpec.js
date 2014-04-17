'use strict';


describe('Project Watcher Service tests', function () {

    var projectListResponse = {list: [
        {
            project: {id: 101, title: 'PIRATE Telescope', author: {id: 1, username: 'The Open University'}, type: 'challenge', activity: {}, description: {teaser: 'har har har', image: 'http://pirate.open.ac.uk/PIRATE_files/IMG_2861_CDK17_web.JPG'}, open: true},
            access: {member: true, admin: true, author: false},
            data: {members: 30, responses: 23}
        },
        {
            project: {id: 102, title: 'Fastest lift', type: 'senseit', activity: {}, author: {id: 2, username: 'nQuire Young Citizen project'}, description: {teaser: 'Find the fastest lift in the world', image: 'http://upload.wikimedia.org/wikipedia/commons/thumb/9/9b/Licenciado_Gustavo_D%C3%ADaz_Ordaz_International_Airport_%282014%29_-_03.JPG/450px-Licenciado_Gustavo_D%C3%ADaz_Ordaz_International_Airport_%282014%29_-_03.JPG'}, open: true},
            access: {member: true, admin: false, author: false},
            data: {members: 40, series: 230}
        },
        {
            project: {id: 103, title: 'Bumble bees', type: 'spotit', author: {id: 3, username: 'BSA'}, activity: {}, description: {teaser: "Help us find out what's happening to bumble bees!", image: 'http://upload.wikimedia.org/wikipedia/commons/thumb/4/4a/Bumblebee_October_2007-3a.jpg/800px-Bumblebee_October_2007-3a.jpg'}, open: true},
            access: {member: true, admin: false, author: false},
            data: {members: 40, spotted: 45}
        }
    ], categories: {all: 23, senseit: 8, challenge: 9, spotit: 6}
    };

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

    describe('project list watcher', function () {
        beforeEach(function () {
            projectService.watchList(scope);
            scope.projectListWatcher.query('all');

            httpMock.expectGET("api/projects").respond(projectListResponse);
            httpMock.flush();
            timeout.flush();
        });

        it('should have set scope', function() {
            expect(scope.projectListWatcher).toBeDefined();
        });

        it('should have initial projects', function() {
            expect(scope.projectList.ready).toBe(true);
            expect(scope.projectList.projects.length).toBe(3);
            expect(scope.projectList.categories).toBeDefined();
        });
    });

    describe('project 101 watcher', function () {
        beforeEach(function () {
            projectService.watchProject(scope, 101);

            httpMock.expectGET("api/project/101").respond(projectListResponse.list[0]);
            httpMock.flush();
            timeout.flush();
        });

        it('should have set scope', function() {
            expect(scope.projectWatcher).toBeDefined();
        });

        it('should have initial projects', function() {
            expect(scope.projectData.ready).toBe(true);
            expect(scope.projectData.project).toBeDefined();
            expect(scope.projectData.access).toBeDefined();
        });
    });

});

