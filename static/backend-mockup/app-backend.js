"use strict";

var backendRequests = {};

angular.module('senseItWebDev', ['senseItWeb', 'ngMockE2E']).run(function ($httpBackend) {

    console.log('app backend');

    $httpBackend.whenGET(/partials+/).passThrough();

    for (var request in backendRequests) {
        if (backendRequests.hasOwnProperty(request)) {
            var parts = request.split(" ");
            if (parts.length == 2 && parts[0] === 'POST') {
                $httpBackend.whenPOST(parts[1]).respond(backendRequests[request]);
            } else if (parts.length == 2 && parts[0] === 'PUT') {
                $httpBackend.whenPUT(parts[1]).respond(backendRequests[request]);
            } else if (parts.length == 2 && parts[0] === 'DELETE') {
                $httpBackend.whenDELETE(parts[1]).respond(backendRequests[request]);
            } else {
                $httpBackend.whenGET(request).respond(backendRequests[request]);
            }
        }
    }
});
