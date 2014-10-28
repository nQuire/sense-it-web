'use strict';

angular.module('senseItServices', null, null).factory('ProjectService', ['RestService', 'OpenIdService', function (RestService, OpenIdService) {

    var utils = {
        composePath: function (path, suffix) {
            return path + (suffix ? '/' + suffix : '');
        },
        /**
         *
         * @param method
         * @param path
         * @param data
         * @param [files=null]
         * @param [convertToMultipart=false]
         * @returns {*}
         * @private
         */
        request: function (method, path, data, files, convertToMultipart) {
            return data ? RestService[method](path, data, files, convertToMultipart) : RestService[method](path);
        },
        /**
         *
         * @param method
         * @param projectId
         * @param [path=null]
         * @param [data=null]
         * @param [files=null]
         * @param [convertToMultipart=false]
         * @returns {*}
         */
        projectRequest: function (method, projectId, path, data, files, convertToMultipart) {
            var _path = utils.composePath('api/project/' + projectId, path);
            return utils.request(method, _path, data, files, convertToMultipart);
        },

        /**
         *
         * @param method
         * @param [path=null]
         * @param [data=null]
         * @returns {*}
         */
        projectsRequest: function (method, path, data) {
            var _path = this.composePath('api/projects', path);
            return this.request(method, _path, data);
        }
    };


    var ProjectListWatcher = function (scope) {
        var self = this;

        this.data = {projects: [], categories: {}, ready: true};
        this._query = false;

        scope.projectList = this.data;

        var openIdListener = function () {
            self._reload();
        };

        scope.$on('$destroy', function () {
            OpenIdService.removeListener(openIdListener);
        });
        OpenIdService.registerListener(openIdListener);
    };

    ProjectListWatcher.prototype._reload = function () {
        this.data.ready = false;
        var self = this;

        RestService.get('api/projects', this._query).then(function (data) {
            self.data.projects = data.list;
            self.data.categories = data.categories;
            self.data.ready = true;
        });
    };


    ProjectListWatcher.prototype.query = function (query) {
        this._query = query;
        this._reload();
    };

    var MyProjectListWatcher = function (scope) {
        var self = this;

        this.data = {member: [], admin: [], ready: true};
        this._query = false;

        scope.projectList = this.data;

        var openIdListener = function () {
            self._reload();
        };

        scope.$on('$destroy', function () {
            OpenIdService.removeListener(openIdListener);
        });
        OpenIdService.registerListener(openIdListener);

        this._reload();
    };

    MyProjectListWatcher.prototype._reload = function () {
        this.data.ready = false;
        var self = this;
        RestService.get('api/projects/mine').then(function (data) {
            self.data.admin = data.admin;
            self.data.member = data.member;
            self.data.ready = true;
        });
    };


    var ProjectWatcher = function (scope, projectId) {
        var self = this;
        this.projectId = projectId;
        this.scope = scope;

        this.data = {project: null, access: null, data: null, ready: false, reloading: false};

        scope.projectData = this.data;

        var openIdListener = function () {
                self._reload();
        };

        scope.$on('$destroy', function () {
            OpenIdService.removeListener(openIdListener);
        });

        OpenIdService.registerListener(openIdListener);

        this._reload();
    };

    ProjectWatcher.prototype.projectRequest = function (method, path, data, files, convertToMultipart) {
        return utils.projectRequest(method, this.projectId, path, data, files, convertToMultipart);
    };

    ProjectWatcher.prototype._reload = function () {
        var self = this;
        utils.projectRequest('get', this.projectId).then(function (data) {
            self.data.ready = false;
            self.data.project = data.project;
            self.data.access = data.access;
            self.data.data = data.data;
            self.data.ready = true;
        });
    };


    ProjectWatcher.prototype._subscriptionAction = function (action) {
        var self = this;
        return utils.projectRequest('post', this.projectId, action).then(function (data) {
            if (data) {
                self.data.access = data;
            }
            return true;
        });
    };

    ProjectWatcher.prototype.joinProject = function () {
        return this._subscriptionAction('join');
    };

    ProjectWatcher.prototype.leaveProject = function () {
        return this._subscriptionAction('leave');
    };


    ProjectWatcher.prototype.deleteProject = function (callback) {
        var self = this;
        return utils.projectRequest('delete', this.projectId).then(function (deleted) {
            if (deleted) {
                self.data.project = null;
                self.data.access = null;
                self.data.data = null;
                self.data.ready = true;
                if (callback) {
                    callback();
                }
            }
            return deleted;
        });
    };

    /**
     *
     * @param {string} method
     * @param {object} path
     * @param {object} [data=null]
     * @param {object} [files=null]
     * @returns {object}
     * @private
     */
    ProjectWatcher.prototype.updateProjectAction = function (method, path, data, files) {
        var self = this;
        return utils.projectRequest(method, this.projectId, path, data, files).then(function (data) {
            self.data.project = data.project;
            self.data.access = data.access;
            self.data.data = data.data;
            self.data.ready = true;
            return true;
        });
    };

    /**
     *
     * @param {object} [files=null]
     * @returns {object}
     * @private
     */
    ProjectWatcher.prototype.saveMetadata = function (files) {
        return this.updateProjectAction('post', 'metadata', {
            title: this.data.project.title,
            metadata: SiwClone(this.data.project.metadata)
        }, files);
    };


    ProjectWatcher.prototype.openProject = function () {
        return this.updateProjectAction('put', 'admin/open');
    };
    ProjectWatcher.prototype.closeProject = function () {
        return this.updateProjectAction('put', 'admin/close');
    };

    ProjectWatcher.prototype.getUsers = function () {
        return utils.projectRequest('get', this.projectId, 'admin/users');
    };


    return {
        watchList: function (scope) {
            scope.projectListWatcher = new ProjectListWatcher(scope);
        },
        watchMyList: function (scope) {
            scope.projectListWatcher = new MyProjectListWatcher(scope);
        },
        watchProject: function (scope, projectId) {
            scope.projectWatcher = new ProjectWatcher(scope, projectId);
        },
        createProject: function (type, title) {
            return utils.projectsRequest('post', false, {type: type, title: title});
        }
    };


}]);
