"use strict";

console.log('app backend');

var senseItWebDev = angular.module('senseItWebDev', ['senseItWeb', 'ngMockE2E']);

senseItWebDev.run(function ($httpBackend) {

    senseItWebDev._httpBackend = $httpBackend;

    console.log('app backend');

    $httpBackend.whenGET('index.hmtl').passThrough();
    $httpBackend.whenGET(/partials+/).passThrough();
});

senseItWebDev.setUpLogin = function(logged, admin, member) {

};

senseItWebDev.setUpProject = function() {

};


