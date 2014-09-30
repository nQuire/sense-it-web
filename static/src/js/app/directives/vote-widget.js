angular.module('senseItWeb', null, null).directive('siwVoteWidget', function (ModalService) {
  return {
    templateUrl: 'partials/widgets/vote-widget.html',
    scope: {
      'voteManager': '=',
      'voteTarget': '='
    },

    controller: function ($scope, VoteService) {
      $scope.voteIconClass = function (iconValue) {
        var classes = [];

        if ($scope.voteManager.votingEnabled()) {
          classes.push('enabled');
        }

        if (iconValue == -2) {
          if ($scope.voteTarget.voteCount.myVote && $scope.voteTarget.voteCount.myVote.value == -2) {
            classes.push('selected');
          } else {
            classes.push('not-selected');
          }
        } else {
          var state = $scope.voteTarget.voteCount.myVote ? iconValue * $scope.voteTarget.voteCount.myVote.value : 0;
          if (state == 1) {
            classes.push('selected');
          } else if (state == -1) {
            classes.push('not-selected');
          }
        }

        return classes;
      };


      $scope.vote = function (iconValue) {
        if ($scope.voteManager.votingEnabled()) {
          var value = $scope.voteTarget.voteCount && $scope.voteTarget.voteCount.myVote && $scope.voteTarget.voteCount.myVote.value == iconValue ? 0 : iconValue;
          VoteService.vote($scope.voteManager.getPath($scope.voteTarget), {value: value}).then(function (voteCount) {
            if (voteCount) {
              $scope.voteTarget.voteCount = voteCount;
            }
          });
        }
      };

      $scope.report = function () {
        if ($scope.voteTarget.voteCount) {
          if ($scope.voteTarget.voteCount.myVote.value == -2) {
            $scope.vote(0);
          } else {
            ModalService.open({
              body: '<p>Are you sure you want to report this as inapropriate content?</p>' +
                '<p><b>Please note that the system admin will be notified, and ' +
                'the content will be removed if demmed inappropriate.</b></p>' +
                '<p>As this site is intended for all ages, please only report content ' +
                'that is offensive or unsuitable for young people.</p>',
              title: 'Report inappropriate content',
              ok: function () {
                $scope.vote(-2);
                return true;
              }
            });
          }
        }
      };
    }
  };
});
