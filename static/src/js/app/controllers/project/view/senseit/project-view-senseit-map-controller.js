angular.module('senseItWeb', null, null).controller('ProjectViewSenseItMapCtrl', function ($scope, $filter, $state) {

    $scope.sortableClass = function (column) {
        return column === $scope.sortedData.params.column ? ($scope.sortedData.params.ascending ? 'ascending' : 'descending') : '';
    };

    $scope.sort = function (column) {
        var ascending = column === $scope.sortedData.params.column ?
            !$scope.sortedData.params.ascending : false;

        $scope.sortedData.sort(column, ascending);
    };

    $scope.goto = {
        index: function (index) {
            $scope.mapData.selected = $scope.sortedData.data[index].id;
        },
        first: function () {
            this.index(0);
        },
        next: function () {
            if ($scope.mapData.selectedIndex >= 0) {
                this.index($scope.mapData.selectedIndex < $scope.sortedData.data.length - 1 ? $scope.mapData.selectedIndex + 1 : 0);
            }
        },
        previous: function () {
            if ($scope.mapData.selectedIndex >= 0) {
                this.index($scope.mapData.selectedIndex > 0 ? $scope.mapData.selectedIndex - 1 : $scope.sortedData.data.length - 1);
            }
        },
        last: function () {
            this.index($scope.sortedData.data.length - 1);
        }
    };


    $scope.mapData = {
        selected: $state.params.item,
        selectedIndex: -1,
        textKey: $scope.dataInfo.tableVariables.length > 0 ? 0 : 'date',
        iconText: function (item) {
            var text = '';
            console.log(item);
            switch (this.textKey) {
                case 'date':
                    if (item.date) {
                        var format = 'shortDate';
                        var now = new Date();
                        var date = new Date(item.date);
                        if (now.getFullYear() == date.getFullYear() && now.getMonth() == date.getMonth() && now.getDate() == date.getDate()) {
                            format = 'H:mm';
                        }
                        text = $filter('date')(item.date, format);
                    }
                    break;
                case 'author':
                    text = item.author.username;
                    break;
                case 'votes':
                    text = item.voteCount.positive + '/' + (-item.voteCount.negative);
                    break;
                default:
                    if (this.textKey in $scope.dataInfo.tableVariables) {
                        text = this.value(item, $scope.dataInfo.tableVariables[this.textKey]).toPrecision(5);
                    }
                    break;
            }

            return text.length > 10 ? text.substr(0, 10) : text;
        },
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
                    return item.voteCount.positive;
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
            if (!item || !item.varValue[v.id]) {
                return 0;
            }
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


    $scope.showOptions = [
        {label: 'Author', value: 'author'},
        {label: 'Date', value: 'date'},
        {label: 'Votes', value: 'votes'}
    ];

    for (var i = 0; i < $scope.mapData.mapVariables.length; i++) {
        var v = $scope.mapData.mapVariables[i];
        $scope.showOptions.push({label: v.label, value: i});
    }


});