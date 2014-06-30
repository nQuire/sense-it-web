angular.module('senseItWeb', null, null).controller('ForumNodeCtrl', function ($scope, $state) {

    $scope.forum.getNode($state.params.forumId);

});