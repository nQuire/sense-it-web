angular.module('senseItWeb', null, null).controller('AdminReportedCtrl', function ($scope) {

  $scope.category = null;
  $scope.content = null;
  $scope.blocks = [];

  $scope.categoryMenuItemClass = function (cat) {
    return cat == $scope.category ? 'selected' : '';
  };

  $scope.selectCategory = function (cat) {
    if (cat != $scope.category) {
      $scope.category = cat;
      $scope.blocks = [];
      $scope.content = $scope.admin.data.reported[$scope.category];
      if ($scope.content.length > 0) {
        var fib = $scope.content[0].info;
        for (var i = 0; i < fib.length; i++) {
          $scope.blocks.push(fib[i].title);
        }
      }
    }
  };

  $scope.labels = {
    'challengeIdea': 'Challenge ideas',
    'forumThread': 'Forum threads',
    'comment:forumThread': 'Forum posts',
    'project': 'Projects',
    'comment:project': 'Comments (Project)',
    'senseitSeries': 'Sense-it series',
    'comment:senseitSeries': 'Comments (Sense-it series)',
    'spotitObservation': 'Spot-it observations',
    'comment:spotitObservation': 'Comments (Spot-it observation)'
  };

  $scope.admin.getReportedContent().then(function () {
    if ($scope.admin.data.reported) {
      for (var cat in $scope.admin.data.reported) {
        if ($scope.admin.data.reported.hasOwnProperty(cat)) {
          $scope.selectCategory(cat);
          break;
        }
      }
    }
  });


});