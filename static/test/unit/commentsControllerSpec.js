'use strict';

describe('Comments Controller tests', function () {
    var token, rootScope, controllerService, httpMock, timeout;
    var parentScope, parentCtrl, scope, ctrl;

    beforeEach(function () {
        module('senseItWeb');

        inject(function ($rootScope, $controller, $httpBackend, $timeout) {
            token = 'tkn';
            httpMock = $httpBackend;
            rootScope = $rootScope;
            controllerService = $controller;
            timeout = $timeout;
        });



        httpMock.expectGET("api/security/status").respond({
            "logged": true,
            "profile": {"id": 1, "username": "me", "openIds": [
                {"id": 2, "openId": "https://example.com/id?id=me", "email": "me@example.com"}
            ]},
            "token": token
        });

        httpMock.expectGET('partials/projects.html').respond(200);

        parentScope = rootScope.$new();
        parentCtrl = controllerService('MainCtrl', {$scope: parentScope});
        parentScope.commentThread = {type: 'project', id: 1000};

        httpMock.flush();
        timeout.flush();

        httpMock.expectGET("api/project/1000/comments").respond([
            {id: 1001, user: {id: 1, username: 'me', comment: 'c1'}},
            {id: 1002, user: {id: 2, username: 'other', comment: 'c2'}}
        ]);

        scope = parentScope.$new();
        ctrl = controllerService('CommentsCtrl', {$scope: scope});

        httpMock.flush();
        timeout.flush();
    });


    afterEach(function () {
        httpMock.verifyNoOutstandingExpectation();
        httpMock.verifyNoOutstandingRequest();
    });


    it('should have login info', function () {
        expect(parentScope.status).toBeDefined();
        expect(parentScope.status.logged).toBe(true);
        expect(parentScope.status.profile.id).toBe(1);
    });

    it('should have two comments', function() {
        expect(scope.comments).toBeDefined();
        expect(scope.comments.list.length).toBe(2);
    });

    it('should allow delete my comments', function() {
        expect(scope.canDelete(1001)).toBe(true);
        expect(scope.canDelete(1002)).toBe(false);
        expect(scope.canDelete(1003)).toBe(false);
    });

    it('should post comment', function() {
        httpMock.expectPOST("api/project/1000/comments", '{"comment":"c3"}').respond([
            {id: 1001, user: {id: 1, username: 'me', comment: 'c1'}},
            {id: 1002, user: {id: 2, username: 'other', comment: 'c2'}},
            {id: 1003, user: {id: 1, username: 'me', comment: 'c3'}}
        ]);

        scope.posting.postComment('c3');

        httpMock.flush();
        timeout.flush();

        expect(scope.comments.list.length).toBe(3);
        expect(scope.commentById(1003)).toBeDefined();
        expect(scope.canDelete(1003)).toBe(true);
    });


    it('should open form', function() {
        expect(scope.posting.isOpen).toBe(false);
        scope.posting.open();
        expect(scope.posting.isOpen).toBe(true);
        scope.posting.cancel();
        expect(scope.posting.isOpen).toBe(false);
    });

    it('should not submit comment', function() {
        expect(scope.posting.isOpen).toBe(false);
        scope.posting.comment = 'c3';
        scope.posting.submit();
    });



    it('should submit non-empty comment', function() {
        httpMock.expectPOST("api/project/1000/comments", '{"comment":"c3"}').respond([
            {id: 1001, user: {id: 1, username: 'me', comment: 'c1'}},
            {id: 1002, user: {id: 2, username: 'other', comment: 'c2'}},
            {id: 1003, user: {id: 1, username: 'me', comment: 'c3'}}
        ]);

        scope.posting.open();
        scope.posting.comment = 'c3';
        scope.posting.submit();


        httpMock.flush();
        timeout.flush();

        expect(scope.posting.isOpen).toBe(false);
        scope.posting.open();
        expect(scope.posting.comment).toBe("");
        scope.posting.submit();
        expect(scope.posting.isOpen).toBe(true);
    });

    it('should delete comment', function() {
        expect(scope.comments.list.length).toBe(2);

        httpMock.expectDELETE("api/project/1000/comments/1001").respond([
            {id: 1002, author: {id: 2, username: 'other', comment: 'c2'}}
        ]);

        scope.posting.deleteComment(1001);

        httpMock.flush();
        timeout.flush();

        expect(scope.comments.list.length).toBe(1);
    });

});

