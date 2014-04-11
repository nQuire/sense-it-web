'use strict';

describe('Profile edit', function () {

    var usernameText = function () {
        return element(by.binding('status.profile.username'));
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
    var usernameField = function () {
        return element(by.model('form.values.username'));
    };

    var usernameErrorText = function () {
        return element(by.css('.profile-username-error'));
    };

    var checkElements = function (formOpen, usernameError) {
        expect(usernameText().isPresent()).toBe(!formOpen);
        expect(editButton().isPresent()).toBe(!formOpen);
        expect(saveButton().isPresent()).toBe(formOpen);
        expect(cancelButton().isPresent()).toBe(formOpen);
        expect(usernameField().isPresent()).toBe(formOpen);
        expect(usernameErrorText().isPresent()).toBe(formOpen && usernameError ? true : false);
    };


    var load = function (module) {
        var e2eDBModule = require('./db/' + module);
        browser.addMockModule('e2eDB', e2eDBModule.module);
        browser.get('#/profile');
    };

    describe('update ok', function () {
        beforeEach(function () {
            load('profile_update_ok');
            browser.debugger();
        });


        it('should have edit button', function () {
            checkElements(false, null);
        });

        it('should allow edit', function () {
            editButton().click();
            checkElements(true, null);
        });

        it('should cancel', function () {
            editButton().click();
            cancelButton().click();

            expect(usernameText().getText()).toBe("me");
            checkElements(false, null);
        });

        it('should save', function () {
            editButton().click();
            saveButton().click();

            expect(usernameText().getText()).toBe("available");
            checkElements(false, null);
        });
    });

    describe('update empty', function () {
        beforeEach(function () {
            load('profile_update_empty');
            browser.debugger();
        });

        it('should not save', function () {
            editButton().click();
            saveButton().click();

            checkElements(true, "Username cannot be empty");

            cancelButton().click();
            expect(usernameText().getText()).toBe("me");
            checkElements(false, null);

            editButton().click();
            checkElements(true, null);
        });
    });

    describe('update not available', function () {
        beforeEach(function () {
            load('profile_update_unavailable');
            browser.debugger();
        });

        it('should not save', function () {
            editButton().click();
            saveButton().click();

            checkElements(true, "Username not available (already taken)");

            cancelButton().click();
            expect(usernameText().getText()).toBe("me");
            checkElements(false, null);

            editButton().click();
            checkElements(true, null);
        });
    });
});
