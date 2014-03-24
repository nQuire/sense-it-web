/**
 *
 * @param {object} object
 * @param {array} keys
 * @param {function} saveCallback
 * @param {function} [cancelCallback=null]
 * @private
 */
var SiwFormManager = function (object, keys, saveCallback, cancelCallback) {
    this.object = object;
    this.keys = keys;
    this._isOpen = false;
    this.values = {};
    this.saveCallback = saveCallback;
    this.cancelCallback = cancelCallback;
};

SiwFormManager.prototype.setObject = function (object) {
    this.object = object;
};


SiwFormManager.prototype._copyValues = function (from, to) {
    for (var i = 0; i < this.keys.length; i++) {
        var key = this.keys[i];
        to[key] = SiwClone(from[key]);
    }
};

SiwFormManager.prototype.open = function () {
    this._copyValues(this.object, this.values);
    this._isOpen = true;
};

SiwFormManager.prototype.isOpen = function () {
    return this._isOpen;
};

SiwFormManager.prototype._close = function () {
    this._isOpen = false;
};

SiwFormManager.prototype.cancel = function () {
    this._close();
    if (this.cancelCallback) {
        this.cancelCallback();
    }
};

SiwFormManager.prototype.save = function () {
    this._copyValues(this.values, this.object);
    this._close();
    if (this.saveCallback) {
        this.saveCallback();
    }
};

