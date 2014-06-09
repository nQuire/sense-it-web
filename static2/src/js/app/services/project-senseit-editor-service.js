'use strict';

angular.module('senseItServices', null, null).factory('ProjectSenseItEditService', ['ProjectService', function () {

    var ProjectSenseItEditor = function(projectWatcher) {
        this.projectWatcher = projectWatcher;
    };

    ProjectSenseItEditor.prototype._request = function(method, path, data) {
        var _path = 'senseit' + (path ? '/' + path : '');
        return this.projectWatcher.updateProjectAction(method, _path, data);
    };


    ProjectSenseItEditor.prototype.createInput = function (input) {
        return this._request('post', 'inputs', {
            sensor: input.sensor,
            rate: input.rate
        });
    };

    ProjectSenseItEditor.prototype.updateInput = function (input) {
        return this._request('put', 'input/' + input.id, {
            sensor: input.sensor,
            rate: input.rate
        });
    };

    ProjectSenseItEditor.prototype.deleteInput = function (inputId) {
        return this._request('delete', 'input/' + inputId);
    };

    ProjectSenseItEditor.prototype.updateProfile = function (profile) {
        return this._request('put', 'profile', {
            geolocated: profile.geolocated,
            tx: profile.tx
        });
    };

    return {
        senseitEditor: function(projectWatcher) {
            return new ProjectSenseItEditor(projectWatcher);
        }
    };
}]);
