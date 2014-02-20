/*global $*/

angular.module('senseItWeb', null, null).directive('siwSortableTableWidget', function () {
    return {
        link: function (scope, element) {
            element.addClass('sortable-table');
        },
        controller: function ($scope) {
            $scope.sort = {
                column: false,
                ascending: true,
                compare: function (a, b) {
                    var c = $scope.tableData.sort[$scope.sort.column](a, b);
                    return $scope.sort.ascending ? c : -c;
                }
            };

            $scope.selected = null;

            $scope.headerClass = function (column) {
                return $scope.tableData.sort[column] && column === $scope.sort.column ? ($scope.sort.ascending ? 'ascending' : 'descending') : '';
            };

            $scope.rowClass = function(item) {
                return $scope.selected == item ? 'selected' : '';
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

            $scope.select = function(item) {
                $scope.selected = (item == $scope.selected) ? null : item;
                $scope.tableData.selectCallback($scope.selected);
            };
        }
    };
});
