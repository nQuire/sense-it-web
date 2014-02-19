angular.module('senseItWeb', null, null).directive('siwVoteWidget', function () {
    return {
        templateUrl: 'partials/vote-widget.html',
        scope: {
            'voteManager': '=',
            'voteTarget': '='
        },

        controller: function ($scope, VoteService) {
            $scope.voteCount = $scope.voteManager.getVoteCount($scope.voteTarget);

            $scope.voteIconClass = function (iconValue) {
                var classes = []

                if ($scope.voteManager.votingEnabled) {
                    classes.push('enabled');
                }

                var state = $scope.voteCount.myVote ? iconValue * $scope.voteCount.myVote.value : 0;
                if (state > 0) {
                    classes.push('selected');
                } else if (state < 0) {
                    classes.push('not-selected');
                }

                return classes;
            };

            $scope.vote = function (iconValue) {
                if ($scope.voteManager.votingEnabled) {
                    var value = $scope.voteCount && $scope.voteCount.myVote && $scope.voteCount.myVote.value == iconValue ? 0 : iconValue;
                    VoteService.vote($scope.voteManager.getPath($scope.voteTarget), {value: value}).then(function (voteCount) {
                        if (voteCount) {
                            $scope.voteCount = voteCount;
                            $scope.voteManager.setVoteCount($scope.voteTarget, voteCount);
                        }
                    });
                }
            };
        }
    };
});
