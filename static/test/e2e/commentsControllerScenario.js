'use strict';

describe('Comments Controller tests', function () {

    var goToComments = function() {
        browser.get('#/project/101/comments');
    };


    describe('As project author', function() {

        beforeEach(function() {
            var mockModule = require('./db/comments-project-author.js');
            browser.addMockModule('httpBackendMock', mockModule.httpBackendMock);
            goToComments();
        });

        it('should have no warnings', function() {
            expect($('.comments-noaccess-project-nologged').getSize()).toBe(0);
        })

    });

});

