/**
 *
 * @param {object} object
 * @param {array} keys
 * @param {function} saveCallback
 * @param {function} [cancelCallback=null]
 * @private
 */
var SiwFormManager = function (object, keys, saveCallback, cancelCallback) {
    this.getObject = !(object && object.constructor && object.call && object.apply) ? function() {
        return object;
    } : object;

    this.keys = keys;
    this._isOpen = false;
    this.values = {};
    this.saveCallback = saveCallback;
    this.cancelCallback = cancelCallback;
    this.files = {};
};

SiwFormManager.prototype.setFile = function(name, file) {
    this.files[name] = file;
};

SiwFormManager.prototype.clearFile = function(name) {
    delete this.files[name];
};

SiwFormManager.prototype.deleteFile = function(name) {
    this.files[name] = false;
};

SiwFormManager.prototype._copyValues = function (from, to) {
    for (var i = 0; i < this.keys.length; i++) {
        var key = this.keys[i];
        to[key] = SiwClone(from[key]);
    }
};

/**
 *
 * @param [mode=null]
 */
SiwFormManager.prototype.open = function (mode) {
    this.files = {};
    this._copyValues(this.getObject(), this.values);
    this._isOpen = mode || true;
};

/**
 *
 * @param [mode=null]
 * @returns {boolean}
 */
SiwFormManager.prototype.isOpen = function (mode) {
    return mode ? this._isOpen == mode : this._isOpen;
};

SiwFormManager.prototype.getMode = function () {
    return this.mode;
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
    this._copyValues(this.values, this.getObject());
    this._close();
    if (this.saveCallback) {
        this.saveCallback();
    }
};

