/*global $*/

angular.module('senseItWeb', null, null).directive('siwSortableTableWidget', function () {
    return {
        link: function ($scope, element, params) {
            element.addClass('sortable-table');

            var sortAscending = params.sortColumn && params.sortColumn.length > 0 && params.sortColumn[0] == '-';
            var sortColumn = sortAscending ? params.sortColumn.substr(1) : (params.sortColumn || null);

            $scope.sortedData.sort(sortColumn, !!sortAscending);

            $scope.headerClass = function (column) {
                return column === $scope.sortedData.params.column ? ($scope.sortedData.params.ascending ? 'ascending' : 'descending') : '';
            };

            $scope.rowClass = function (item) {
                return $scope.selected == item ? 'selected' : '';
            };

            $scope.headerSort = function (column) {
                var ascending = column === $scope.sortedData.params.column ?
                    !$scope.sortedData.params.ascending : false;

                $scope.sortedData.sort(column, ascending);
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
        }
    };
});


