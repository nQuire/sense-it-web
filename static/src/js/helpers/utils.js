
var SigUtils = {
    formatFloat: function(value, sigFig) {
        var power = Math.pow(10, sigFig || 0);
        return String(Math.round(value * power) / power);
    },
    moveArrayItem: function(array, index, up) {
        var otherIndex = index + (up ? -1 : 1);
        if (index >= 0 && index < array.length && otherIndex >= 0 && otherIndex < array.length) {
            var temp = array[otherIndex];
            array[otherIndex] = array[index];
            array[index] = temp;
            return true;
        } else {
            return false;
        }
    }
};