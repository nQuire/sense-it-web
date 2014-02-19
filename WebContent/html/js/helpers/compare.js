/**
 * Created by evilfer on 2/17/14.
 */

var siwCompare = {

    string: function(a, b) {
        var _a = a.toLowerCase();
        var _b = b.toLowerCase();
        return _a < _b ? -1 : (_a > _b ? 1 : 0);
    },
    voteCount: function(a, b) {
        var c = a.positive - a.negative - b.positive + b.negative;
        return c === 0 ? a.positive - b.positive : c;
    }
};
