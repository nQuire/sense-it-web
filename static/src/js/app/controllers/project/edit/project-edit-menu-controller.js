


angular.module('senseItWeb', null, null).controller('ProjectEditMenuCtrl', function ($scope, $state) {

    $scope.projectEditMenuIsActive = function(tab) {
        return $state.current.name.indexOf('project.edit.' + tab) === 0;
    };

    $scope.type = function(type) {
        return $scope.projectData.project.type == type;
    };

});

