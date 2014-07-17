angular.module('senseItWeb', null, null).controller('ProjectViewDataItemCtrl', function ($scope, $state) {

    $scope.goto = {
        hasNext: function () {
            return $state.params.itemIndex < $scope.sortedData.data.length;
        },
        next: function () {
            $state.go('project.view.data-item', {itemIndex: parseInt($state.params.itemIndex) + 1});
        },
        hasPrevious: function () {
            return $state.params.itemIndex > 1;
        },
        previous: function () {
            $state.go('project.view.data-item', {itemIndex: parseInt($state.params.itemIndex) - 1});
        }
    };

    var update = function () {
        $scope.item = $scope.dataReady && $scope.sortedData.data.length >= $state.params.itemIndex ? $scope.sortedData.data[$state.params.itemIndex - 1] : null;

        $scope.commentThread = $scope.item ? {
            thread: $scope.item,
            title: 'Image discussion',
            path: 'project/' + $scope.projectData.project.id + '/spotit/data/' + $scope.item.id,
            postingEnabled: function () {
                return $scope.status.logged &&
                    ($scope.projectData.access.admin ||
                        ($scope.projectData.access.member && $scope.projectData.project.open));
            }
        } : null;
    };

    $scope.deleteDataItem = function() {
        $scope.deleteData($scope.item, 'project.view.data-list');
    };

    $scope.$on('$destroy', $scope.$watch('dataReady', update));
    update();

});