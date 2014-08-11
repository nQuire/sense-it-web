angular.module('senseItWeb', null, null).controller('ProjectViewSenseItItemCtrl', function ($scope) {

    $scope.commentThread = $scope.item ? {
        thread: $scope.item,
        title: false,
        path: 'project/' + $scope.projectData.project.id + '/senseit/data/' + $scope.item.id,
        postingEnabled: function () {
            return $scope.status.logged &&
                ($scope.projectData.access.admin ||
                    ($scope.projectData.access.member && $scope.projectData.project.open));
        },
        postingDisabledTemplate: $scope.templates.projectDataCommentsDisabled
    } : null;

    $scope.commentVoteManager = $scope.item ? {
        votingEnabled: $scope.commentThread.postingEnabled,
        getPath: function (target) {
            return $scope.commentThread + '/comments/' + target.id + '/vote';
        }
    } : null;

});