angular.module('senseItWeb', null, null).controller('ForumCtrl', function ($scope, ForumService) {

    ForumService.get($scope);


});