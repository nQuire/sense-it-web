'use strict';

describe('Project Comments', function () {

    var deleteButton = function () {
        return element(by.buttonText('Delete project'));
    };
    var editButton = function () {
        return element(by.buttonText('Edit'));
    };

    var saveButton = function () {
        return element(by.buttonText('Save'));
    };
    var cancelButton = function () {
        return element(by.buttonText('Cancel'));
    };

    var load = function(module) {
        var e2eDBModule = require('./db/' + module);
        browser.addMockModule('e2eDB', e2eDBModule.module);
        browser.get('#/project/101/edit');
    };

    beforeEach(function () {
        var e2eDBCommon = require('./db/db-base');
        browser.addMockModule('e2eDBCommon', e2eDBCommon.module);
    });

    /*describe('as non admin closed', function() {
        beforeEach(function () {
            load('p101_member_close');
        });

        it('should be empty', function () {
            expect(deleteButton().isPresent()).toBe(false);
            expect(editButton().isPresent()).toBe(false);
        });
    });

    describe('as admin open', function() {
        beforeEach(function () {
            load('p101_admin_open');
        });

        it('should be empty', function () {
            expect(deleteButton().isPresent()).toBe(false);
            expect(editButton().isPresent()).toBe(false);
        });
    });*/

    describe('as admin', function () {
        beforeEach(function () {
           load('p101_admin');
        });

        it('should have button', function () {
            expect(deleteButton().isPresent()).toBe(true);
            expect(editButton().isPresent()).toBe(true);
            expect(saveButton().isPresent()).toBe(false);
            expect(cancelButton().isPresent()).toBe(false);
        });

        it ('should have data', function() {
            expect(element(by.binding('project.title')).isPresent()).toBe(true);
            expect(element(by.binding('project.description.description')).isPresent()).toBe(true);
            expect(element(by.binding('project.description.image')).isPresent()).toBe(true);
            expect(element(by.binding('project.description.teaser')).isPresent()).toBe(true);
        });

        describe('when editing', function() {
            beforeEach(function() {
                editButton().click();
            });

            it('should show buttons', function() {
                expect(deleteButton().isPresent()).toBe(false);
                expect(editButton().isPresent()).toBe(false);
                expect(saveButton().isPresent()).toBe(true);
                expect(cancelButton().isPresent()).toBe(true);
            });

            it('should show form', function() {
                expect(element(by.model('form.values.title')).isPresent()).toBe(true);
                expect(element(by.model('form.values.description.description')).isPresent()).toBe(true);
                expect(element(by.model('form.values.description.html')).isPresent()).toBe(true);
                expect(element(by.model('form.values.description.teaser')).isPresent()).toBe(true);
            });
        });
    });
});
