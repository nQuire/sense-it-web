'use strict';

angular.module('senseItServices', null, null).factory('ProjectService', ['$rootScope', 'RestService', function ($rootScope, RestService) {

    var service = {
        _projectData: {}
    };

    service._load = function (id) {
        RestService.get('api/project/' + id).then(function (response) {
            service._projectData[id].project = response.data;
            service._projectData[id].ready = true;
        });
    };

    service.get = function (projectId) {
        if (!(projectId in service._projectData)) {
          // $rootScope.$apply(function () {
                service._projectData[projectId] = {
                    ready: false,
                    project: null
                };
          // });
        }

        service._load(projectId);
        return service._projectData[projectId];
    };

    service.saveMetadata = function(projectId) {
        var project = service._projectData[projectId];

        RestService.put('api/project/' + projectId, )
    };

    return service;
}]);
