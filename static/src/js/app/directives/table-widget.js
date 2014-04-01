/*global $*/

angular.module('senseItWeb', null, null).directive('siwSortableTableWidget', function () {
    return {
        link: function ($scope, element, params) {

            element.addClass('sortable-table');

            $scope.sort = {
                column: false,
                ascending: true,
                compare: function (a, b) {
                    var c = $scope.sort.f[$scope.sort.column](a, b);
                    return $scope.sort.ascending ? c : -c;
                },
                f: $scope[params.tableData].sort
            };

            $scope.headerClass = function (column) {
                return $scope.sort.f[column] && column === $scope.sort.column ? ($scope.sort.ascending ? 'ascending' : 'descending') : '';
            };

            $scope.rowClass = function (item) {
                return $scope.selected == item ? 'selected' : '';
            };

            $scope.headerSort = function (column) {
                if ($scope.sort.f[column]) {
                    if (column === $scope.sort.column) {
                        $scope.sort.ascending = !$scope.sort.ascending;
                    } else {
                        $scope.sort.column = column;
                        $scope.sort.ascending = true;
                    }
                }
            };

            $scope.unsortedItems = typeof $scope[params.tableData].items === 'function' ? $scope[params.tableData].items : function () {
                return $scope[params.tableData].items;
            };

            $scope.sortedItems = function () {
                var l = $scope.unsortedItems();
                if ($scope.sort.column) {
                    l.sort($scope.sort.compare);
                }
                return l;
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
        },
        controller: function ($scope) {

        }
    };
});


