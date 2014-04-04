'use strict';

describe('Project Comments', function () {


    var goToComments = function () {
        browser.get('#/project/101/comments');
    };

    beforeEach(function () {
        var e2eDBCommon = require('./db/db-base');
        browser.addMockModule('e2eDBCommon', e2eDBCommon.module);
    });


    var expectCannotComment = function (error) {
        it('should show warning', function () {
            var warnings = ['nologged', 'closed', 'nomember'];

            for (var i = 0; i < warnings.length; i++) {
                var w = warnings[i];
                expect(element.all(by.css('.comments-noaccess-project-' + w)).count()).toBe(error == w ? 1 : 0);
            }
        });

        it('should not show comments', function () {
            expect(element.all(by.repeater('comment in comments.list')).count()).toBe(0);
        });

        it('should not have comment button', function () {
            expect(element(by.buttonText('New comment')).isPresent()).toBe(false);
        });

    };

    var expectCanComment = function () {

        it('should show no warnings', function () {
            expect(element.all(by.css('.comments-noaccess-project-nologged')).count()).toBe(0);
            expect(element.all(by.css('.comments-noaccess-project-closed')).count()).toBe(0);
            expect(element.all(by.css('.comments-noaccess-project-nomember')).count()).toBe(0);
        });


        it('should show comments', function () {
            expect(element.all(by.repeater('comment in comments.list')).count()).toBe(2);
        });

        it('should have comment button', function () {
            var comment = function () {
                return element(by.buttonText('New comment'));
            };
            var cancel = function () {
                return element(by.buttonText('Cancel'));
            };
            var submit = function () {
                return element(by.buttonText('Submit'));
            };
            var textarea = function () {
                return element(by.model('posting.comment'));
            };

            var expectButtons = function (open) {
                expect(comment().isPresent()).toBe(!open);
                expect(cancel().isPresent()).toBe(open);
                expect(submit().isPresent()).toBe(open);
                expect(textarea().isPresent()).toBe(open);
            };

            expectButtons(false);

            comment().click();
            expectButtons(true);

            submit().click();
            expectButtons(true);

            textarea().sendKeys('c3');
            expect(textarea().getAttribute('value')).toBe('c3');

            cancel().click();
            expectButtons(false);

            comment().click();
            expectButtons(true);
            expect(textarea().getAttribute('value')).toBe('');
            textarea().sendKeys('c3');
            expect(textarea().getAttribute('value')).toBe('c3');

            submit().click();
            expectButtons(false);

            expect(element.all(by.repeater('comment in comments.list')).count()).toBe(3);
        });
    };

    var expectCanDelete = function () {
        it('should show delete button', function () {
            var items = element.all(by.repeater('comment in comments.list'));
            expect(items.count()).toBe(2);

            element.all(by.repeater('comment in comments.list')).then(function (items) {
                expect(items[0].isElementPresent(by.buttonText('Delete'))).toBe(false);
                expect(items[1].isElementPresent(by.buttonText('Delete'))).toBe(true);
            });
        });


        it('should delete comment', function () {
            var items = element.all(by.repeater('comment in comments.list'));
            expect(items.count()).toBe(2);

            element.all(by.repeater('comment in comments.list')).then(function (items) {
                items[1].findElement(by.buttonText('Delete')).click();
                expect(element.all(by.repeater('comment in comments.list')).count()).toBe(1);
            });
        });

    };

    describe('As not logged', function () {
        beforeEach(function () {
            var e2eDBModule = require('./db/p101_nologged');
            browser.addMockModule('e2eDB', e2eDBModule.module);

            goToComments();
        });

        expectCannotComment('nologged');
    });


    describe('As member close project', function () {
        beforeEach(function () {
            var e2eDBModule = require('./db/p101_member_close');
            browser.addMockModule('e2eDB', e2eDBModule.module);

            goToComments();
        });

        expectCannotComment('closed');
    });


    describe('As no member close', function () {
        beforeEach(function () {
            var e2eDBModule = require('./db/p101_no_member_close');
            browser.addMockModule('e2eDB', e2eDBModule.module);

            goToComments();
        });

        expectCannotComment('closed');
    });

    describe('As no member open', function () {
        beforeEach(function () {
            var e2eDBModule = require('./db/p101_no_member_open');
            browser.addMockModule('e2eDB', e2eDBModule.module);

            goToComments();
        });

        expectCannotComment('nomember');
    });


    describe('As author', function () {
        beforeEach(function () {
            var e2eDBModule = require('./db/p101_admin');
            browser.addMockModule('e2eDB', e2eDBModule.module);

            goToComments();
        });

        expectCanComment();
    });

    describe('As member open project', function () {
        beforeEach(function () {
            var e2eDBModule = require('./db/p101_member_open');
            browser.addMockModule('e2eDB', e2eDBModule.module);

            goToComments();
        });

        expectCanComment();

        expectCanDelete();
    });


});
