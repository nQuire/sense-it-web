'use strict';

describe('Profile edit', function () {

    var links = ['home', 'about', 'create', 'profile'];

    var navLink = function (link) {
        return element(by.css('#navbar-' + link));
    };

    var testActiveLink = function(activeLink) {
        links.forEach(function (link) {
            var button = navLink(link);
            var parent = button.findElement(by.xpath('ancestor::li'));
            expect(parent.getAttribute('class')).toMatch(activeLink === link ? 'active' : '');
        });
    };


    describe('front page', function () {

        beforeEach(function () {
            browser.get('index-e2e.php?maps=loggedout,projects');
        });

        it('should have all buttons', function () {
            links.forEach(function (link) {
                expect(navLink(link).isPresent()).toBe(true);
            });
        });

        it('home should be activated', function() {
            testActiveLink('home');
        });

        it('should go to about', function() {
            navLink('about').click();
            testActiveLink('about');
        });

        it('should go to create', function() {
            navLink('create').click();
            testActiveLink('create');
        });

        it('should go to profile', function() {
            navLink('profile').click();
            testActiveLink('profile');
        });
    });

    describe('create page', function () {

        beforeEach(function () {
            browser.get('index-e2e.php?maps=loggedout,projects#/create');
        });

        it('home should be activated', function() {
            testActiveLink('create');
        });

        it('should go to home', function() {
            navLink('home').click();
            testActiveLink('home');
        });

    });

});
