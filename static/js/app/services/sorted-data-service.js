'use strict';

angular.module('senseItServices', null, null).factory('SortedDataService', [function () {

    var SortedData = function (getDataFunction, sortFunctions, filterFunctions) {
        this.getDataFunction = getDataFunction;
        this.sortFunctions = sortFunctions;
        this.filterFunctions = filterFunctions || [];

        this.params = {
            column: null,
            ascending: true
        };

        var self = this;
        this._compare = function (a, b) {
            if (self.params.column in self.sortFunctions) {
                var c = self.sortFunctions[self.params.column](a, b);
                return self.params.ascending ? c : -c;
            } else {
                return 0;
            }
        };

        this._filter = function(a) {
            for (var i = 0; i < self.filterFunctions.length; i++) {
                if (!self.filterFunctions(a)) {
                    return false;
                }
            }

            return true;
        };

        this._update();
    };


    SortedData.prototype.sort = function (column, ascending) {
        this.params = {column: column, ascending: ascending};
        this._sortData();
    };

    SortedData.prototype._update = function () {
        this._originalData = this.getDataFunction();
        this._sortData();
    };

    SortedData.prototype._sortData = function () {
        this.data = this._originalData.filter(this._filter);
        this.data.sort(this._compare);
    };


    return {
        get: function (getDataFunction, sortFunctions, filterFunctions, scope, watchKey, filterWatch) {
            var sd = new SortedData(getDataFunction, sortFunctions, filterFunctions);
            if (scope) {
                if (watchKey) {
                    scope.$on('$destroy', scope.$watch(watchKey, function () {
                        sd._update();
                    }));
                }

                if (filterWatch) {
                    scope.$on('$destroy', scope.$watch(filterWatch, function () {
                        sd._sortData();
                    }, true));
                }
            }
            return sd;
        }
    };
}]);