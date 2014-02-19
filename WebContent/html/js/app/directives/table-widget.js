/*global $*/

angular.module('senseItWeb', null, null).directive('siwSortableTableWidget', function () {
    return {
        link: function (scope, element) {
            element.addClass('sortable-table');
        },
        controller: function ($scope) {
            console.log($scope);
            $scope.sort = {
                column: false,
                ascending: true,
                compare: function (a, b) {
                    var c = $scope.tableData.sort[$scope.sort.column](a, b);
                    return $scope.sort.ascending ? c : -c;
                }
            };

            $scope.headerClass = function (column) {
                return $scope.tableData.sort[column] && column === $scope.sort.column ? ($scope.sort.ascending ? 'ascending' : 'descending') : '';
            };

            $scope.headerSort = function (column) {
                if ($scope.tableData.sort[column]) {
                    if (column === $scope.sort.column) {
                        $scope.sort.ascending = !$scope.sort.ascending;
                    } else {
                        $scope.sort.column = column;
                        $scope.sort.ascending = true;
                    }
                }
            };

            $scope.test = "test";

            $scope.unsortedItems = typeof $scope.tableData.items === 'function' ? $scope.tableData.items : function () {
                return $scope.tableData.items;
            };


            $scope.sortedItems = function () {
                var l = $scope.unsortedItems();
                if ($scope.sort.column) {
                    l.sort($scope.sort.compare);
                }
                return l;
            }
        }
    };
});
