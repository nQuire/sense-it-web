
var siwFormManager = function(object, keys, saveCallback) {
    this.object = object;
    this.keys = keys;
    this._isOpen = false;
    this.values = {};
    this.saveCallback = saveCallback;
};


siwFormManager.prototype._copyValues = function(from, to) {
    for (var i = 0; i < this.keys.length; i++) {
        var key = this.keys[i];
        to[key] = from[key];
    }
};

siwFormManager.prototype.open = function() {
    this._copyValues(this.object, this.values);
    this._isOpen = true;
};

siwFormManager.prototype.isOpen = function() {
    return this._isOpen;
};

siwFormManager.prototype._close = function() {
    this._isOpen = false;
};

siwFormManager.prototype.cancel = function() {
    this._close();
};

siwFormManager.prototype.save = function() {
    this._copyValues(this.values, this.object);
    this._close();
    this.saveCallback();
};

