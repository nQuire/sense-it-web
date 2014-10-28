angular.module('senseItWeb', null, null).controller('AdminReportedCtrl', function ($scope, ModalService) {

  $scope.category = null;
  $scope.content = null;
  $scope.blocks = [];

  $scope.categoryMenuItemClass = function (cat) {
    return cat == $scope.category ? 'selected' : '';
  };

  var _selectContent = function () {
    $scope.blocks = [];
    $scope.content = $scope.admin.data.reported[$scope.category];
    if ($scope.content.length > 0) {
      var fib = $scope.content[0].info;
      for (var i = 0; i < fib.length; i++) {
        $scope.blocks.push(fib[i].title);
      }
    }
  };

  $scope.selectCategory = function (cat) {
    if (cat != $scope.category) {
      $scope.category = cat;
      _selectContent();
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


  var confirm = function (item, method, title, body) {
    ModalService.open({
      body: body,
      title: title,
      ok: function () {
        $scope.admin[method](item.id).then(function () {
          if (!($scope.category in $scope.admin.data.reported)) {
            for (var cat in $scope.admin.data.reported) {
              $scope.category = null;
              if ($scope.admin.data.reported.hasOwnProperty(cat)) {
                $scope.category = cat;
                break;
              }
            }
          }
          _selectContent();
        });
        return true;
      }
    });
  };

  $scope.deleteReportedContent = function (item) {
    confirm(item, 'deleteReportedContent', 'Delete entity', '<p>Are you sure you want to delete this entity?</p>' +
    '<p><b>Please note that this action cannot be undone!</b></p>');
  };

  $scope.approveReportedContent = function (item) {
    confirm(item, 'approveReportedContent', 'Approve entity', '<p>Are you sure you want to approve this entity?</p>' +
    '<p>Users have flagged it as inappropriate, please review it before approving it.</p>');
  };
});
