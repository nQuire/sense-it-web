'use strict';

describe('Project List Controller tests', function () {

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


    var token, rootScope, controllerService, httpMock, timeout, state;
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

        parentScope = rootScope.$new();
        parentCtrl = controllerService('MainCtrl', {$scope: parentScope});
        parentScope.commentThread = {type: 'project', id: 1000};

        httpMock.flush();
        timeout.flush();

        httpMock.expectGET("api/projects").respond(projectListResponse);

        scope = parentScope.$new();

        state = {
            go: function () {
            },
            params: {}
        };

        spyOn(state, 'go').andCallThrough();

        ctrl = controllerService('ProjectListCtrl', {$scope: scope, $state: state});

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
        expect(scope.projectList.projects.length).toBe(3);
        expect(scope.projectList.ready).toBe(true);
        expect(scope.projectList.categories).toBeDefined();
    });

});

