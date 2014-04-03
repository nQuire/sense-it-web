

exports.httpBackendMock = function () {
    angular.module('httpBackendMock', ['senseItWeb', 'ngMockE2E']).run(function ($httpBackend) {


        console.log('app backend:');
        console.log('comments-project-author')
        console.log($httpBackend);

        $httpBackend.whenGET(/partials+/).passThrough();

        var id = 1;
        var profile = {
            "id": id, "name": id.toString(), "openIds": [
                {"id": 11, "openId": "https://example.com/id?id=me", "email": "me@example.com"}
            ]
        };

        $httpBackend.whenGET('api/openid/profile').respond({
            "logged": true,
            "profile": profile,
            "token": 'tkn'
        });

        var projects = [
            {
                id: 101,
                title: 'p101',
                type: 'senseit'
            }
        ];

        var project101 = {
            project: projects[0],
            access: {member: true, admin: true, author: true}
        };

        var project101comments = [
            {id: 1001, author: {id: 1, name: '1'}, comment: 'c1'},
            {id: 1002, author: {id: 2, name: '2'}, comment: 'c2'}
        ];

        senseItWebDev._httpBackend.whenGET('api/projects').respond(projects);

        senseItWebDev._httpBackend.whenGET('api/project/101').respond(project101);
        senseItWebDev._httpBackend.whenGET('api/project/101/comments').respond(project101comments);
    });
};
