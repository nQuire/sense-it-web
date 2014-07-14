/*global $*/

angular.module('senseItWeb', null, null).directive('siwSortableTableWidget', function () {
    return {
        link: function ($scope, element, params) {
            element.addClass('sortable-table');

            var sortDescending = params.sortColumn && params.sortColumn.length > 0 && params.sortColumn[0] == '-';
            var sortColumn = sortDescending ? params.sortColumn.substr(1) : (params.sortColumn || false);

            $scope.sort = {
                sort: function () {
                    $scope[params.tableData].items.sort($scope.sort.compare);
                },
                column: sortColumn,
                ascending: !sortDescending,
                compare: function (a, b) {
                    if ($scope.sort.f[$scope.sort.column]) {
                        var c = $scope.sort.f[$scope.sort.column](a, b);
                        return $scope.sort.ascending ? c : -c;
                    } else {
                        return 0;
                    }
                },
                f: $scope[params.tableData].sort
            };

            $scope.headerClass = function (column) {
                return $scope.sort.f[column] && column === $scope.sort.column ? ($scope.sort.ascending ? 'ascending' : 'descending') : '';
            };

            $scope.rowClass = function (item) {
                return $scope.selected == item ? 'selected' : '';
            };

            $scope.headerSort = function (column, descending) {
                if ($scope.sort.f[column]) {
                    if (column === $scope.sort.column) {
                        $scope.sort.ascending = !$scope.sort.ascending;
                    } else {
                        $scope.sort.column = column;
                        $scope.sort.ascending = !descending;
                    }
                }

                $scope.sort.sort();
            };


            $scope.sortedItems = function () {
                if ($scope.sort.needSorting) {

                    $scope.sort.needSorting = false;
                }

                return $scope[params.tableData].items;
            };

            $scope.select = function (item, button, onlyOn) {
                if (item != $scope[params.tableData].selected) {
                    var notify = true;
                } else if (!onlyOn) {
                    notify = true;
                    item = null;
                }

                if (notify) {
                    $scope[params.tableData].selected = item;
                    $scope[params.tableData].selectCallback(item, button);
                }
            };

            $scope.selectButtonClass = function (item) {
                return $scope.itemIsSelected(item) ? 'pure-button-active' : 'pure-button-primary';
            };

            $scope.itemIsSelected = function (item) {
                return $scope[params.tableData].selected == item;
            };

            $scope.headerSort(sortColumn, sortDescending);

            $scope.$on('$destroy', $scope.$watch(params.tableData + '.items', function () {
                $scope.sort.sort();
            }));
        }
    };
});


