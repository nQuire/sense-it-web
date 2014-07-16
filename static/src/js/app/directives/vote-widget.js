angular.module('senseItWeb', null, null).directive('siwVoteWidget', function () {
    return {
        templateUrl: 'partials/widgets/vote-widget.html',
        scope: {
            'voteManager': '=',
            'voteTarget': '='
        },

        controller: function ($scope, VoteService) {
            $scope.voteIconClass = function (iconValue) {
                var classes = []

                if ($scope.voteManager.votingEnabled()) {
                    classes.push('enabled');
                }

                var state = $scope.voteTarget.voteCount.myVote ? iconValue * $scope.voteTarget.voteCount.myVote.value : 0;
                if (state > 0) {
                    classes.push('selected');
                } else if (state < 0) {
                    classes.push('not-selected');
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
        }
    };
});
