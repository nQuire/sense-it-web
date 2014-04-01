
var SigUtils = {
    formatFloat: function(value, sigFig) {
        var power = Math.pow(10, sigFig || 0);
        return String(Math.round(value * power) / power);
    }
};