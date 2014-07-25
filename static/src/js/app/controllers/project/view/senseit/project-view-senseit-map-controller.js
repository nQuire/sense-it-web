angular.module('senseItWeb', null, null).controller('ProjectViewSenseItMapCtrl', function ($scope, $filter) {

    $scope.sortableClass = function (column) {
        return column === $scope.sortedData.params.column ? ($scope.sortedData.params.ascending ? 'ascending' : 'descending') : '';
    };

    $scope.sort = function (column) {
        var ascending = column === $scope.sortedData.params.column ?
            !$scope.sortedData.params.ascending : false;

        $scope.sortedData.sort(column, ascending);
    };

    $scope.mapData = {
        infoWindow: function (item) {
            var content = '';
            content += 'Author: ' + item.author.username + '<br/>';
            if (item.date) {
                content += 'On ' + $filter('fuzzyDate')(item.date) + '<br/>';
            }

            for (var i = 0; i < $scope.dataInfo.tableVariables.length; i++) {
                var v = $scope.dataInfo.tableVariables[i];
                content += v.label() + ": " + item.varValue[v.id].v[0] + '<br/>';
            }

            for (var i = 0; i < $scope.dataInfo.plotVariables.length; i++) {
                var v = $scope.dataInfo.plotVariables[i];
                content += '<img src="api/project/' + $scope.projectData.project.id + '/senseit/data/' + item.id + '/' + v.id + '.png" /><br/>';
            }

            return content;
        },
        getItemHeat: function (item) {
            switch ($scope.sortedData.params.column) {
                case 'votes':
                    return item.votes.positive;
                case 'date':
                    return item.date || 0;
                case 'author':
                    return 1;
                default:
                    return $scope.sortedData.params.column in item.varValue ? item.varValue[$scope.sortedData.params.column].v[0] : 0;
            }
        },
        mapVariables: $scope.dataInfo.tableVariables.map(function (v) {
            return {id: v.id, label: v.label(), weight: v.weight};
        }),
        value: function (item, v) {
            return item.varValue[v.id].v[0];
        },
        location: function (item) {
            try {
                return JSON.parse(item.geolocation);
            } catch (e) {
                return null;
            }
        }
    };


});