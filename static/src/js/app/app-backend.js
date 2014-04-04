"use strict";

console.log('app backend');

angular.module('senseItWebDev', ['senseItWeb', 'ngMockE2E', 'e2eDB']).run(function ($httpBackend, e2eDbRequests) {

    console.log('app backend');
    console.log(e2eDbRequests);
    console.log($httpBackend);

    $httpBackend.whenGET('index.hmtl').passThrough();
    $httpBackend.whenGET(/partials+/).passThrough();

    for (var request in e2eDbRequests.requests) {
        if (e2eDbRequests.requests.hasOwnProperty(request)) {
            var parts = request.split(" ");
            if (parts.length == 2 && parts[0] === 'POST') {
                $httpBackend.whenPOST(parts[1]).respond(e2eDbRequests.requests[request]);
            } else if (parts.length == 2 && parts[0] === 'DELETE') {
                $httpBackend.whenDELETE(parts[1]).respond(e2eDbRequests.requests[request]);
            } else {
                $httpBackend.whenGET(request).respond(e2eDbRequests.requests[request]);
            }
        }
    }
});
