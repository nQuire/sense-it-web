angular.module('senseItWeb', null, null).controller('ProjectViewDataItemCtrl', function ($scope, $state) {


    $scope.goto = {
        next: function () {
            $state.go('project.view.data-item', {itemId: $scope.itemIndex.next});
        },
        previous: function () {
            $state.go('project.view.data-item', {itemId: $scope.itemIndex.previous});
        }
    };

    var update = function () {
        if ($state.params.itemId) {
            $scope.itemIndex = null;
            $scope.item = null;
            for (var i = 0; i < $scope.sortedData.data.length; i++) {
                if ($scope.sortedData.data[i].id == $state.params.itemId) {
                    $scope.itemIndex = {
                        index: i,
                        next: i < $scope.sortedData.data.length - 1 ? $scope.sortedData.data[i + 1].id : false,
                        previous: i > 0 ? $scope.sortedData.data[i - 1].id : false
                    };
                    $scope.item = $scope.sortedData.data[i];
                    break;
                }
            }

            if ($scope.item) {

                $scope.commentThread = {
                    thread: $scope.item,
                    title: 'Image discussion',
                    path: 'project/' + $scope.projectData.project.id + '/' + $scope.projectData.project.type + '/data/' + $scope.item.id,
                    postingEnabled: function () {
                        return $scope.status.logged &&
                            ($scope.projectData.access.admin ||
                                ($scope.projectData.access.member && $scope.projectData.project.open));
                    },
                    postingDisabledTemplate: $scope.templates.projectDataCommentsDisabled
                };

                $scope.commentVoteManager = {
                    votingEnabled: $scope.commentThread.postingEnabled,
                    getPath: function (target) {
                        return 'api/project/' + $scope.projectData.project.id + '/' + $scope.projectData.project.type +
                            '/data/' + $scope.item.id + '/comments/' + target.id + '/vote';
                    }
                };
            }
        }
    };

    $scope.deleteDataItem = function () {
        $scope.deleteData($scope.item, 'project.view.data-list');
    };

    $scope.$on('$destroy', $scope.$watch('sortedData.data', update));

    update();
});