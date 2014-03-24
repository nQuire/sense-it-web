


angular.module('senseItWeb', null, null).controller('CreateCtrl', function ($scope, $state, ProjectService, OpenIdService) {
    OpenIdService.registerWatcher($scope);

    $scope.iunderstand = false;

    $scope.form = {
        values: {},
        close: function() {
            this.isOpen = false;
        },
        save: function() {
            ProjectService.createProject(this.values).then(function(id) {
                $state.go('project-edit', { projectId: id });
            });
        },
        reset: function() {
            this.values = {};
        },
        okButtonDisabled: function() {
            return !this.values.type || !this.values.title || !this.values.description;
        },
        resetButtonDisabled: function() {
            return !this.values.type && !this.values.title && !this.values.description;
        }
    };

});

