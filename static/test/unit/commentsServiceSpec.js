'use strict';

describe('Comments Service tests', function () {
    var restService, token, openidService, commentService, httpMock, http, timeout, scope;

    beforeEach(function () {
        module('senseItWeb');

        inject(function ($http, $httpBackend, $timeout, RestService, OpenIdService, CommentService) {
            restService = RestService;
            openidService = OpenIdService;
            commentService = CommentService;

            token = 'tkn';
            http = $http;
            httpMock = $httpBackend;
            httpMock.whenGET("api/security/status").respond(
                {
                    "logged": true,
                    "profile": {"id": 1, "username": "evilfer", "openIds": [
                        {"id": 2, "openId": "https://example.com/id?id=evf", "email": "evilfer@gmail.com"}
                    ]},
                    "token": token
                });

            httpMock.whenGET("api/project/1000/comments").respond([{id: 1001, user: {id: 1, username: 'evilfer'}, comment: 'x'}]);
            httpMock.whenDELETE("api/project/1000/comments/1001").respond([]);
            httpMock.whenPOST("api/project/1000/comments").respond([{id: 1001, user: {id: 1, username: 'evilfer'}, comment: 'x'}, {id: 1002, user: {id: 1, username: 'evilfer'}, comment: 'x'}]);

            timeout = $timeout;
        });

        httpMock.expectGET("api/security/status");
        openidService.update();
        httpMock.flush();
        timeout.flush();

        scope = {
            _events: {},
            _watches: {},
            $on: function(event, f) {
                this._events[event] = f;
            },
            $watch: function(watched, f) {
                this._watches[watched] = f;
                return 'stopWatching: ' + watched;
            },
            _update: function() {}
        };

        spyOn(scope, '$on').andCallThrough();
        spyOn(scope, '$watch').andCallThrough();
        spyOn(scope, '_update');

    });

    afterEach(function() {
        httpMock.verifyNoOutstandingExpectation();
        httpMock.verifyNoOutstandingRequest();
    });


    it('should have token', function () {
        expect(http.defaults.headers.common['nquire-it-token']).toBe(token);
    });

    it('should receive comments', function() {
        httpMock.expectGET("api/project/1000/comments");

        commentService.get('project', 1000, scope, scope._update);
        expect(scope.comments).toBeDefined();
        expect(scope.comments.list).toBeDefined();
        expect(scope.comments.list.length).toBe(0);
        expect(scope.$watch).toHaveBeenCalledWith('comments.list', scope._update);
        expect(scope._watches['comments.list']).toBe(scope._update);
        expect(scope.$on).toHaveBeenCalled();
        expect(scope._events['$destroy']).toBe('stopWatching: comments.list');

        httpMock.flush();
        timeout.flush();

        expect(scope.comments.list.length).toBe(1);
    });

    it('should delete comment', function() {
        httpMock.expectGET("api/project/1000/comments");
        commentService.get('project', 1000, scope, scope._update);

        httpMock.flush();
        timeout.flush();

        expect(scope.comments.list.length).toBe(1);

        httpMock.expectDELETE("api/project/1000/comments/1001");
        scope.comments.deleteComment(1001);

        httpMock.flush();
        timeout.flush();

        expect(scope.comments.list.length).toBe(0);
    });

    it('should post comment', function() {
        httpMock.expectGET("api/project/1000/comments");
        commentService.get('project', 1000, scope, scope._update);

        httpMock.flush();
        timeout.flush();

        expect(scope.comments.list.length).toBe(1);

        httpMock.expectPOST("api/project/1000/comments", '{"comment":"x"}');
        scope.comments.post('x');

        httpMock.flush();
        timeout.flush();

        expect(scope.comments.list.length).toBe(2);
    });

});

