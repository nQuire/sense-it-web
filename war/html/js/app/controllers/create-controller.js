


angular.module('senseItWeb', null, null).controller('CreateCtrl', function ($scope, $state, RestService) {
    $scope.iunderstand = false;

    $scope.form = {
        values: {},
        close: function() {
            this.isOpen = false;
        },
        save: function() {
            RestService.post('api/projects', this.values).then(function(response) {
                var id = response.data;
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

