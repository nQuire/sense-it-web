/**
 * Created by evilfer on 2/17/14.
 */

var siwVotes = {

    count: function (votes) {
        var voteCount = {
            positive: 0,
            negative: 0,
            total: 0,
            balance: 0
        };
        for (var i = 0; i < votes.length; i++) {
            if (votes[i].value > 0) {
                voteCount.positive++;
            } else if (votes[i].value < 0) {
                voteCount.negative--;
            }
        }

        return voteCount;
    },

    countVotable: function(votableList, votesKey, voteCountKey) {
        for (var i = 0; i < votableList.length; i++) {
            votableList[i][voteCountKey] = this.count(votableList[i][votesKey]);
        }
    }
};
