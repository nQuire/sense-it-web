'use strict';

describe('Project Comments', function () {


    var goToComments = function () {
        browser.get('#/project/101/comments');
    };

    beforeEach(function () {
        var e2eDBCommon = require('./db/db-base');
        browser.addMockModule('e2eDBCommon', e2eDBCommon.module);
    });

    describe('As not logged', function () {
        beforeEach(function () {
            var e2eDBModule = require('./db/p101_nologged');
            browser.addMockModule('e2eDB', e2eDBModule.module);

            goToComments();
        });

        it('should show no-logged warning', function () {
            expect(element.all(by.css('.comments-noaccess-project-nologged')).count()).toBe(1);
            expect(element.all(by.css('.comments-noaccess-project-closed')).count()).toBe(0);
            expect(element.all(by.css('.comments-noaccess-project-nomember')).count()).toBe(0);
        });

    });


    describe('As author', function () {
        beforeEach(function () {
            var e2eDBModule = require('./db/p101_admin');
            browser.addMockModule('e2eDB', e2eDBModule.module);

            goToComments();
        });

        it('should show no warnings', function () {
            expect(element.all(by.css('.comments-noaccess-project-nologged')).count()).toBe(0);
            expect(element.all(by.css('.comments-noaccess-project-closed')).count()).toBe(0);
            expect(element.all(by.css('.comments-noaccess-project-nomember')).count()).toBe(0);
        });
    });

    describe('As member open project', function () {
        beforeEach(function () {
            var e2eDBModule = require('./db/p101_member_open');
            browser.addMockModule('e2eDB', e2eDBModule.module);

            goToComments();
        });

        it('should show no warnings', function () {
            expect(element.all(by.css('.comments-noaccess-project-nologged')).count()).toBe(0);
            expect(element.all(by.css('.comments-noaccess-project-closed')).count()).toBe(0);
            expect(element.all(by.css('.comments-noaccess-project-nomember')).count()).toBe(0);
        });
    });


    describe('As member close project', function () {
        beforeEach(function () {
            var e2eDBModule = require('./db/p101_member_close');
            browser.addMockModule('e2eDB', e2eDBModule.module);

            goToComments();
        });

        it('should show no warnings', function () {
            expect(element.all(by.css('.comments-noaccess-project-nologged')).count()).toBe(0);
            expect(element.all(by.css('.comments-noaccess-project-closed')).count()).toBe(1);
            expect(element.all(by.css('.comments-noaccess-project-nomember')).count()).toBe(0);
        });
    });


    describe('As no member close', function () {
        beforeEach(function () {
            var e2eDBModule = require('./db/p101_no_member_close');
            browser.addMockModule('e2eDB', e2eDBModule.module);

            goToComments();
        });

        it('should show no warnings', function () {
            expect(element.all(by.css('.comments-noaccess-project-nologged')).count()).toBe(0);
            expect(element.all(by.css('.comments-noaccess-project-closed')).count()).toBe(1);
            expect(element.all(by.css('.comments-noaccess-project-nomember')).count()).toBe(0);
        });
    });

    describe('As no member open', function () {
        beforeEach(function () {
            var e2eDBModule = require('./db/p101_no_member_open');
            browser.addMockModule('e2eDB', e2eDBModule.module);

            goToComments();
        });

        it('should show no warnings', function () {
            expect(element.all(by.css('.comments-noaccess-project-nologged')).count()).toBe(0);
            expect(element.all(by.css('.comments-noaccess-project-closed')).count()).toBe(0);
            expect(element.all(by.css('.comments-noaccess-project-nomember')).count()).toBe(1);
        });
    });

});

