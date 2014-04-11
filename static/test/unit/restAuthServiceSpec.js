'use strict';

describe('Rest+Auth tests', function () {
    var restService, token, openidService, httpMock, http, timeout;

    beforeEach(function () {
        module('senseItWeb');

        inject(function ($http, $httpBackend, $timeout, RestService, OpenIdService) {
            restService = RestService;
            openidService = OpenIdService;
            token = 'tkn';
            http = $http;
            httpMock = $httpBackend;
            httpMock.whenGET("api/security/status").respond(
                {
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
    });

    afterEach(function() {
        httpMock.verifyNoOutstandingExpectation();
        httpMock.verifyNoOutstandingRequest();
    });


    it('should have token', function () {
        expect(http.defaults.headers.common['nquire-it-token']).toBe(token);
    });


});

