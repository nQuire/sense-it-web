angular.module('senseItWeb', null, null).controller('ForumNodeNewThreadCtrl', function ($scope, $state) {

    $scope.data = {title: "", message: ""};

    $scope.createThread = function() {
        $scope.forum.postNewThread($scope.forum.node.id, $scope.data).then(function(id) {
            if (id) {
                $state.go('forum.thread', {threadId: id});
            }
        });
    };

});