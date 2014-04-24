function SiwClone(obj) {
    if (obj == null) {
        return null;
    } else if (Object.prototype.toString.call(obj) === '[object Array]') {
        var temp = [], len = obj.length;
        for (var i = 0; i < len; i++) {
            temp[i] = SiwClone(obj[i]);
        }
    } else if (typeof (obj) == 'object') {
        temp = obj.constructor();
        for (var key in obj) {
            if (obj.hasOwnProperty(key) && key != "$$hashKey") {
                temp[key] = SiwClone(obj[key]);
            }
        }
    } else {
        temp = obj;
    }

    return temp;
}