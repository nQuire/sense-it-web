angular.module('senseItWeb', null, null).controller('AdminTextsCtrl', function ($scope) {

    $scope.itemList = [
        {id: 'headerSubtitle', label: 'Header subtitle', format: false},
        {id: 'nquireTeaser', label: 'Project list teaser', format: true},
        {id: 'about', label: 'About', format: true},
        {id: 'createSenseIt', label: 'Create: Sense It intro', format: true},
        {id: 'createSenseItHelp', label: 'Create: Sense It help', format: true},
        {id: 'createSpotIt', label: 'Create: Spot It intro', format: true},
        {id: 'createSpotItHelp', label: 'Create: Spot It help', format: true},
        {id: 'createWinIt', label: 'Create: Win It intro', format: true},
        {id: 'createWinItHelp', label: 'Create: Win It help', format: true}
    ];

});