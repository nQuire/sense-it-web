


angular.module('senseItWeb', null, null).controller('ProjectListMineCtrl', function ($scope, $state, ProjectService) {

    ProjectService.watchMyList($scope);

});

