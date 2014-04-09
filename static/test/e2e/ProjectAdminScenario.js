'use strict';

describe('Project Comments', function () {

    var openButton = function () {
        return element(by.buttonText('Open project'));
    };
    var closeButton = function () {
        return element(by.buttonText('Close project'));
    };
    var editButton = function () {
        return element(by.buttonText('Edit project'));
    };

    var load = function(module) {
        var e2eDBModule = require('./db/' + module);
        browser.addMockModule('e2eDB', e2eDBModule.module);
        browser.get('#/project/101/admin');
    }

    beforeEach(function () {
        var e2eDBCommon = require('./db/db-base');
        browser.addMockModule('e2eDBCommon', e2eDBCommon.module);


    });

    describe('as non admin', function() {
        beforeEach(function () {
            load('p101_member_close');
        });

        it('should be empty', function () {
            expect(openButton().isPresent()).toBe(false);
            expect(closeButton().isPresent()).toBe(false);
            expect(editButton().isPresent()).toBe(false);
        });

    });

    describe('as admin', function () {

        beforeEach(function () {
           load('p101_admin');
        });

        it('should have close button', function () {
            expect(openButton().isPresent()).toBe(true);
            expect(closeButton().isPresent()).toBe(false);
            expect(editButton().isPresent()).toBe(true);
        });


        it('should open project', function () {
            openButton().click();

            expect(openButton().isPresent()).toBe(false);
            expect(closeButton().isPresent()).toBe(true);
            expect(editButton().isPresent()).toBe(false);
        });


        it('should go to edit', function () {
            editButton().click();
            expect(browser.getCurrentUrl()).toContain('#/project/101/edit');
        });
    });
});
