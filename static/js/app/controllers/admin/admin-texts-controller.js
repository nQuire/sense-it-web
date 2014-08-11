angular.module('senseItWeb', null, null).controller('AdminTextsCtrl', function ($scope) {

    $scope.itemList = [
        {id: 'headerSubtitle', label: 'Header subtitle', format: false},
        {id: 'nquireTeaser', label: 'Project list teaser', format: true},
        {id: 'about', label: 'About', format: true}
    ];

});