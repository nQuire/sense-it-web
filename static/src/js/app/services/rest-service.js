'use strict';

angular.module('senseItServices', null, null).factory('RestService', ['$http', function ($http) {

    var service = {
        errorListeners: [],
        registerErrorListener: function(listener) {
          this.errorListeners.push(listener);
        },
        _promiserequest: function(promise) {
            return promise.then(function(response) {
                if (response.data) {
                    return response.data;
                } else {
                    for (var i = 0; i < service.errorListeners.length; i++) {
                        service.errorListeners[i]();
                    }
                    return null;
                }
            });
        },
        get: function(path) {
            return service._promiserequest($http.get(path));
        },
        _createUploadPromise: function(method, path, data, files) {
            var hasFiles = false;
            if (files) {
                for (var fileId in files) {
                    if (files.hasOwnProperty(fileId)) {
                        hasFiles = true;
                        break;
                    }
                }
            }

            var promise;

            if (hasFiles) {
                var fd = new FormData();

                fd.append("body", "body=" + JSON.stringify(data));

                for (fileId in files) {
                    if (files.hasOwnProperty(fileId)) {
                        fd.append("files[" + fileId + "]", files[fileId]);
                    }
                }

                promise = $http[method](path + '/files', fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                });

            } else {
                promise = $http[method](path, data);
            }
            return service._promiserequest(promise);
        },
        /**
         *
         * @param path
         * @param data
         * @param [files=null]
         * @returns {*}
         */
        post: function(path, data, files) {
            return service._createUploadPromise('post', path, data, files);
        },
        /**
         *
         * @param path
         * @param data
         * @param [files=null]
         * @returns {*}
         */
        put: function(path, data, files) {
            return service._createUploadPromise('put', path, data, files);
        },
        delete: function(path) {
            return service._promiserequest($http.delete(path));
        },
        setToken: function(token) {
            $http.defaults.headers.common.token = token;
        }
    };

    return service;
}]);
