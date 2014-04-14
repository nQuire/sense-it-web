'use strict';

describe('Project Service Admin tests', function () {
    var token, openidService, httpMock, http, timeout, scope;

    beforeEach(function () {
        module('senseItWeb');

        inject(function ($http, $httpBackend, $timeout, OpenIdService) {
            openidService = OpenIdService;

            token = 'tkn';
            http = $http;
            httpMock = $httpBackend;
            httpMock.whenGET("api/security/status").respond({
                "logged": true,
                "profile": {"id": 1, "username": "evilfer", "authorities": []},
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

    it('should contain initial profile', function () {
        expect(openidService.status.profile.username).toBe('evilfer');
    });

    it('should request update', function() {
        openidService.status.profile.username = 'evilfer2';
        httpMock.expectPUT('api/security/profile', {username: 'evilfer2'}).respond({
            logged: true,
            profile: {"id": 1, "username": "evilfer", "authorities": []},
            connections: [],
            responses: {}
        });
        openidService.saveProfile();
        httpMock.flush();
        timeout.flush();
    });

});

