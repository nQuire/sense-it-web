'use strict';

describe('Project Memnership', function () {

    var joinButton = function () {
        return element(by.buttonText('Join project'));
    };
    var leaveButton = function () {
        return element(by.buttonText('Leave project'));
    };

    var load = function(module) {
        var e2eDBModule = require('./db/' + module);
        browser.addMockModule('e2eDB', e2eDBModule.module);
        browser.get('#/project/101');
    }

    beforeEach(function () {
        var e2eDBCommon = require('./db/db-base');
        browser.addMockModule('e2eDBCommon', e2eDBCommon.module);
    });

    describe('when project closed', function() {
        beforeEach(function () {
            load('p101_no_member_close');
        });

        it('should be empty', function () {
            expect(leaveButton().isPresent()).toBe(false);
            expect(joinButton().isPresent()).toBe(false);
        });
    });

    describe('as non member open', function() {
        beforeEach(function () {
            load('p101_no_member_open');
        });

        it('should show join button', function () {
            expect(joinButton().isPresent()).toBe(true);
            expect(leaveButton().isPresent()).toBe(false);
        });

        it('should go to other page after login', function() {
            joinButton().click();
            expect(browser.getCurrentUrl()).toContain('#/project/101/challenge');
        });
    });

    describe('as member open', function() {
        beforeEach(function () {
            load('p101_member_open');
        });

        it('should show leave button', function () {
            expect(joinButton().isPresent()).toBe(false);
            expect(leaveButton().isPresent()).toBe(true);
        });

        it('should leave project', function() {
            leaveButton().click();
            expect(joinButton().isPresent()).toBe(true);
            expect(leaveButton().isPresent()).toBe(false);
        });
    });

});
